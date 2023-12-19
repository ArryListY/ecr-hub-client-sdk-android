package com.wisecashier.ecr.demo.trans.cloud

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import buildToBeSignedString
import com.wisecashier.ecr.demo.R
import com.wisecashier.ecr.demo.constant.InvokeConstant
import com.wisecashier.ecr.demo.util.DateUtil
import generateSign
import getMillisecond
import kotlinx.android.synthetic.main.activity_cloud_payment.*
import mapToJsonString
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL


class CloudPurchaseActivity : Activity() {

    // 创建后台线程和主线程的 Handler
    private lateinit var backgroundThread: HandlerThread
    private lateinit var backgroundHandler: Handler
    private val mainHandler = Handler(Looper.getMainLooper())
    private lateinit var admin: String // 声明为成员变量

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloud_payment)
        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)

        val switchButton = findViewById<Switch>(R.id.switchButton)
        admin = ""
        switchButton.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            admin = if (isChecked) {
                Log.e("Test", "open")
                "1"
            } else {
                Log.e("Test", "close")
                ""
            }
        }

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
            val appId = InvokeConstant.APP_ID

            val amount = edit_input_amount.text.toString()
            if (amount.isEmpty()) {
                Toast.makeText(this, "请输入金额", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val tips = edit_input_tip.text.toString()
            val cash = edit_input_cash.text.toString()
            val amt = String.format("%.2f", amount.toDouble())
            val description = edit_input_description.text.toString()
            val expire = edit_input_expires.text.toString()
            val orderNo = DateUtil.getCurDateStr("yyyyMMddHHmmss")
            val defaultDescription = "This is a ECR order"

            val merchant_no = sharedPreferences.getString("merchant_no", "").toString()
            val store_no = sharedPreferences.getString("store_no", "").toString()
            val terminal_sn = sharedPreferences.getString("terminal_sn", "").toString()
            val price_currency = sharedPreferences.getString("price_currency", "").toString()
            val url = sharedPreferences.getString("url", "").toString()

            val editor = sharedPreferences.edit()
            editor.putString("merchant_order_no", orderNo)
            editor.apply()

            val parameters = mutableMapOf(
                // Common parameters
                "app_id" to appId,
                "charset" to "UTF-8",
                "format" to "JSON",
                "sign_type" to "RSA2",
                "version" to "1.0",
                "api_version" to "2.0",
//                "pay_method_category" to "BANKCARD",
                "message_receiving_application" to "WISECASHIER",
                "timestamp" to getMillisecond().toString(),
                "method" to InvokeConstant.ORDER,
//                "required_terminal_authentication" to "1",
                // API owned parameters
                "merchant_no" to merchant_no,
                "store_no" to store_no,
                "terminal_sn" to terminal_sn,
                "order_amount" to amt,
                "expires" to (if (expire.isNotEmpty()) expire else "300"),
                "price_currency" to price_currency,
                "description" to (if (description.isNotEmpty()) description else defaultDescription),
                "trans_type" to InvokeConstant.PURCHASE.toString(),
                "merchant_order_no" to orderNo
            )

            if (tips.isNotEmpty()) {
                val tip = String.format("%.2f", tips.toDouble())
                parameters["tip_amount"] = tip
            }

            if (cash.isNotEmpty()) {
                val cashAmount = String.format("%.2f", cash.toDouble())
                parameters["cashback_amount"] = cashAmount
            }

            if (admin == "1") {
                Log.e("Test", "开启管理员验证")
                parameters["required_terminal_authentication"] = "1"
            }

            val stringToBeSigned = buildToBeSignedString(parameters)
            val sign = generateSign(stringToBeSigned, appRsaPrivateKeyPem)
            parameters["sign"] = sign
            Log.e("Test", "sign  -->> [$sign] parameters  -->> $parameters")

            // Send HTTP request (You will need to handle HTTP requests in your Kotlin environment)
            val jsonString = mapToJsonString(parameters)
            runOnUiThread {
                Log.e("Test", "Request to gateway [$url] send data  -->> $jsonString")
                tv_btn_3.text =
                    "Request to gateway [$url] send data  -->> $jsonString"
            }

            backgroundHandler.post {
                val response = sendHttpRequest(url, jsonString)
                mainHandler.post {
                    // 在主线程中处理网络请求的结果
                    // 这里可以更新 UI 或执行其他操作
                    runOnUiThread {
                        Log.e("Test", "Response from gateway [$url] receive data <<-- $response")
                        tv_btn_3.text =
                            tv_btn_3.text.toString() + "\n" + "Response from gateway [$url] receive data <<-- $response"
                    }
                }
            }
        }


//        tv_btn_qr.setOnClickListener {
//            // 启动后台线程
//            backgroundThread = HandlerThread("NetworkThread")
//            backgroundThread.start()
//            backgroundHandler = Handler(backgroundThread.looper)
//
//            val appRsaPrivateKeyPem = InvokeConstant.appRsaPrivateKeyPem
//            val gatewayRsaPublicKeyPem = InvokeConstant.gatewayRsaPublicKeyPem
//            val appId = InvokeConstant.APP_ID
//
//            val amount = edit_input_amount.text.toString()
//            if (amount.isEmpty()) {
//                Toast.makeText(this, "请输入金额", Toast.LENGTH_LONG).show()
//                return@setOnClickListener
//            }
//            val tips = edit_input_tip.text.toString()
//            val cash = edit_input_cash.text.toString()
//            val amt = String.format("%.2f", amount.toDouble())
//            val description = edit_input_description.text.toString()
//            val expire = edit_input_expires.text.toString()
//            val orderNo = DateUtil.getCurDateStr("yyyyMMddHHmmss")
//            val defaultDescription = "This is a ECR order"
//
//            val merchant_no = sharedPreferences.getString("merchant_no", "").toString()
//            val store_no = sharedPreferences.getString("store_no", "").toString()
//            val terminal_sn = sharedPreferences.getString("terminal_sn", "").toString()
//            val price_currency = sharedPreferences.getString("price_currency", "").toString()
//            val url = sharedPreferences.getString("url", "").toString()
//
//            val editor = sharedPreferences.edit()
//            editor.putString("merchant_order_no", orderNo)
//            editor.apply()
//
//            val parameters = mutableMapOf(
//                // Common parameters
//                "app_id" to appId,
//                "charset" to "UTF-8",
//                "format" to "JSON",
//                "sign_type" to "RSA2",
//                "version" to "1.0",
//                "api_version" to "2.0",
//                "pay_method_category" to "QR_C_SCAN_B",
//                "pay_method_id" to "ScanToPay",
//                "message_receiving_application" to "WISECASHIER",
//                "timestamp" to getMillisecond().toString(),
//                "method" to InvokeConstant.ORDER,
//                // API owned parameters
//                "merchant_no" to merchant_no,
//                "store_no" to store_no,
//                "terminal_sn" to terminal_sn,
//                "order_amount" to amt,
//                "expires" to (if (expire.isNotEmpty()) expire else "300"),
//                "price_currency" to price_currency,
//                "description" to (if (description.isNotEmpty()) description else defaultDescription),
//                "trans_type" to InvokeConstant.PURCHASE.toString(),
//                "merchant_order_no" to orderNo
//            )
//
//            if (tips.isNotEmpty()) {
//                val tip = String.format("%.2f", tips.toDouble())
//                parameters["tip_amount"] = tip
//            }
//
//            val stringToBeSigned = buildToBeSignedString(parameters)
//            val sign = generateSign(stringToBeSigned, appRsaPrivateKeyPem)
//            parameters["sign"] = sign
//            Log.e("Test", "sign  -->> [$sign] parameters  -->> $parameters")
//
//            // Send HTTP request (You will need to handle HTTP requests in your Kotlin environment)
//            val jsonString = mapToJsonString(parameters)
//            runOnUiThread {
//                Log.e("Test", "Request to gateway [$url] send data  -->> $jsonString")
//                tv_btn_3.text =
//                    "Request to gateway [$url] send data  -->> $jsonString"
//            }
//
//            backgroundHandler.post {
//                val response = sendHttpRequest(url, jsonString)
//                mainHandler.post {
//                    // 在主线程中处理网络请求的结果
//                    // 这里可以更新 UI 或执行其他操作
//                    runOnUiThread {
//                        Log.e("Test", "Response from gateway [$url] receive data <<-- $response")
//                        tv_btn_3.text =
//                            tv_btn_3.text.toString() + "\n" + "Response from gateway [$url] receive data <<-- $response"
//                    }
//                }
//            }
//        }
    }

//    private fun cScanQR() {
//
//    }

    override fun onDestroy() {
        // 释放后台线程资源
        if (::backgroundThread.isInitialized) {
            Log.e("Test","isInitialized")
            backgroundThread.quitSafely()
        }
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