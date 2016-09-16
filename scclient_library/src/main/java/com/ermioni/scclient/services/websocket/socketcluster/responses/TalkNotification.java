package com.ermioni.scclient.services.websocket.socketcluster.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DM on 22.06.2016.
 */
public class TalkNotification
{
    public static class TalkCounter
    {
        @SerializedName("talk_id")
        public int id;
    }

    @SerializedName("new_private_talk_message")
    public TalkCounter privateTalks;
    @SerializedName("new_talk_message")
    public TalkCounter spotTalks;
    @SerializedName("new_free_talk_message")
    public TalkCounter freeTalks;
}
