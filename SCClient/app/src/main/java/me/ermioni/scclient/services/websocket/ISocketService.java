package me.ermioni.scclient.services.websocket;

import me.ermioni.scclient.services.ICallBack;
import me.ermioni.scclient.services.ISCChannelListener;
import me.ermioni.scclient.services.IService;

/**
 * Created by dark on 26.03.16.
 */
public interface ISocketService extends IService {
    void Start();

    void Stop();

    boolean SubscribeNewChannel(String name, ISCChannelListener channelListener);

    boolean UnSubscribeNewChannel(String name);

    void SendMessage(String mesage, ICallBack callBack);

    void SendMessage(String channelName, String mesage, ICallBack callBack);

    void Login(String user, String password, ICallBack callBack);
}
