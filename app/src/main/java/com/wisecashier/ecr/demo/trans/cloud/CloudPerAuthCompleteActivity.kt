package com.wisecashier.ecr.demo.trans.cloud

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.widget.Toast
import buildToBeSignedString
import com.wisecashier.ecr.demo.R
import com.wisecashier.ecr.demo.constant.InvokeConstant
import com.wisecashier.ecr.demo.util.DateUtil
import generateSign
import getMillisecond
import kotlinx.android.synthetic.main.activity_cloud_perauthcomplete.*
import kotlinx.android.synthetic.main.activity_cloud_perauthcomplete.edit_input_merchant_order_no
import kotlinx.android.synthetic.main.activity_cloud_perauthcomplete.tv_btn_1
import kotlinx.android.synthetic.main.activity_cloud_perauthcomplete.tv_btn_2
import kotlinx.android.synthetic.main.activity_cloud_perauthcomplete.tv_btn_3
import kotlinx.android.synthetic.main.activity_cloud_query.*
import mapToJsonString
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class CloudPerAuthCompleteActivity : Activity() {

    // 创建后台线程和主线程的 Handler
    private lateinit var backgroundThread: HandlerThread
    private lateinit var backgroundHandler: Handler
    private val mainHandler = Handler(Looper.getMainLooper())

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloud_perauthcomplete)
        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)


        tv_btn_2.setOnClickListener {
            finish()
        }

        tv_btn_1.setOnClickListener {
            // 启动后台线程
            backgroundThread = HandlerThread("NetworkThread")
            backgroundThread.start()
            backgroundHandler = Handler(backgroundThread.looper)

            val appRsaPrivateKeyPem = InvokeConstant.appRsaPrivateKeyPem
            val gatewayRsaPublicKeyPem = InvokeConstant.gatewayRsaPublicKeyPem
            val url = sharedPreferences.getString("url","").toString()
            val appId = InvokeConstant.APP_ID
            val amount = edit_input_amount.text.toString()
            val expire = edit_input_expires.text.toString()
            if (amount.isEmpty()) {
                Toast.makeText(this, "请输入金额", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val amt = String.format("%.2f", amount.toDouble())

//            val description = edit_input_description.text.toString()
            val defaultDescription = "默认描述"
            val merchant_no = sharedPreferences.getString("merchant_no", "").toString()
            val store_no = sharedPreferences.getString("store_no", "").toString()
            val terminal_sn = sharedPreferences.getString("terminal_sn", "").toString()
            val price_currency = sharedPreferences.getString("price_currency", "").toString()


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
                "merchant_no" to merchant_no,
                "store_no" to store_no,
                "terminal_sn" to terminal_sn,
                "expires" to (if (expire.isNotEmpty()) expire else "300"),
                "order_amount" to amt,
                "price_currency" to price_currency,
                "description" to defaultDescription,
                "trans_type" to InvokeConstant.PRE_AUTH_COMPLETE.toString(),
                "merchant_order_no" to DateUtil.getCurDateStr("yyyyMMddHHmmss")
            )

            if (edit_input_merchant_order_no.text.isNotEmpty()){
                val org_merchant_order_no = edit_input_merchant_order_no.text.toString()
                parameters["orig_merchant_order_no"] = org_merchant_order_no
                Log.e("isNotEmpty", org_merchant_order_no)
            } else {
                val org_merchant_order_no = sharedPreferences.getString("merchant_order_no", "").toString()
                parameters["orig_merchant_order_no"] = org_merchant_order_no
                Log.e("isEmpty", org_merchant_order_no)
            }

            val editor = sharedPreferences.edit()
            editor.putString("merchant_order_no", parameters.getValue("merchant_order_no"))
            editor.apply()

            val stringToBeSigned = buildToBeSignedString(parameters)
            val sign = generateSign(stringToBeSigned, appRsaPrivateKeyPem)
            parameters["sign"] = sign

            // Send HTTP request (You will need to handle HTTP requests in your Kotlin environment)
            val jsonString = mapToJsonString(parameters)
            runOnUiThread {
                Log.e("Test", "Request to gateway -->> [$url] send data  -->> $jsonString")
                tv_btn_3.text =
                    "Request to gateway [$url] send data  -->> $jsonString"
            }
            backgroundHandler.post {
                val response = sendHttpRequest(url, jsonString)
                mainHandler.post {
                    // 在主线程中处理网络请求的结果
                    // 这里可以更新 UI 或执行其他操作
                    runOnUiThread {
                        Log.e("Test","Response from gateway [$url] receive data <<-- $response")
                        tv_btn_3.text =
                            tv_btn_3.text.toString() + "\n" + "Response from gateway [$url] receive data <<-- $response"
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        // 释放后台线程资源
        backgroundThread.quitSafely()
        super.onDestroy()
    }

    private fun sendHttpRequest(url: String, jsonString: String): String {
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

}