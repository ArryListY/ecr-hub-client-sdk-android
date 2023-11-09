package com.wisecashier.ecr.demo.trans.wlan

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.wisecashier.ecr.demo.MainActivity
import com.wisecashier.ecr.demo.R
import com.wisecashier.ecr.demo.constant.InvokeConstant
import com.wisecashier.ecr.sdk.client.payment.PaymentParams
import com.wisecashier.ecr.sdk.listener.ECRHubResponseCallBack
import com.wisecashier.ecr.sdk.util.Constants
import kotlinx.android.synthetic.main.activity_payment.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class PaymentActivity : Activity() {
    var merchantOrderNo: String? = null
    fun getCurDateStr(format: String?): String? {
        val c = Calendar.getInstance()
        return date2Str(c, format)
    }

    fun date2Str(c: Calendar?, format: String?): String? {
        return if (c == null) null else date2Str(
            c.time,
            format
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun date2Str(d: Date?, format: String?): String? {
        var format = format
        return if (d == null) {
            null
        } else {
            if (format == null || format.length == 0) {
                format = "yyyy-MM-dd HH:mm:ss"
            }
            val sdf = SimpleDateFormat(format)
            sdf.format(d)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        tv_btn_2.setOnClickListener {
            finish()
        }
        tv_btn_1.setOnClickListener {
            val amount = edit_input_amount.text.toString()
            val tip = edit_input_tip.text.toString()
            val description = edit_input_description.text.toString()
            if (amount.isEmpty()) {
                Toast.makeText(this, "请输入金额", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val params = PaymentParams()
            params.transType = Constants.TRANS_TYPE_PURCHASE
            params.appId = InvokeConstant.APP_ID
            merchantOrderNo = "WLAN_" + getCurDateStr("yyyyMMddHHmmss")
            val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("merchant_order_no", merchantOrderNo)
            editor.apply()
            params.merchantOrderNo = merchantOrderNo
            params.transAmount = amount
            params.tipAmount = tip
            params.description = description
            params.msgId = "111111"
            val voiceData = params.voice_data
            voiceData.content = "WiseCashier2 Received a new order"
            voiceData.content_locale = "en-US"
            params.voice_data = voiceData
            runOnUiThread {
                tv_btn_3.text =
                    "交易发送数据：" + "\n" + params.toJSON().toString()
            }
            MainActivity.mClient.payment.purchase(params, object :
                ECRHubResponseCallBack {
                override fun onError(errorCode: String?, errorMsg: String?) {
                    runOnUiThread {
                        tv_btn_3.text = tv_btn_3.text.toString() + "\n" + "交易失败" + errorMsg
                    }
                }

                override fun onSuccess(data: String?) {
                    runOnUiThread {
                        tv_btn_3.text =
                            tv_btn_3.text.toString() + "\n" + "交易结果数据：" + "\n" + data.toString()
                    }
                }
            })
        }
    }

}