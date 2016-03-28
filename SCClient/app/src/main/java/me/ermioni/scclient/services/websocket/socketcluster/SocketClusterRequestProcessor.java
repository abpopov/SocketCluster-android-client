package me.ermioni.scclient.services.websocket.socketcluster;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;

import me.ermioni.scclient.services.websocket.socketcluster.requests.HandshakeRequest;

/**
 * Created by dark on 27.03.16.
 */
public class SocketClusterRequestProcessor implements ISocketClusterRequestProcessor {

    private static final String PING_REQUEST = "#1";

    private static final String PONG_RESPONSE = "#2";

    @Override
    public void ProcessTextRequest(WebSocket ws, String text) {
        switch (text) {
            case PING_REQUEST: {
                Pong(ws);
                break;
            }
        }
    }

    private void Pong(WebSocket ws) {
        ws.sendText(PONG_RESPONSE);
    }

    @Override
    public void HandShake(WebSocket ws) {
        ws.sendText((new Gson()).toJson(new HandshakeRequest()));
    }
}
