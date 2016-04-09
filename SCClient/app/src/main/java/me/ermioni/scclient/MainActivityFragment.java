package me.ermioni.scclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.ermioni.scclient.services.ISCChannelListener;
import me.ermioni.scclient.services.websocket.ISocketService;
import me.ermioni.scclient.services.websocket.SocketService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements ISCChannelListener {

    private TextView twInfo;

    private final ISocketService wss;

    public MainActivityFragment() {
        wss = new SocketService(5, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        view.findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wss.Start();
            }
        });

        view.findViewById(R.id.btn_disconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wss.Stop();
            }
        });

        view.findViewById(R.id.btn_subscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribe();
            }
        });

        view.findViewById(R.id.btn_unsubscribe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wss.UnSubscribeNewChannel("test");
            }
        });

        twInfo = (TextView)view.findViewById(R.id.tw_info);

        return view;
    }

    private void subscribe() {
        wss.SubscribeNewChannel("test", this);
    }

    @Override
    public void onRecieveMessageInChannel(String channel, String message) {
        twInfo.setText(message);
    }
}
