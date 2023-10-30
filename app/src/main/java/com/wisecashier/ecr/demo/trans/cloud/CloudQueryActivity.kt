package com.wisecashier.ecr.demo.trans.cloud

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import buildToBeSignedString
import com.wisecashier.ecr.demo.R
import com.wisecashier.ecr.demo.constant.InvokeConstant
import generateSign
import getMillisecond
import kotlinx.android.synthetic.main.activity_cloud_query.*
import kotlinx.android.synthetic.main.activity_cloud_query.tv_btn_1
import kotlinx.android.synthetic.main.activity_cloud_query.tv_btn_2
import kotlinx.android.synthetic.main.activity_cloud_query.tv_btn_3
import mapToJsonString
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class CloudQueryActivity : Activity() {

    // 创建后台线程和主线程的 Handler
    private lateinit var backgroundThread: HandlerThread
    private lateinit var backgroundHandler: Handler
    private val mainHandler = Handler(Looper.getMainLooper())


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloud_query)
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

            val merchant_no = sharedPreferences.getString("merchant_no", "").toString()


            val parameters = mutableMapOf(
                // Common parameters
                "app_id" to appId,
                "charset" to "UTF-8",
                "format" to "JSON",
                "sign_type" to "RSA2",
                "version" to "1.0",
                "timestamp" to getMillisecond().toString(),
                "method" to InvokeConstant.QUERY,
                // API owned parameters
                "merchant_no" to merchant_no
            )
            if (edit_input_merchant_order_no.text.isNotEmpty()){
                val merchant_order_no = edit_input_merchant_order_no.text.toString()
                parameters["merchant_order_no"] = merchant_order_no
                Log.e("isNotEmpty", merchant_order_no)
            } else {
                val merchant_order_no = sharedPreferences.getString("merchant_order_no", "").toString()
                parameters["merchant_order_no"] = merchant_order_no
                Log.e("isEmpty", merchant_order_no)
            }

            val stringToBeSigned = buildToBeSignedString(parameters)
            val sign = generateSign(stringToBeSigned, appRsaPrivateKeyPem)
            parameters["sign"] = sign

            // Send HTTP request (You will need to handle HTTP requests in your Kotlin environment)
            val jsonString = mapToJsonString(parameters)
            runOnUiThread {
                tv_btn_3.text =
                    "Request to gateway[$url] send data  -->> $jsonString"
            }
            backgroundHandler.post {
                val response = sendHttpRequest(url, jsonString)
                mainHandler.post {
                    // 在主线程中处理网络请求的结果
                    // 这里可以更新 UI 或执行其他操作
                    runOnUiThread {
                        tv_btn_3.text =
                            tv_btn_3.text.toString() + "\n" + "Response from gateway[$url] receive data <<-- $response"
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        // 释放后台线程资源
        backgroundThread.quitSafely()
        backgroundThread.interrupt()
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