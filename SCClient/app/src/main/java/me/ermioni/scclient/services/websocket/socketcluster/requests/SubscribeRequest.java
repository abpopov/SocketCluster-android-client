package me.ermioni.scclient.services.websocket.socketcluster.requests;

import me.ermioni.scclient.services.websocket.socketcluster.channel.SCChannel;

/**
 * Created by dark on 27.03.16.
 */
public class SubscribeRequest extends SocketClusterRequest {
    public SubscribeRequest(SCChannel channel) {
        event = "#subscribe";
        data = channel.Name;
    }
}
