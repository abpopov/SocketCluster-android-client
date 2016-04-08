package me.ermioni.scclient.services.websocket.socketcluster.requests;

import me.ermioni.scclient.services.websocket.socketcluster.channel.SCChannel;

/**
 * Created by dark on 27.03.16.
 */
public class UnsubscribeRequest extends BaseRequest {
    public UnsubscribeRequest(int cid, SCChannel channel) {
        super(cid);

        event = "#unsubscribe";
        data = channel.Name;
    }
}
