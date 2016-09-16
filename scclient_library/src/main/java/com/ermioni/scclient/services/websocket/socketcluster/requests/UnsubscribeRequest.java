package com.ermioni.scclient.services.websocket.socketcluster.requests;

/**
 * Created by dark on 27.03.16.
 */
public class UnsubscribeRequest extends SubscribeRequest
{
    public UnsubscribeRequest(int cid, String channelName)
    {
        super(cid, channelName);

        event = "#unsubscribe";
    }
}
