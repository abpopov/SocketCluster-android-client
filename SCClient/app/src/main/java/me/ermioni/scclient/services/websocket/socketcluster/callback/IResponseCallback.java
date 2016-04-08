package me.ermioni.scclient.services.websocket.socketcluster.callback;

import com.neovisionaries.ws.client.WebSocket;

/**
 * Created by dark on 08.04.16.
 */
public interface IResponseCallback {

    void onConnectedCallback(WebSocket ws, String message);

    void onPublishCallback(WebSocket ws, String message);

    void onErrorCallback(WebSocket ws, String message);
}
