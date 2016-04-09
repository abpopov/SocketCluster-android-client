package me.ermioni.scclient.services.websocket.socketcluster.callback;

import com.neovisionaries.ws.client.WebSocket;

import me.ermioni.scclient.services.websocket.socketcluster.responses.PublishResponse;

/**
 * Created by dark on 08.04.16.
 */
public interface IResponseCallback {

    void onConnected(WebSocket ws, String message);

    void onRecieved(WebSocket ws, int rid);

    void onError(WebSocket ws, String message);

    void onPublish(PublishResponse response);
}
