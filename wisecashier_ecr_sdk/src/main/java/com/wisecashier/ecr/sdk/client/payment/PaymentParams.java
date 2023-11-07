package com.wisecashier.ecr.sdk.client.payment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class PaymentParams {
    //topic
    String topic;
    //The App ID assigned by the system to the caller must be passed in non-offline mode.
    String appId;
    //Merchant order number.
    String merchantOrderNo;

    String origMerchantOrderNo;
    //trans Amount
    String transAmount;
    String orderAmount;
    //price currency
    String priceCurrency;
    //tip amount
    String tipAmount;
    /**
     * trans type
     */
    String transType;
    /**
     * payment method
     */
    String payMethod;
    /**
     * description info
     */
    String description;
    /**
     * notify url
     */
    String notifyUrl;
    /**
     * attach info
     */
    String attach;

    String msgId;

    private VoiceData voice_data;

    private PrintData print_data;

    public void setOrigMerchantOrderNo(String origMerchantOrderNo) {
        this.origMerchantOrderNo = origMerchantOrderNo;
    }

    public String getOrigMerchantOrderNo() {
        return origMerchantOrderNo;
    }

    public void setVoice_data(VoiceData voice_data) {
        this.voice_data = voice_data;
    }

    public void setPrint_data(PrintData print_data) {
        this.print_data = print_data;
    }

    public VoiceData getVoice_data() {
        if(null == voice_data){
            voice_data = new VoiceData();
        }
        return voice_data;
    }

    public PrintData getPrint_data() {
        if(null == print_data){
            print_data = new PrintData();
        }
        return print_data;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public String getDescription() {
        return description;
    }

    public String getAttach() {
        return attach;
    }

    public String getTransType() {
        return transType;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public String getAppId() {
        return appId;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public String getTipAmount() {
        return tipAmount;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public void setTipAmount(String tipAmount) {
        this.tipAmount = tipAmount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("topic", this.topic);
        json.put("app_id", this.appId);
        if (null != merchantOrderNo) {
            json.put("merchant_order_no", this.merchantOrderNo);
        }
        if (null != origMerchantOrderNo) {
            json.put("orig_merchant_order_no", this.origMerchantOrderNo);
        }
        if (null != transAmount) {
            json.put("trans_amount", this.transAmount);
        }
        if (null != orderAmount) {
            json.put("order_amount", this.orderAmount);
        }
        if (null != tipAmount) {
            json.put("tip_amount", this.tipAmount);
        }
        json.put("trans_type", this.transType);
        json.put("pay_method_category", this.payMethod);
        if (null != description) {
            json.put("tip_amount", this.tipAmount);
        }
        if (null != notifyUrl) {
            json.put("notify_url", this.notifyUrl);
        }
        if (null != notifyUrl) {
            json.put("notify_url", this.notifyUrl);
        }
        if (null != attach) {
            json.put("attach", this.attach);
        }
        if (null != msgId) {
            json.put("msg_id", this.msgId);
        }
        return json;
    }

   public class VoiceData {
        /**
         * 语音播报内容
         */
        private String content;
        /**
         * 语音播报语种
         */
        private String content_locale;
        /**
         * 语音文件地址
         */
        private String content_url;

        public void setContent(String content) {
            this.content = content;
        }

        public void setContent_locale(String content_locale) {
            this.content_locale = content_locale;
        }

        public void setContent_url(String content_url) {
            this.content_url = content_url;
        }

        public String getContent() {
            return content;
        }

        public String getContent_locale() {
            return content_locale;
        }

        public String getContent_url() {
            return content_url;
        }
    }

    public class PrintData {
        /**
         * 打印文本内容
         */
        String content;
        /**
         * 打印内容文本地址
         */
        String content_url;

        public void setContent_url(String content_url) {
            this.content_url = content_url;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent_url() {
            return content_url;
        }

        public String getContent() {
            return content;
        }
    }
}
