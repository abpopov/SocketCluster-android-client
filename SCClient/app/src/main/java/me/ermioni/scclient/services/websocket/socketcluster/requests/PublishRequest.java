package me.ermioni.scclient.services.websocket.socketcluster.requests;

/**
 * Created by dark on 08.04.16.
 */
public class PublishRequest extends BaseRequest {

    public PublishRequest(int cid, String message) {
        super(cid);
        event = "#publish";
        data = message;
    }


}
