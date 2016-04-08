package me.ermioni.scclient.services.websocket.socketcluster.requests;

/**
 * Created by dark on 27.03.16.
 */
public class BaseRequest {
    public int cid;

    public String event;

    public Object data;

    public BaseRequest(int cid) {
        this.cid = cid;
    }
}
