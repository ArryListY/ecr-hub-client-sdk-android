import android.content.Intent
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.wisecashier.ecr.demo.constant.InvokeConstant
import com.wisecashier.ecr.demo.util.DateUtil
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64
import java.util.TreeMap

fun getMillisecond(): Long {
    return System.currentTimeMillis()
}

fun buildToBeSignedString(data: MutableMap<String, String>): String {
    val sortedParams = TreeMap(data)
    val encodedParams = sortedParams.entries.joinToString("&") { "${it.key}=${it.value}" }
    return encodedParams
}

fun generateSign(signData: String, privateKeyPem: String): String {
    val privateKeyBytes = Base64.getDecoder().decode(privateKeyPem.replace(Regex("-----[\\w\\s]+-----"), "").replace("\n", ""))
    val keySpec = PKCS8EncodedKeySpec(privateKeyBytes)
    val keyFactory = KeyFactory.getInstance("RSA")
    val privateKey: PrivateKey = keyFactory.generatePrivate(keySpec)

    val signature = Signature.getInstance("SHA256withRSA")
    signature.initSign(privateKey)

    val signDataBytes = signData.toByteArray(Charsets.UTF_8)
    signature.update(signDataBytes)

    val signatureBytes = signature.sign()

    // 使用Base64编码将数字签名转换为Base64字符串
    val signatureBase64 = Base64.getEncoder().encodeToString(signatureBytes)

    return signatureBase64
}

//fun verifyResponseSignature(respObject: Map<String, String>, publicKeyPem: String): Boolean {
//    val filteredRespObject = mutableMapOf<String, String>()
//    for ((key, value) in respObject) {
//        if (value != null && key != "sign") {
//            filteredRespObject[key] = value
//        }
//    }
//    val respStringToBeSigned = buildToBeSignedString(filteredRespObject)
//    val respSignature = Base64.getDecoder().decode(respObject["sign"])
//    val publicKeyBytes = Base64.getDecoder()
//        .decode(publicKeyPem.replace(Regex("-----[\\w\\s]+-----"), "").replace("\n", ""))
//    val keySpec = X509EncodedKeySpec(publicKeyBytes)
//    val publicKey = KeyFactory.getInstance("RSA").generatePublic(keySpec)
//    val signature = Signature.getInstance("SHA256withRSA")
//    signature.initVerify(publicKey)
//    signature.update(respStringToBeSigned.toByteArray(Charsets.UTF_8)) // 将签名数据以UTF-8编码字节数组形式传递
//    return signature.verify(respSignature)
//}

fun sendHttpRequest(url: String, jsonString: String): String {
    val urlObj = URL(url)
    val connection = urlObj.openConnection() as HttpURLConnection

    connection.requestMethod = "POST"
    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
    connection.doOutput = true

    val outputStream = DataOutputStream(connection.outputStream)
    outputStream.write(jsonString.toByteArray(Charsets.UTF_8))
    outputStream.flush()
    outputStream.close()

    val responseCode = connection.responseCode
    if (responseCode == HttpURLConnection.HTTP_OK) {
        val responseStream = connection.inputStream
        val responseText = responseStream.bufferedReader(Charsets.UTF_8).readText()
        responseStream.close()
        return responseText
    }

    return ""
}

fun mapToJsonString(parameters: Map<String, Any>): String {
    val gson = Gson()
    val jsonObject = JsonObject()

    for ((key, value) in parameters) {
        jsonObject.addProperty(key, value.toString())
    }

    return gson.toJson(jsonObject)
}

fun main() {
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

    val gatewayUrl = "https://gw.wisepaycloud.com/api/entry"
    val appId = "wz6012822ca2f1as78"

    val parameters = mutableMapOf(
        // Common parameters
        "app_id" to appId,
        "charset" to "UTF-8",
        "format" to "JSON",
        "sign_type" to "RSA2",
        "version" to "1.0",
        "api_version" to "2.0",
        "pay_method_category" to "BANKCARD",
        "message_receiving_application" to "WISECASHIER",
        "timestamp" to getMillisecond().toString(),
        "method" to InvokeConstant.ORDER,
        // API owned parameters
        "merchant_no" to "302200067808",
        "store_no" to "4022000476",
        "terminal_sn" to "WPYB002248000865",
        "order_amount" to "1",
        "price_currency" to "USD",
        "description" to "description",
        "trans_type" to InvokeConstant.PURCHASE.toString(),
        "merchant_order_no" to DateUtil.getCurDateStr("yyyyMMddHHmmss")
    )

    val stringToBeSigned = buildToBeSignedString(parameters)
    val sign = generateSign(stringToBeSigned, appRsaPrivateKeyPem)
    parameters["sign"] = sign

    // Send HTTP request (You will need to handle HTTP requests in your Kotlin environment)
    val jsonString = mapToJsonString(parameters)
    println("Request to gateway[$gatewayUrl] send data  -->> $jsonString")

    // Handle the HTTP request and response here
    val responseStr = sendHttpRequest(gatewayUrl, jsonString)// Replace this with the actual response

    println("Response from gateway[$gatewayUrl] receive data <<-- $responseStr")

    // Verify the signature of the response message
//    val respObject = mapOf<String, String>() // Parse the response JSON into a map
//    val verificationResult = verifyResponseSignature(respObject, gatewayRsaPublicKeyPem)
//    println("SignVerifyResult: $verificationResult")
}
