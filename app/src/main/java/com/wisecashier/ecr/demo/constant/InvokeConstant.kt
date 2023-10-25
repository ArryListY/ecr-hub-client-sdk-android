package com.wisecashier.ecr.demo.constant

object InvokeConstant {

    val appRsaPrivateKeyPem = """
-----BEGIN RSA PRIVATE KEY-----
MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCJ9N8YTm0wEuBye5zSiZKncNIg6WBvTHjg4dYc9BH0zR+LeHd/jVo+InrMiIFgblnf7eYJ/wlPG5cSMMSyQMHj/7o0prc+VZsqi1mjX88InK4qDl6qFBFrg4dRrCAwILLVOP/ppUU3souMmnYnvOSUyuLBnhSno8qTpFSbcFAkPaHRmUmcTtVPWuNNbPaZVlNn81imIIVw8LKzOzCz9CNNcikkvbUlZp/cZ0Fl3s6icqOCjRvamg8KLZJs9D52S20X60ynreEIn8g6lr77byOGCCRwpdMl9Cl89WmbC3A3RKh7GRPtjyx3B5aQqE3sK5sNT8H8/EqcnQe8QoVBs7x1AgMBAAECggEAbE6vB+oqlt97DuY1TKVtWb+dePFAIKEtFYC4FKsZndOcvGaripxzCO0Q85sH16lLLh8bxyVPLag/hqx7AGcO0e1nRwbMPkf/NfuJOFZzuBMqOSJm96ghtQLiLiCwdJh3Ticd41U5bmziWlS6BqCp5JcUR2XQWXyiAh+1vQMEKC56CNPxr7imXITS7BYdY0qiGiOANcoEJhfQXn4BjzEm2FJufdlHW6/IeRYZ874HF3/7aUOyhbnapxYHU9PzicMc9XwerMcXMGvOfTUnCtRVONLn7jiknbpwdZ1d8PoItUZuaXdAyY6wFHZF+KvyrEoOV0eWRYzPgku2oSMD7IckUQKBgQDy+etogil+3F8S/qW1WiG6l2tLx5cwq3Ak9weN1zOo8A43obtXRxW2b4fDeol8y6LmYaIW9kKmK0qHEx5LoUSH7VN3FigNpFSa6muVJUuY+ob4H3Je+h3PaqbwhVFzjDofuIolPiJZW64LaFPZcxxD9vM2tZ6txLeMUmGftPoVSwKBgQCRWeNxbvTs8U5UknrdDZHUwXUmy5Qg2LUzxDXdbZXgolMh0AgDShcEFvaF+G3OFHj9vovytt0HtEXyC9LEc6w/a08mtGHiAJbjyjrJCHRNrcFflEm1xseIqXQXDj5Tw05XkAnlWN+r0w3Ix45+GrNhMOGfDkVyQeYI5Jsxxt0dPwKBgFJwcWr4HtQoOSnctKSffCovDfycL7QXtukT18BMb/611F0TxtiKCdfoZ4vvm454GUFJhxF7ZIm0zoid9/15LiNgZp1VKynVw878Epx8FvZEql6tbMTE4DBr41BgK46k2WPB3T1do5HmBVthfnGdGM4Gj+bUII6c3BoEKZNieCeZAoGAQ3rx5wXWW/KjpQvkUqAsJhQyqXI2MRGq/n+Hamen/4QdCEOmlLBfAx0OEqCFifljOpquKl7POvZsyrTGg0IYo9DUDGoOT3hqlRKcPBzasf2LGy6jEetZU48oQFPyh7zSsEBE999M6F6xtZdABjerM+IXvVpIz4TcoSBRFMj4es0CgYEAiNCbs2YPKBFpJq9EYsSsx0GwXAItXFAM71TogWs/8InGNq0PYRZTn9Lq6mFEyLkFql1zWQkS7CG3uDPBV+V2G4MbvoZeFgiQmly7uMQJwbEiKjJBsOzkYY2ZhFYLjGUpGer2U82XeEU6F/Vh4FLkVEE2iB3nPpVQs0qPZxfFZvM=
-----END RSA PRIVATE KEY-----
""".trimIndent()
    val gatewayRsaPublicKeyPem = """
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2m4nkQKyQAxJc8VVsz/L6qVbtDWRTBolUK8Dwhi9wH6aygA6363PVNEPM8eRI5W19ssCyfdtNFy6DRAureoYV053ETPUefEA5bHDOQnjbb9PuNEfT651v8cqwEaTptaxj2zujsWI8Ad3R50EyQHsskQWms/gv2aB36XUM4vyOIk4P1f3dxtqigH0YROEYiuwFFqsyJuNSjJzNbCmfgqlQv/+pE/pOV9MIQe0CAdD26JF10QpSssEwKgvKvnXPUynVu09cjSEipev5cLJSApKSDZxrRjSFBXrh6nzg8JK05ehkI8wdsryRUneh0PGN0PgYLP/wjKiqlgTJaItxnb/JQIDAQAB
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
    const val ORDER = "wisehub.cloud.pay.order"
    const val QUERY = "order.query"

}