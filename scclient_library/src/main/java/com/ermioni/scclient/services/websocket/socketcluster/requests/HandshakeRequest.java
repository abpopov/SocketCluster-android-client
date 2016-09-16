package com.ermioni.scclient.services.websocket.socketcluster.requests;

/**
 * Created by dark on 27.03.16.
 */
public class HandshakeRequest
{

    public String event;

    public Object data;

    public HandshakeRequest()
    {
        event = "#handshake";
        data = "";
    }
}
