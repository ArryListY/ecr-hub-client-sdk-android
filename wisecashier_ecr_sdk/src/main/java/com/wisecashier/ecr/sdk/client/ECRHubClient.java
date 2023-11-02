package com.wisecashier.ecr.sdk.client;

import static com.wisecashier.ecr.sdk.util.Constants.HEART_BEAT_TOPIC;
import static com.wisecashier.ecr.sdk.util.Constants.INIT_TOPIC;

import android.net.nsd.NsdManager;
import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import com.wiseasy.ecr.hub.data.ECRHubRequestProto;
import com.wiseasy.ecr.hub.data.ECRHubResponseProto;
import com.wisecashier.ecr.sdk.client.payment.Payment;
import com.wisecashier.ecr.sdk.listener.ECRHubConnectListener;
import com.wisecashier.ecr.sdk.listener.ECRHubResponseCallBack;
import com.wisecashier.ecr.sdk.util.Base64Utils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * @author pupan
 */
public class ECRHubClient {

    private static final String TAG = "ECRHubClient";
    //server config
    private final ECRHubConfig config;
    // listener
    private final ECRHubConnectListener connectListener;
    // server ip
    private final String ipAddress;
    private WebSocketClient webSocketClient;

    public Payment payment;

    private ECRHubResponseCallBack initCallback;


    public ECRHubClient(String ip, ECRHubConfig config, ECRHubConnectListener listener) {
        this.config = config;
        this.ipAddress = ip;
        this.connectListener = listener;
        initSocketClient();
    }

    public void init(ECRHubResponseCallBack callBack) {
        initCallback = callBack;
        if (isConnected()) {
            ECRHubRequestProto.RequestBizData bizData = ECRHubRequestProto.RequestBizData.newBuilder().setConfirmOnTerminal(true).setIsAutoSettlement(true).build();
            ECRHubRequestProto.ECRHubRequest data = ECRHubRequestProto.ECRHubRequest.newBuilder().setTopic(INIT_TOPIC).setBizData(bizData).build();
            webSocketClient.send(data.toByteArray());
        }
    }

    private void sendHeartBeat() {
        if (isConnected()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("topic", HEART_BEAT_TOPIC);
            webSocketClient.send(jsonObject.toString());
        }
    }

    /**
     * 初始化websocket连接
     */
    private void initSocketClient() {
        URI uri = URI.create(ipAddress);
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                try {
                    ECRHubResponseProto.ECRHubResponse response = ECRHubResponseProto.ECRHubResponse.parseFrom(message.getBytes());
                    if (response.getTopic().equals(INIT_TOPIC)) {
                        ECRHubResponseProto.ResponseBizData initData = response.getBizData();
                        initCallback.onSuccess(JsonFormat.printToString(initData));
                    } else if (!response.getTopic().equals(HEART_BEAT_TOPIC)) {
                        if (null != payment && null != payment.getResponseCallBack()) {
                            payment.getResponseCallBack().onSuccess(JsonFormat.printToString(response));
                        }
                    }
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
            }


            @Override
            public void onMessage(ByteBuffer bytes) {
                try {
                    ECRHubResponseProto.ECRHubResponse response = ECRHubResponseProto.ECRHubResponse.parseFrom(bytes);
                    if (response.getTopic().equals(INIT_TOPIC)) {
                        ECRHubResponseProto.ResponseBizData initData = response.getBizData();
                        initCallback.onSuccess(JsonFormat.printToString(initData));
                    } else if (!response.getTopic().equals(HEART_BEAT_TOPIC)) {
                        if (null != payment && null != payment.getResponseCallBack()) {
                            payment.getResponseCallBack().onSuccess(JsonFormat.printToString(response));
                        }
                    }
                } catch (InvalidProtocolBufferException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.e(TAG, "websocket connect success");
                if (null != connectListener) {
                    connectListener.onConnect();
                }
                //   heartBeatRunnable.run();
            }

            @Override
            public void onError(Exception ex) {
                Log.e(TAG, "websocket connect error：" + ex);
                if (null != connectListener) {
                    connectListener.onError(ex.getLocalizedMessage(), ex.getMessage());
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.e(TAG, "websocket disconect：·code:" + code + "·reason:" + reason + "·remote:" + remote);
                if (null != connectListener) {
                    connectListener.onDisconnect();
                }
            }
        };
        if (config.getConnectionTimeout() != 0) {
            webSocketClient.setConnectionLostTimeout(config.getConnectionTimeout());
        }
        payment = new Payment((webSocketClient));
    }

    public boolean isConnected() {
        return null != webSocketClient && webSocketClient.isOpen();
    }


    /**
     * connect
     */
    public void connect() {
        if (webSocketClient.isOpen()) {
            return;
        }
        webSocketClient.connect();
    }

    public void disConnect() {
        if (!webSocketClient.isOpen()) {
            return;
        }
        webSocketClient.close();
    }

    /**
     * 开启重连
     */
    private void reconnectWs() {
        Log.e(TAG, "reconnectWs");
        initSocketClient();
    }


    private static final long HEART_BEAT_RATE = 10 * 1000;//每隔10秒进行一次对长连接的心跳检测
    private final Handler mHandler = new Handler();
    //心跳检测
    private final Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(heartBeatRunnable);
            if (webSocketClient != null) {
                if (webSocketClient.isClosed()) {
                    Log.e(TAG, "close status");
                    reconnectWs();
                } else {
                    if (isConnected()) {
                        sendHeartBeat();
                    } else {
                        reconnectWs();
                    }
                }
            } else {
                initSocketClient();
            }
            mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
        }
    };
}
