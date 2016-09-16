package com.ermioni.scclient.services.websocket;

import com.ermioni.scclient.services.ICallBack;
import com.ermioni.scclient.services.ISCChannelListener;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by DM on 25.05.2016.
 */
public class SCChannel implements IConnectionListener
{
    private class SendPair
    {
        public final Message message;
        public final ICallBack callBack;

        private SendPair(Message message, ICallBack callBack)
        {
            this.message = message;
            this.callBack = callBack;
        }
    }

    private final ISocketService iss;
    private final String channel;
    private volatile boolean connected = false;
    private final AtomicBoolean msgSent = new AtomicBoolean(false);
    private final LinkedList<SendPair> queue = new LinkedList<>();
    private final ICallBack callback = new ICallBack()
    {
        @Override
        public void onCallBack(boolean success)
        {
            onSubscribedOrMessageSent();
        }
    };
    private final ISCChannelListener listener;

    public SCChannel(ISocketService iss, String channel, ISCChannelListener listener)
    {
        this.iss = iss;
        this.channel = channel;
        this.listener = listener;
        iss.addListener(this);
    }

    public void dispose()
    {
        iss.removeListener(this);
        iss.unSubscribeNewChannel(channel, listener);
    }

    @Override
    public synchronized void onConnected()
    {
        iss.subscribeNewChannel(channel, listener, callback);
        connected = true;
        msgSent.set(!queue.isEmpty());
        onSubscribedOrMessageSent();
    }

    @Override
    public synchronized void onDisconnected()
    {
        iss.unSubscribeNewChannel(channel, listener);
        msgSent.set(false);
        connected = false;
    }

    private synchronized void onSubscribedOrMessageSent()
    {
        if (!connected)
        {
            msgSent.set(false);
            return;
        }
        if (msgSent.get())
        {
            SendPair pair = queue.poll();
            if (pair != null)
            {
                sendMessageInner(pair.message, pair.callBack);
            }
            else
            {
                msgSent.compareAndSet(true, false);
            }
        }
    }

    private void sendMessageInner(Message msg, final ICallBack callBack)
    {
        iss.sendMessage(channel, msg, new ICallBack()
        {
            @Override
            public void onCallBack(boolean success)
            {
                if (callBack != null)
                {
                    callBack.onCallBack(success);
                }
                onSubscribedOrMessageSent();
            }
        });
    }

    public synchronized void sendMessage(Message message, ICallBack callBack)
    {
        if (connected && queue.isEmpty() && msgSent.compareAndSet(false, true))
        {
            sendMessageInner(message, callBack);
        }
        else
        {
            queue.offer(new SendPair(message, callBack));
        }
    }
}
