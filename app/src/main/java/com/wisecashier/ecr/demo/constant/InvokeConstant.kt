package com.wisecashier.ecr.demo.constant

object InvokeConstant {

    val appRsaPrivateKeyPem = """
-----BEGIN RSA PRIVATE KEY-----
-----END RSA PRIVATE KEY-----
""".trimIndent()
    val gatewayRsaPublicKeyPem = """
-----BEGIN PUBLIC KEY-----
-----END PUBLIC KEY-----
""".trimIndent()

    //FIXED
    //Please don't change these parameter,otherwise the transaction will be failed.
    const val PURCHASE = 1
    const val VOID = 2
    const val REFUND = 3
    const val PRE_AUTH = 4
    const val PRE_AUTH_COMPLETE = 6
    const val PRE_AUTH_CANCEL = 5
    const val PRE_AUTH_COMPLETE_CANCEL = 7
    const val PRE_AUTH_COMPLETE_REFUND = 8
    const val BALANCE = "BALANCE"
    const val CASH_ADVANCE = "CASHADVANCE"
    const val CASH_BACK = "CASHBACK"
    const val SETTLEMENT = "SETTLEMENT"
    const val REPRINT = "REPRINT"

    const val APP_ID = "wz6012822ca2f1as78"
    const val SANDBOX_GATEWAY_URL = "https://gw.wisepaycloud.com/api/entry"
    const val GATEWAY_URL = "https://gw.paycloud.world/api/entry"
    const val ORDER = "wisehub.cloud.pay.order"
    const val QUERY = "order.query"

}