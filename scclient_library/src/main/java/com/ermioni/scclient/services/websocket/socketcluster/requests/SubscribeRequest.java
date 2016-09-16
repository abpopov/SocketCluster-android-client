package com.ermioni.scclient.services.websocket.socketcluster.requests;

/**
 * Created by dark on 27.03.16.
 */
public class SubscribeRequest extends BaseRequest<SubscribeRequest.RequestData>
{
    public static class RequestData
    {
        public final String channel;

        public RequestData(String channel)
        {
            this.channel = channel;
        }
    }

    public SubscribeRequest(int cid, String channelName)
    {
        super(cid);

        event = "#subscribe";
        data = new RequestData(channelName);
    }
}
