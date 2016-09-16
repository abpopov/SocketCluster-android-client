package com.ermioni.scclient.services;

import com.ermioni.scclient.services.websocket.Message;
import com.ermioni.scclient.services.websocket.socketcluster.responses.NotificationCounters;
import com.ermioni.scclient.services.websocket.UserNotification;
import com.ermioni.scclient.services.websocket.socketcluster.responses.TalkNotification;
import com.google.gson.JsonObject;

/**
 * Created by dark on 09.04.16.
 */
public interface ISCChannelListener
{
    void onReceiveMessage(Message message);

    void onReceiveUser(UserNotification notification);

    void onReceiveNotification(JsonObject notification);

    void onReceiveCounters(NotificationCounters notification);

    void onReceiveTalkNotification(TalkNotification talkNotification);
}
