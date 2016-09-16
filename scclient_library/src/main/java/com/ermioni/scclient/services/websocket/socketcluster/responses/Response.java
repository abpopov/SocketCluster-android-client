package com.ermioni.scclient.services.websocket.socketcluster.responses;

/**
 * Created by DM on 26.05.2016.
 */
public class Response{
    public final int rid;
    public final String error;

    public Response(int rid, String error)
    {
        this.rid = rid;
        this.error = error;
    }
}
