package me.ermioni.scclient.services.websocket.socketcluster;

import com.neovisionaries.ws.client.WebSocket;

import me.ermioni.scclient.services.IService;

/**
 * Created by dark on 27.03.16.
 */
public interface ISocketClusterRequestProcessor extends IService {
    void ProcessTextRequest(WebSocket ws, String text);
    void HandShake(WebSocket ws);
}
