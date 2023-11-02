package com.wisecashier.ecr.sdk.util;

/**
 * basic constants
 */
public class Constants {
    /**
     * init topic
     */
    public static final String INIT_TOPIC = "ecrhub.pay.init";

    /**
     * payment topic
     */
    public static final String PAYMENT_TOPIC = "ecrhub.pay.order";

    /**
     * query topic
     */
    public static final String QUERY_TOPIC = "ecrhub.pay.query";

    /**
     * close topic
     */
    public static final String CLOSE_TOPIC = "ecrhub.pay.close";

    /**
     * heart beat topic
     */
    public static final String HEART_BEAT_TOPIC = "ecrhub.heartbeat";

    /**
     * trans type purchase
     */
    public static final String TRANS_TYPE_PURCHASE = "1";

    /**
     * trans type void
     */
    public static final String TRANS_TYPE_VOID = "2";

    /**
     * trans type refund
     */
    public static final String TRANS_TYPE_REFUND = "3";

    /**
     * trans type pre-auth
     */
    public static final String TRANS_TYPE_PRE_AUTH = "4";

    /**
     * trans type pre-auth cancel
     */
    public static final String TRANS_TYPE_PRE_AUTH_CANCEL = "5";

    /**
     * trans type pre-auth complete
     */
    public static final String TRANS_TYPE_PRE_AUTH_COMPLETE = "6";

    /**
     * trans type pre-auth complete cancel
     */
    public static final String TRANS_TYPE_PRE_AUTH_COMPLETE_CANCEL = "7";

    /**
     * trans type pre-auth complete refund
     */
    public static final String TRANS_TYPE_PRE_AUTH_COMPLETE_REFUND = "8";

    /**
     * BankCard Payment
     */
    public final static String BANKCARD_PAY_TYPE = "BANKCARD";
    /**
     * qr C scan B
     */
    public final static String QR_C_SCAN_B_PAY_TYPE = "QR_C_SCAN_B";
    /**
     * qr B scan C
     */
    public final static String QR_B_SCAN_C = "QR_B_SCAN_C";
}
