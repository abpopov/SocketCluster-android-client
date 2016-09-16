package com.ermioni.scclient.services.websocket.socketcluster.responses;

import com.ermioni.scclient.services.websocket.Message;
import com.ermioni.scclient.services.websocket.socketcluster.requests.BaseRequest;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DM on 05.06.2016.
 */
public class PublishToChannelResponse extends BaseRequest<PublishToChannelResponse.ChannelData>
{
    public static class ChannelData
    {
        public final String channel;
        public final MessageWrapper data;

        public ChannelData(String channel, Message data, JsonObject userNotification, JsonObject notification, NotificationCounters counters, TalkNotification talkNotification)
        {
            this.channel = channel;
            this.data = new MessageWrapper(data, userNotification, notification, counters, talkNotification);
        }
    }

    public static class MessageWrapper
    {
        public final Message message;
        @SerializedName("notification")
        public final JsonObject userNotification;
        @SerializedName("nt")
        public final JsonObject notification;
        @SerializedName("cu")
        public final NotificationCounters counters;
        @SerializedName("tcu")
        public final TalkNotification talkNotification;

        public MessageWrapper(Message message, JsonObject userNotification, JsonObject notification, NotificationCounters counters, TalkNotification talkNotification)
        {
            this.message = message;
            this.userNotification = userNotification;
            this.notification = notification;
            this.counters = counters;
            this.talkNotification = talkNotification;
        }
    }

    public PublishToChannelResponse(int cid)
    {
        super(cid);
    }
}
