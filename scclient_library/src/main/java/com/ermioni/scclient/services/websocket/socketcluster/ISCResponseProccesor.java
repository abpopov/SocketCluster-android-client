package com.ermioni.scclient.services.websocket.socketcluster;

import com.neovisionaries.ws.client.WebSocket;
import com.ermioni.scclient.services.IService;

/**
 * Created by dark on 27.03.16.
 */
public interface ISCResponseProccesor extends IService
{
    void processTextResponse(WebSocket ws, String text);
}
