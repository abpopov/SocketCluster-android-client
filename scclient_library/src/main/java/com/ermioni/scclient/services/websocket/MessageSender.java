package com.ermioni.scclient.services.websocket;

import com.ermioni.scclient.services.websocket.socketcluster.requests.BaseRequest;

/**
 * Created by DM on 22.05.2016.
 */
public interface MessageSender
{
    void Start();

    void Stop();

    boolean isRunning();

    void sendMessage(BaseRequest request);
}
