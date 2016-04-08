package me.ermioni.scclient.services.websocket.socketcluster.requests;

import me.ermioni.scclient.services.websocket.socketcluster.channel.SCChannel;

/**
 * Created by dark on 27.03.16.
 */
public class SubscribeRequest extends BaseRequest {
    public SubscribeRequest(int cid, SCChannel channel) {
        super(cid);

        event = "#subscribe";
        data = channel.Name;
    }
}
