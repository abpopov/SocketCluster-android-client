package me.ermioni.scclient.services.websocket;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketListener;

import java.util.ArrayList;

import me.ermioni.scclient.services.websocket.factory.ISocketFactoryService;
import me.ermioni.scclient.services.websocket.factory.SocketFactoryService;
import me.ermioni.scclient.services.websocket.socketcluster.channel.CHANNEL_STATE;
import me.ermioni.scclient.services.websocket.socketcluster.channel.SCChannel;
import me.ermioni.scclient.services.websocket.socketcluster.requests.SubscribeRequest;
import me.ermioni.scclient.services.websocket.socketcluster.requests.UnsubscribeRequest;

/**
 * Created by dark on 26.03.16.
 */
public class SocketService implements ISocketService {

    private final WebSocketListener socketAdapter;

    private final ISocketFactoryService socketFactoryService;

    private WebSocket socket;

    private ArrayList<SCChannel> channelList = new ArrayList<SCChannel>();

    public SocketService(WebSocketListener socketAdapter) {
        this.socketAdapter = socketAdapter;
        this.socketFactoryService = new SocketFactoryService();
    }

    @Override
    public void Start() {
        Stop();

        socket = socketFactoryService.GetSocket()
                .addListener(socketAdapter)
                .connectAsynchronously();
    }

    @Override
    public void Stop() {
        if (socket != null) {
            socket.disconnect(0, "manual stop");
        }

        socket = null;
    }

    @Override
    public boolean SubscribeNewChannel(String name) {
        if (socket == null) {
            return false;
        }

        SCChannel channel = new SCChannel(name);
        if (channelList.contains(channel)) {
            return false;
        }
        channelList.add(channel);

        socket.sendText((new Gson()).toJson(new SubscribeRequest(channel)));
        channel.State = CHANNEL_STATE.CHANNEL_STATE_PENDING;
        return true;
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

        socket.sendText((new Gson()).toJson(new UnsubscribeRequest(channel)));
        channel.State = CHANNEL_STATE.CHANNEL_STATE_UNSUBSRIBED;
        channelList.remove(index);
        return true;
    }
}
