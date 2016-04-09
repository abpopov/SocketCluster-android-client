package me.ermioni.scclient.services.websocket.socketcluster.requests;

/**
 * Created by dark on 08.04.16.
 */
public class PublishToChannelRequest extends BaseRequest {

    public String channel;

    public PublishToChannelRequest(int cid, String channelName, String message) {
        super(cid);
        event = "#publish";
        channel = channelName;
        data = message;
    }


}
