package me.ermioni.scclient.services.websocket.socketcluster.channel;

/**
 * Created by dark on 27.03.16.
 */
public class SCChannel {
    public String Name;
    public CHANNEL_STATE State;

    public SCChannel(String name) {
        Name = name;
        State = CHANNEL_STATE.CHANNEL_STATE_UNSUBSRIBED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SCChannel scChannel = (SCChannel) o;

        return Name.equals(scChannel.Name);

    }

    @Override
    public int hashCode() {
        return Name.hashCode();
    }
}
