package com.wisecashier.ecr.sdk.client.payment;

import static com.wisecashier.ecr.sdk.util.Constants.CLOSE_TOPIC;
import static com.wisecashier.ecr.sdk.util.Constants.INIT_TOPIC;
import static com.wisecashier.ecr.sdk.util.Constants.PAYMENT_TOPIC;
import static com.wisecashier.ecr.sdk.util.Constants.QUERY_TOPIC;

import com.wiseasy.ecr.hub.data.ECRHubRequestProto;
import com.wisecashier.ecr.sdk.listener.ECRHubResponseCallBack;
import com.wisecashier.ecr.sdk.util.Constants;

import org.java_websocket.client.WebSocketClient;

public class Payment {
    WebSocketClient webSocketClient;
    private ECRHubResponseCallBack responseCallBack;

    public Payment(WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    public ECRHubResponseCallBack getResponseCallBack() {
        return responseCallBack;
    }

    public void purchase(PaymentParams params, ECRHubResponseCallBack callBack) {
        responseCallBack = callBack;
        if (null == params.getTopic()) {
            params.setTopic(PAYMENT_TOPIC);
        }
        if (null == params.transType) {
            params.setTransType(Constants.TRANS_TYPE_PURCHASE);
        }
        ECRHubRequestProto.VoiceData voiceData = null;
        if (null != params.getVoice_data() && null != params.getVoice_data().getContent()) {
            voiceData = ECRHubRequestProto.VoiceData.newBuilder().setContent(params.getVoice_data().getContent()).setContentLocale(params.getVoice_data().getContent_locale()).build();
        }
        ECRHubRequestProto.RequestBizData bizData = ECRHubRequestProto.RequestBizData.newBuilder().setMerchantOrderNo(params.merchantOrderNo).setTransType("" + params.transType).setOrderAmount(params.transAmount).build();
        ECRHubRequestProto.ECRHubRequest data = ECRHubRequestProto.ECRHubRequest.newBuilder().setRequestId(params.msgId).setTopic(params.getTopic()).setVoiceData(voiceData).setBizData(bizData).setAppId(params.appId).build();
        if (null != webSocketClient && webSocketClient.isOpen()) {
            webSocketClient.send(data.toByteArray());
        }
    }

    public void close(PaymentParams params, ECRHubResponseCallBack callBack) {
        responseCallBack = callBack;
        if (null == params.getTopic()) {
            params.setTopic(CLOSE_TOPIC);
        }
        ECRHubRequestProto.RequestBizData bizData = ECRHubRequestProto.RequestBizData.newBuilder().setMerchantOrderNo(params.getMerchantOrderNo()).build();
        ECRHubRequestProto.ECRHubRequest data = ECRHubRequestProto.ECRHubRequest.newBuilder().setRequestId(params.msgId).setTopic(params.getTopic()).setBizData(bizData).setAppId(params.appId).build();
        if (null != webSocketClient && webSocketClient.isOpen()) {
            webSocketClient.send(data.toByteArray());
        }
    }

    public void query(PaymentParams params, ECRHubResponseCallBack callBack) {
        responseCallBack = callBack;
        if (null == params.getTopic()) {
            params.setTopic(QUERY_TOPIC);
        }
        if (null == params.merchantOrderNo) {
            params.setMerchantOrderNo(params.origMerchantOrderNo);
        }
        ECRHubRequestProto.RequestBizData bizData = ECRHubRequestProto.RequestBizData.newBuilder().setOrigMerchantOrderNo(params.getOrigMerchantOrderNo()).setMerchantOrderNo(params.merchantOrderNo).build();
        ECRHubRequestProto.ECRHubRequest data = ECRHubRequestProto.ECRHubRequest.newBuilder().setRequestId(params.msgId).setTopic(params.getTopic()).setBizData(bizData).setAppId(params.appId).build();
        if (null != webSocketClient && webSocketClient.isOpen()) {
            webSocketClient.send(data.toByteArray());
        }
    }

    public void refund(PaymentParams params, ECRHubResponseCallBack callBack) {
        responseCallBack = callBack;
        if (null == params.getTopic()) {
            params.setTopic(PAYMENT_TOPIC);
        }
        if (null == params.transType) {
            params.setTransType(Constants.TRANS_TYPE_REFUND);
        }
        ECRHubRequestProto.VoiceData voiceData = null;
        if (null != params.getVoice_data() && null != params.getVoice_data().getContent()) {
            voiceData = ECRHubRequestProto.VoiceData.newBuilder().setContent(params.getVoice_data().getContent()).setContentLocale(params.getVoice_data().getContent_locale()).build();
        }
        ECRHubRequestProto.RequestBizData bizData = ECRHubRequestProto.RequestBizData.newBuilder().setMerchantOrderNo(params.merchantOrderNo).setOrigMerchantOrderNo(params.origMerchantOrderNo).setPayMethodCategory(params.payMethod).setTransType("" + params.transType).setOrderAmount(params.transAmount).build();
        ECRHubRequestProto.ECRHubRequest data = ECRHubRequestProto.ECRHubRequest.newBuilder().setRequestId(params.msgId).setTopic(params.getTopic()).setVoiceData(voiceData).setBizData(bizData).setAppId(params.appId).build();
        if (null != webSocketClient && webSocketClient.isOpen()) {
            webSocketClient.send(data.toByteArray());
        }
    }
}
