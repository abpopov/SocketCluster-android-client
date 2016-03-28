package me.ermioni.scclient.services.websocket.socketcluster.requests;

/**
 * Created by dark on 27.03.16.
 */
public class HandshakeRequest extends SocketClusterRequest {

    public HandshakeRequest() {
        event = "#handshake";
        data = "";
    }
}
