package me.ermioni.scclient.services.websocket.adapter;

import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;

import java.util.List;
import java.util.Map;

import me.ermioni.scclient.services.websocket.socketcluster.ISocketClusterRequestProcessor;
import me.ermioni.scclient.services.websocket.socketcluster.SocketClusterRequestProcessor;

/**
 * Created by dark on 26.03.16.
 */
public class WebSocketAdapter implements WebSocketListener {

    private final String TAG = this.getClass().getSimpleName();

    private final ISocketClusterRequestProcessor scProcessor;

    public WebSocketAdapter() {
        scProcessor = new SocketClusterRequestProcessor();
    }

    @Override
    public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {
        Log.i(TAG, "onStateChanged(" + newState.toString() + ")");
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        Log.i(TAG, "onConnected()");
        scProcessor.HandShake(websocket);
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {
        Log.i(TAG, "onConnectError(" + cause.toString() + ")");
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        Log.i(TAG, "onDisconnected(ByServer: " + closedByServer + ")");
    }

    @Override
    public void onFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        Log.i(TAG, "onFrame(" + frame.toString() + ")");
    }

    @Override
    public void onContinuationFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        Log.i(TAG, "onContinuationFrame(" + frame.toString() + ")");
    }

    @Override
    public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        Log.i(TAG, "onTextFrame(" + frame.toString() + ")");
    }

    @Override
    public void onBinaryFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        Log.i(TAG, "onBinaryFrame(" + frame.toString() + ")");
    }

    @Override
    public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        Log.i(TAG, "onCloseFrame(" + frame.toString() + ")");
    }

    @Override
    public void onPingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        Log.i(TAG, "onPingFrame: " + frame.toString());
    }

    @Override
    public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        Log.i(TAG, "onPongFrame: " + frame.toString());
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        Log.i(TAG, "onTextMessage: " + text);
        scProcessor.ProcessTextRequest(websocket, text);
    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
        Log.i(TAG, "onBinaryMessage: ");
    }

    @Override
    public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        Log.i(TAG, "onSendingFrame: " + frame.toString());
    }

    @Override
    public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
        Log.i(TAG, "onFrameSent: " + frame.toString());
    }

    @Override
    public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) throws Exception {
        Log.i(TAG, "onFrameUnsent: ");
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
        Log.i(TAG, "onError: ");
    }

    @Override
    public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
        Log.i(TAG, "onFrameError: ");
    }

    @Override
    public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception {
        Log.i(TAG, "onMessageError: ");
    }

    @Override
    public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) throws Exception {
        Log.i(TAG, "onMessageDecompressionError: ");
    }

    @Override
    public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {
        Log.i(TAG, "onTextMessageError: ");
    }

    @Override
    public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
        Log.i(TAG, "onSendError: ");
    }

    @Override
    public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {
        Log.i(TAG, "onUnexpectedError: ");
    }

    @Override
    public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {
        Log.i(TAG, "handleCallbackError: ");
    }

    @Override
    public void onSendingHandshake(WebSocket websocket, String requestLine, List<String[]> headers) throws Exception {
        Log.i(TAG, "onSendingHandshake: ");
    }
}
