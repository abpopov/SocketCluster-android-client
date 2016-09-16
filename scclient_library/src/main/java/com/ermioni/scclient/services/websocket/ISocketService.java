package com.ermioni.scclient.services.websocket;

import com.ermioni.scclient.services.ICallBack;
import com.ermioni.scclient.services.ISCChannelListener;
import com.ermioni.scclient.services.IService;

/**
 * Created by dark on 26.03.16.
 */
public interface ISocketService extends IService
{
    void start(String userToken, String apiToken);

    void stop();

    void subscribeNewChannel(String name, ISCChannelListener channelListener, ICallBack callBack);

    void unSubscribeNewChannel(String name, ISCChannelListener channelListener);

    void sendMessage(String channelName, Message message, ICallBack callBack);

    void addListener(IConnectionListener listener);

    void removeListener(IConnectionListener listener);
}




