package me.ermioni.scclient.services.websocket;

import me.ermioni.scclient.services.ICallBack;
import me.ermioni.scclient.services.IService;
import me.ermioni.scclient.services.dto.MessageDto;

/**
 * Created by dark on 26.03.16.
 */
public interface ISocketService extends IService {
    void Start();

    void Stop();

    boolean SubscribeNewChannel(String name);

    boolean UnSubscribeNewChannel(String name);

    void SendMessage(String channelName, String mesage, ICallBack callBack);
}
