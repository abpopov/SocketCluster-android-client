package me.ermioni.scclient.services;

/**
 * Created by dark on 09.04.16.
 */
public interface ISCChannelListener {
    void onRecieveMessageInChannel(String channel, String message);
}
