package com.ermioni.scclient.services.websocket.socketcluster.requests;

import com.ermioni.scclient.services.websocket.Message;
import com.ermioni.scclient.services.websocket.UserNotification;

/**
 * Created by dark on 08.04.16.
 */
public class PublishToChannelRequest extends BaseRequest<PublishToChannelRequest.ChannelData>
{
    public static class ChannelData
    {
        public final String channel;
        public final MessageWrapper data;

        public ChannelData(String channel, Message data, UserNotification notification)
        {
            this.channel = channel;
            this.data = new MessageWrapper(data, notification);
        }
    }

    public static class MessageWrapper
    {
        public final Message message;
        public final UserNotification notification;

        public MessageWrapper(Message message, UserNotification notification)
        {
            this.message = message;
            this.notification = notification;
        }
    }

    public PublishToChannelRequest(int cid, String channelName, Message message)
    {
        super(cid);
        event = "#publish";
        data = new ChannelData(channelName, message, null);
    }
}

