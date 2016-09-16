package com.ermioni.scclient.services.websocket.socketcluster.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DM on 21.06.2016.
 */
public class NotificationCounters{
    @SerializedName("n_c")
    public Integer notificationCounter;

    @SerializedName("s_c")
    public Integer streamCounter;

    @SerializedName("t_c")
    public Integer talksCounter;

    @SerializedName("f_t_c")
    public Integer freeTalksCounter;

    @SerializedName("p_t_c")
    public Integer privateTalksCounter;

    @SerializedName("a_c")
    public Integer allCounters;
}

