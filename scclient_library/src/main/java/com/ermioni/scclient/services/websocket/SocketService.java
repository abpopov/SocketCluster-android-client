package com.ermioni.scclient.services.websocket;

import android.provider.SyncStateContract;
import android.util.Log;

import com.ermioni.scclient.services.ICallBack;
import com.ermioni.scclient.services.ISCChannelListener;
import com.ermioni.scclient.services.websocket.adapter.WebSocketAdapter;
import com.ermioni.scclient.services.websocket.socketcluster.callback.IResponseCallback;
import com.ermioni.scclient.services.websocket.socketcluster.requests.*;
import com.ermioni.scclient.services.websocket.socketcluster.responses.DisconectedResponse;
import com.ermioni.scclient.services.websocket.socketcluster.responses.NotificationCounters;
import com.ermioni.scclient.services.websocket.socketcluster.responses.PublishToChannelResponse;
import com.ermioni.scclient.services.websocket.socketcluster.responses.TalkNotification;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dark on 26.03.16.
 */
public class SocketService implements ISocketService, IResponseCallback
{

    private final static String TAG = "com.ermioni";

    private final Gson gson = new Gson();

    private final WebSocketListener socketAdapter;

    private WebSocket socket;

    private final ConcurrentHashMap<String, ArrayList<ISCChannelListener>> channelList = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<IConnectionListener, Object> connectionListeners = new ConcurrentHashMap<>();

    private final Map<BaseRequest, ICallBack> callBackList = new ConcurrentHashMap<>();

    private static AtomicInteger cid = new AtomicInteger(0);
    private volatile boolean connected = false;
    static String user_token = null;
    static String api_token = null;
    static String token = null;

    public static String getToken()
    {
        return token;
    }

    public static void setToken(String token)
    {
        SocketService.token = token;
    }

    public static boolean hasToken()
    {
        return token != null && token.length() > 0;
    }

    private int _reconnectTimes = -1;
    private final int _maxReconnectTimes;
    private final boolean _needToRestore;
    private final SocketFactoryService socketFactoryService;

    public SocketService(int maxReconnectTimes, boolean needToRestore, SocketFactoryService socketFactoryService)
    {
        this._maxReconnectTimes = maxReconnectTimes;
        this._needToRestore = needToRestore;
        this.socketFactoryService = socketFactoryService;

        this.socketAdapter = new WebSocketAdapter(this);
    }

    @Override
    public void start(String userToken, String apiToken)
    {
        stop();

        user_token = userToken;
        api_token = apiToken;
        socket = socketFactoryService.GetSocket().addListener(socketAdapter).connectAsynchronously();

        _reconnectTimes = 0;
    }

    @Override
    public void stop()
    {
        if (socket != null)
        {
            socket.disconnect(0, "manual stop");
        }

        socket = null;
        _reconnectTimes = -1;
    }

    @Override
    public void subscribeNewChannel(String name, ISCChannelListener channelListener, ICallBack callBack)
    {
        ArrayList<ISCChannelListener> newList = new ArrayList<>();
        ArrayList<ISCChannelListener> list = channelList.putIfAbsent(name, newList);
        if (list == null)
        {
            list = newList;
        }
        synchronized (list)
        {
            if (!list.contains(channelListener))
            {
                list.add(channelListener);
            }
            else
            {
                callBack.onCallBack(false);
                return;
            }
        }
        subscribeChannel(name, callBack);
    }

    @Override
    public void unSubscribeNewChannel(String name, ISCChannelListener channelListener)
    {
        ArrayList<ISCChannelListener> newList = new ArrayList<>();
        ArrayList<ISCChannelListener> list = channelList.putIfAbsent(name, newList);
        if (list == null)
        {
            list = newList;
        }
        synchronized (list)
        {
            list.remove(channelListener);
            if (list.isEmpty())
            {
                unsubscribeChannel(name);
            }
        }
    }

    @Override
    public void sendMessage(String channelName, Message message, ICallBack callBack)
    {
        BaseRequest request = new PublishToChannelRequest(cid.incrementAndGet(), channelName, message);
        sendMessage(request, callBack);
    }

    private void loginByAuthToken(String user_token, String api_token, ICallBack callBack)
    {
        BaseRequest request = new AuthRequest(cid.incrementAndGet(), user_token, api_token);
        sendMessage(request, callBack);
    }

    @Override
    public void addListener(IConnectionListener listener)
    {
        if (connectionListeners.putIfAbsent(listener, this) == null)
        {
            if (connected)
            {
                listener.onConnected();
            }
            else
            {
                listener.onDisconnected();
            }
        }
    }

    @Override
    public void removeListener(IConnectionListener listener)
    {
        if (connectionListeners.remove(listener) != null)
        {
            listener.onDisconnected();
        }
    }

