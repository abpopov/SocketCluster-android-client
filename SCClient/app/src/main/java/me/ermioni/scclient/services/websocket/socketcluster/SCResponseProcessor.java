package me.ermioni.scclient.services.websocket.socketcluster;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocket;

import me.ermioni.scclient.services.websocket.SocketService;
import me.ermioni.scclient.services.websocket.socketcluster.callback.IResponseCallback;
import me.ermioni.scclient.services.websocket.socketcluster.requests.HandshakeRequest;
import me.ermioni.scclient.services.websocket.socketcluster.requests.TokenRequest;
import me.ermioni.scclient.services.websocket.socketcluster.responses.PublishResponse;

/**
 * Created by dark on 27.03.16.
 */
public class SCResponseProcessor implements ISCResponseProccesor {

    private static final String RID = "rid";

    private static final String PING_REQUEST = "#1";

    private static final String PONG_RESPONSE = "#2";

    private static final String CONNECTED = "connected";

    private static final String ERROR = "error";


    private static final String SET_AUTH_TOKEN_REQUEST = "#setAuthToken";

    private static final String PUBLISH_RESPONSE = "#publish";


    /* non static */
    private final IResponseCallback responseCallback;

    public SCResponseProcessor(IResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    @Override
    public void processTextResponse(WebSocket ws, String text) {
        if(text.startsWith(RID)) {
            int rid = Integer.getInteger(text.substring(4));
            responseCallback.onRecieved(ws, rid);
        }
        else {
            switch (text) {
                case PING_REQUEST: {
                    pong(ws);
                    break;
                }
                case CONNECTED: {
                    handShake(ws);
                    responseCallback.onConnected(ws, text);
                    break;
                }
                case SET_AUTH_TOKEN_REQUEST: {
                    setAuthToken((new Gson()).fromJson(text, TokenRequest.class));
                    break;
                }
                case PUBLISH_RESPONSE: {
                    responseCallback.onPublish((new Gson()).fromJson(text, PublishResponse.class));
                    break;
                }
                case ERROR: {
                    responseCallback.onError(ws, text);
                    break;
                }
            }
        }
    }

    private void pong(WebSocket ws) {
        ws.sendText(PONG_RESPONSE);
    }

    private void handShake(WebSocket ws) {
        HandshakeRequest request = new HandshakeRequest();
        if(SocketService.hasToken()) {
            request.data = new TokenRequest(SocketService.getToken());
        }

        ws.sendText((new Gson()).toJson(request));
    }

    private void setAuthToken(TokenRequest object) {
        if(object != null && object.token.length() > 0) {
            SocketService.setToken(object.token);
        }
    }
}
