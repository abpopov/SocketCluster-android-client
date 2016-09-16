package com.ermioni.scclient.services.websocket;

/**
 * Created by DM on 25.05.2016.
 */
public interface IConnectionListener
{
    void onConnected();

    void onDisconnected();
}
