package me.ermioni.scclient.services.websocket;

import android.util.Log;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import me.ermioni.scclient.services.ICallBack;
import me.ermioni.scclient.services.ISCChannelListener;
import me.ermioni.scclient.services.websocket.adapter.WebSocketAdapter;
import me.ermioni.scclient.services.websocket.socketcluster.callback.IResponseCallback;
import me.ermioni.scclient.services.websocket.socketcluster.channel.CHANNEL_STATE;
import me.ermioni.scclient.services.websocket.socketcluster.channel.SCChannel;
import me.ermioni.scclient.services.websocket.socketcluster.requests.BaseRequest;
import me.ermioni.scclient.services.websocket.socketcluster.requests.LoginRequest;
import me.ermioni.scclient.services.websocket.socketcluster.requests.PublishRequest;
import me.ermioni.scclient.services.websocket.socketcluster.requests.PublishToChannelRequest;
import me.ermioni.scclient.services.websocket.socketcluster.requests.SubscribeRequest;
import me.ermioni.scclient.services.websocket.socketcluster.requests.UnsubscribeRequest;
import me.ermioni.scclient.services.websocket.socketcluster.responses.PublishResponse;

/**
 * Created by dark on 26.03.16.
 */
public class SocketService implements ISocketService, IResponseCallback {

    private final static String TAG = SocketService.class.getSimpleName();

    private final WebSocketListener socketAdapter;

    private WebSocket socket;

    private ArrayList<SCChannel> channelList = new ArrayList<>();

    private ArrayList<BaseRequest> requestsList = new ArrayList<>();

    private Map<BaseRequest, ICallBack> callBackList = new HashMap<>();

    private Map<String, ISCChannelListener> channelListeners = new HashMap<>();

    private static int cid = 0;
    static String token = null;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        SocketService.token = token;
    }

    public static boolean hasToken() {
        return token != null && token.length() > 0;
    }

    private int _reconnectTimes = -1;
    private final int _maxReconnectTimes;
    private final boolean _needToRestore;

    public SocketService(int maxReconnectTimes, boolean needToRestore) {
        this._maxReconnectTimes = maxReconnectTimes;
        this._needToRestore = needToRestore;

        this.socketAdapter = new WebSocketAdapter(this);
    }

    @Override
    public void Start() {
        Stop();

        socket = (new SocketFactoryService()).GetSocket()
                .addListener(socketAdapter)
                .connectAsynchronously();

        _reconnectTimes = 0;
    }

    @Override
    public void Stop() {
        if (socket != null) {
            socket.disconnect(0, "manual stop");
        }

        socket = null;
        _reconnectTimes = -1;
    }

    @Override
    public boolean SubscribeNewChannel(String name, ISCChannelListener channelListener) {

        final SCChannel channel = new SCChannel(name);
        if (channelList.contains(channel)) {
            return false;
        }

        channelList.add(channel);
        if(channelListener != null) {
            channelListeners.put(name, channelListener);
        }
        return subscribeChannel(channel);
    }

    @Override
    public boolean UnSubscribeNewChannel(String name) {
        if (socket == null) {
            return false;
        }

        int index = channelList.indexOf(new SCChannel(name));
        if (index == -1) {
            return false;
        }
        SCChannel channel = channelList.get(index);

        socket.sendText((new Gson()).toJson(new UnsubscribeRequest(++cid, channel)));
        channel.State = CHANNEL_STATE.CHANNEL_STATE_UNSUBSRIBED;
        channelList.remove(index);
        channelListeners.remove(name);
        return true;
    }

    @Override
    public void SendMessage(String message, ICallBack callBack) {
        BaseRequest request = new PublishRequest(++cid, message);
        requestsList.add(request);
        callBackList.put(request, callBack);

        sendMessage(request);
    }

    @Override
    public void SendMessage(String channelName, String message, ICallBack callBack) {
        BaseRequest request = new PublishToChannelRequest(++cid, channelName, message);
        requestsList.add(request);
        callBackList.put(request, callBack);

        sendMessage(request);
    }

    @Override
    public void Login(String user, String password, ICallBack callBack) {
        BaseRequest request = new LoginRequest(++cid, user, password);
        requestsList.add(request);
        callBackList.put(request, callBack);

        sendMessage(request);
    }

    private boolean subscribeChannel(final SCChannel channel) {
        if (socket == null) {
            return false;
        }
        channel.State = CHANNEL_STATE.CHANNEL_STATE_PENDING;

        final BaseRequest request = new SubscribeRequest(++cid, channel);
        callBackList.put(request, new ICallBack() {
            @Override
            public void onCallBack(boolean success) {
                channel.State = success ? CHANNEL_STATE.CHANNEL_STATE_SUBSCRIBED : CHANNEL_STATE.CHANNEL_STATE_UNSUBSRIBED;
                callBackList.remove(request);
            }
        });

        sendMessage(request);
        return true;
    }

    private void sendMessage(BaseRequest request) {
        socket.sendText((new Gson()).toJson(request));
    }

    /*
     * implements IResponseCallback
     */

    @Override
    public void onConnected(WebSocket ws, String message) {
        _reconnectTimes = 0;
        restoreChannels();
    }

    @Override
    public void onRecieved(WebSocket ws, int rid) {
        BaseRequest request = new BaseRequest(rid);
        int index = requestsList.indexOf(request);
        if(index >= 0) {
            requestsList.remove(index);
            //
            ICallBack callBack = callBackList.get(request);
            if(callBack != null) {
                callBack.onCallBack(true);
                callBackList.remove(request);
            }
        }
    }

    @Override
    public void onError(WebSocket ws, String message) {
        try {
            reconnect();
        }catch (Exception ioe) {
            Log.e(TAG, ioe.toString());

            Collection<ICallBack> callbacks = callBackList.values();
            for (ICallBack callback: callbacks) {
                callback.onCallBack(false);
            }
            callBackList.clear();
        }
    }

    @Override
    public void onPublish(PublishResponse response) {
        ISCChannelListener listener = channelListeners.get(response.data.channel);
        if(listener != null) {
            listener.onRecieveMessageInChannel(response.data.channel, response.data.data);
        }
    }

    private void reconnect() throws Exception {
        if(_reconnectTimes < _maxReconnectTimes) {
            socket = socket.recreate().connect();

            ++_reconnectTimes;
            Log.i(TAG, "reconnectTimes: " + _reconnectTimes);
        } else {
            throw new Exception("Max reconnect times expired: " + _reconnectTimes);
        }
    }

    private void restoreChannels() {
        if(_needToRestore) {
            for (SCChannel channel: channelList) {
                subscribeChannel(channel);
            }
            //
            for (BaseRequest request: requestsList) {
                sendMessage(request);
            }
        } else {
            channelList.clear();
            requestsList.clear();
        }
    }
}
