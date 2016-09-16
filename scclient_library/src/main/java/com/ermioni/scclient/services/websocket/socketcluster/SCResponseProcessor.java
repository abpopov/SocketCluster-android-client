package com.ermioni.scclient.services.websocket.socketcluster;

import android.util.Log;
import com.ermioni.scclient.services.websocket.SocketService;
import com.ermioni.scclient.services.websocket.socketcluster.callback.IResponseCallback;
import com.ermioni.scclient.services.websocket.socketcluster.requests.HandshakeRequest;
import com.ermioni.scclient.services.websocket.socketcluster.requests.TokenRequest;
import com.ermioni.scclient.services.websocket.socketcluster.responses.DisconectedResponse;
import com.ermioni.scclient.services.websocket.socketcluster.responses.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neovisionaries.ws.client.WebSocket;

/**
 * Created by dark on 27.03.16.
 */
public class SCResponseProcessor implements ISCResponseProccesor
{

    private static final String RID = "rid";

    private static final String PING_REQUEST = "#1";

    private static final String PONG_RESPONSE = "#2";

    private static final String CONNECTED = "connected";

    private static final String ERROR = "error";

    private static final String EVENT_DISCONNECTED = "#disconnect";

    private static final String SET_AUTH_TOKEN_REQUEST = "#setAuthToken";

    private static final String REMOVE_AUTH_TOKEN_REQUEST = "#removeAuthToken";

    private static final String PUBLISH_RESPONSE = "#publish";


    /* non static */
    private final IResponseCallback responseCallback;

    public SCResponseProcessor(IResponseCallback responseCallback)
    {
        this.responseCallback = responseCallback;
    }

    @Override
    public void processTextResponse(WebSocket ws, String text)
    {
        Log.d("com.ermioni", text);
        if (text.startsWith("{"))
        {
            JsonParser parser = new JsonParser();
            JsonObject o = parser.parse(text).getAsJsonObject();
            if (o.has("rid"))
            {
                handleResponse(o, text);
            }
            else if (o.has("cid"))
            {
                handleRequest(ws, o);
            }
        }
        else
        {
            switch (text)
            {
                case PING_REQUEST:
                {
                    pong(ws);
                    break;
                }
                case CONNECTED:
                {
                    handShake(ws);
                    break;
                }
                case ERROR:
                {
                    responseCallback.onError(0, text);
                    break;
                }
                default:
                {
                    responseCallback.onUnknown(ws, text);
                    break;
                }
            }
        }
    }

    private void handleResponse(JsonObject o, String text)
    {
        int rid = o.get("rid").getAsInt();
        String error = null;
        if (o.has("error"))
        {
            error = o.get("error").getAsString();
        }
        responseCallback.onReceivedSendNotification(rid, error);
    }

    private void handleRequest(WebSocket ws, JsonObject o)
    {
        String error = null;
        boolean noResponse = false;
        int cid = o.get("cid").getAsInt();
        try
        {
            String event = o.get("event").getAsString();
            switch (event)
            {
                case REMOVE_AUTH_TOKEN_REQUEST:
                {
                    setAuthToken(null);
                    noResponse = responseCallback.onConnected();
                    break;
                }
                case SET_AUTH_TOKEN_REQUEST:
                {
                    TokenRequest req = (new Gson()).fromJson(o.get("data"), TokenRequest.class);
                    setAuthToken(req);
                    noResponse = responseCallback.onAuthenticated();
                    break;
                }
                case EVENT_DISCONNECTED:
                {
                    noResponse = responseCallback.onDisconnected((new Gson()).fromJson(o, DisconectedResponse.class));
                    break;
                }
                case PUBLISH_RESPONSE:
                {
                    noResponse = responseCallback.onReceived(cid, o);
                    break;
                }
                default:
                    noResponse = responseCallback.onUnknown(ws, o.toString());
                    error = "No handler";
                    break;
            }
        } catch (Exception e)
        {
            error = e.getMessage();
            noResponse = false;
        }
        if (!noResponse)
        {
            Response r = new Response(cid, error);
            String text = (new Gson()).toJson(r);
            ws.sendText(text);
        }
    }

    private void pong(WebSocket ws)
    {
        ws.sendText(PONG_RESPONSE);
    }

    private void handShake(WebSocket ws)
    {
        HandshakeRequest request = new HandshakeRequest();
        if (SocketService.hasToken())
        {
            request.data = new TokenRequest(SocketService.getToken());
        }

        ws.sendText((new Gson()).toJson(request));
    }

    private void setAuthToken(TokenRequest object)
    {
        if (object != null && object.token.length() > 0)
        {
            SocketService.setToken(object.token);
        }
        else
        {
            SocketService.setToken(null);
        }
    }
}
