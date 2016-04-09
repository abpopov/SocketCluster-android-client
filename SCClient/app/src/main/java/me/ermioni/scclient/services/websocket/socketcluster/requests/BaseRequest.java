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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseRequest that = (BaseRequest) o;

        return cid == that.cid;

    }

    @Override
    public int hashCode() {
        return cid;
    }
}
