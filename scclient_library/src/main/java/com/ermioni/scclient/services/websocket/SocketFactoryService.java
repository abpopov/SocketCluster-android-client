package com.ermioni.scclient.services.websocket;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;

/**
 * Created by dark on 26.03.16.
 */
public class SocketFactoryService
{

    private static final int TIME_OUT = 5000;

    // Create a WebSocketFactory instance.
    private static WebSocketFactory factory = new WebSocketFactory();

    private String socketUri;

    public SocketFactoryService(String socketUri)
    {
        this.socketUri = socketUri;
    }

    public WebSocket GetSocket()
    {
        try
        {
            return factory.createSocket(socketUri, TIME_OUT);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
