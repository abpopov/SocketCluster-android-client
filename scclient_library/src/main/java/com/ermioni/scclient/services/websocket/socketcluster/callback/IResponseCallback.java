package com.ermioni.scclient.services.websocket.socketcluster.callback;

import com.ermioni.scclient.services.websocket.socketcluster.responses.DisconectedResponse;
import com.google.gson.JsonObject;
import com.neovisionaries.ws.client.WebSocket;
import org.json.JSONException;

/**
 * Created by dark on 08.04.16.
 */
public interface IResponseCallback
{
    boolean onConnected();

    boolean onAuthenticated();

    boolean onReceivedSendNotification(int rid, String error);

    boolean onReceived(int id, JsonObject obj) throws JSONException;

    boolean onError(int id, String message);

    boolean onDisconnected(DisconectedResponse response);

    boolean onUnknown(WebSocket ws, String message);
}