    private boolean subscribeChannel(final String channel, ICallBack callBack)
    {
        if (socket == null)
        {
            if (callBack != null)
            {
                callBack.onCallBack(false);
            }
            return false;
        }

        final BaseRequest request = new SubscribeRequest(cid.incrementAndGet(), channel);

        sendMessage(request, callBack);
        return true;
    }

    private boolean unsubscribeChannel(final String channel)
    {
        if (socket == null)
        {
            return false;
        }

        final BaseRequest request = new UnsubscribeRequest(cid.incrementAndGet(), channel);

        sendMessage(request, null);
        return true;
    }

    private void sendMessage(BaseRequest request, ICallBack callBack)
    {
        checkConnected();
        if (socket == null)
        {
            if (callBack != null)
            {
                callBack.onCallBack(false);
            }
            return;
        }
        if (callBack != null)
        {
            callBackList.put(request, callBack);
        }
        String json = gson.toJson(request);
        Log.d(TAG, json);
        socket.sendText(json);
    }

    /*
     * implements IResponseCallback
     */

    @Override
    public boolean onConnected()
    {
        _reconnectTimes = 0;
        loginByAuthToken(user_token, api_token, null);
        return false;
    }

    @Override
    public boolean onAuthenticated()
    {
        notifyConnected();
        return false;
    }

    @Override
    public boolean onReceivedSendNotification(int rid, String error)
    {
        if (error != null)
        {
            Log.e(TAG, rid + ":" + error);
        }
        notifyCallback(rid, error == null);
        return false;
    }

    @Override
    public boolean onReceived(int cid, JsonObject obj) throws JSONException
    {
        PublishToChannelResponse response = gson.fromJson(obj, PublishToChannelResponse.class);
        ArrayList<ISCChannelListener> listeners = channelList.get(response.data.channel);
        if (listeners != null)
        {
            if (response.data.data.message != null)
            {
                for (ISCChannelListener listener : listeners)
                {
                    listener.onReceiveMessage(response.data.data.message);
                }
            }
            UserNotification userNotification = UserNotification.fromJsonObject(response.data.data.userNotification);
            if (userNotification != null)
            {
                for (ISCChannelListener listener : listeners)
                {
                    listener.onReceiveUser(userNotification);
                }
                return true;
            }
            JsonObject notification = response.data.data.notification;
            if (notification != null)
            {
                for (ISCChannelListener listener : listeners)
                {
                    listener.onReceiveNotification(notification);
                }
                return true;
            }
            if (response.data.data.counters != null)
            {
                NotificationCounters counters = response.data.data.counters;
                for (ISCChannelListener listener : listeners)
                {
                    listener.onReceiveCounters(counters);
                }
                return true;
            }
            if (response.data.data.talkNotification != null)
            {
                TalkNotification talkNotification = response.data.data.talkNotification;
                for (ISCChannelListener listener : listeners)
                {
                    listener.onReceiveTalkNotification(talkNotification);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onError(int rid, String message)
    {
        Log.e(TAG, message);
        notifyCallback(rid, false);
        return false;
    }

    @Override
    public boolean onDisconnected(DisconectedResponse response)
    {
        clearCallbacks();
        notifyDisconnected();
        try
        {
            socket.disconnect();
            if (_needToRestore)
            {
                reconnect(false);
            }
        } catch (Exception ioe)
        {
            Log.e(TAG, ioe.toString());
        }
        return false;
    }

    @Override
    public boolean onUnknown(WebSocket ws, String message)
    {
        onError(0, message);
        return false;
    }

    private void checkConnected()
    {
        if (socket != null && socket.getState() != WebSocketState.CLOSED)
        {
            return;
        }
        try
        {
            reconnect(true);
        } catch (Exception e)
        {
            Log.e(TAG, e.toString());
        }
    }

    private void reconnect(boolean force) throws Exception
    {
        if (_reconnectTimes < _maxReconnectTimes)
        {
            if (!force)
            {
                long time = (long) Math.random() % 3000;
                Thread.sleep(time);
            }
            socket = socket.recreate().connect();

            ++_reconnectTimes;
            Log.i(TAG, "reconnectTimes: " + _reconnectTimes);
        }
        else
        {
            throw new Exception("Max reconnect times expired: " + _reconnectTimes);
        }
    }

    private void notifyCallback(int rid, boolean status)
    {
        BaseRequest req = new BaseRequest(rid);
        ICallBack callBack = callBackList.get(req);
        if (callBack != null)
        {
            callBack.onCallBack(status);
            callBackList.remove(req);
        }
    }

    private void clearCallbacks()
    {
        Collection<ICallBack> callbacks = callBackList.values();
        for (ICallBack callback : callbacks)
        {
            callback.onCallBack(false);
        }
        callBackList.clear();
    }

    private void notifyConnected()
    {
        connected = true;
        for (IConnectionListener listener : connectionListeners.keySet())
        {
            listener.onConnected();
        }
    }

    private void notifyDisconnected()
    {
        connected = false;
        for (IConnectionListener listener : connectionListeners.keySet())
        {
            listener.onDisconnected();
        }
    }
}
