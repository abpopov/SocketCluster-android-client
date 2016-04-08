package me.ermioni.scclient.services.websocket.socketcluster;

import com.neovisionaries.ws.client.WebSocket;

import me.ermioni.scclient.services.IService;

/**
 * Created by dark on 27.03.16.
 */
public interface ISCResponseProccesor extends IService {
    void processTextResponse(WebSocket ws, String text);
}
