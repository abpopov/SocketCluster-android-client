package me.ermioni.scclient.services.websocket.factory;

import com.neovisionaries.ws.client.WebSocket;

import me.ermioni.scclient.services.IService;

/**
 * Created by dark on 26.03.16.
 */
public interface ISocketFactoryService extends IService {
    WebSocket GetSocket();
}
