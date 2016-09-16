package com.ermioni.scclient.services.websocket.socketcluster.requests;

import com.ermioni.scclient.services.websocket.Message;

/**
 * Created by dark on 08.04.16.
 */
public class PublishRequest extends BaseRequest
{

    public PublishRequest(int cid, Message message)
    {
        super(cid);
        event = "#publish";
        data = message;
    }
}
