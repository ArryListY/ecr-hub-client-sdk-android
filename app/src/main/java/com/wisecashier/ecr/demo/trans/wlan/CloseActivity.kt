package com.wisecashier.ecr.demo.trans.wlan

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.wisecashier.ecr.demo.MainActivity
import com.wisecashier.ecr.demo.R
import com.wisecashier.ecr.sdk.client.payment.PaymentParams
import com.wisecashier.ecr.sdk.listener.ECRHubResponseCallBack
import kotlinx.android.synthetic.main.activity_close.edit_input_merchant_order_no
import kotlinx.android.synthetic.main.activity_close.tv_btn_1
import kotlinx.android.synthetic.main.activity_close.tv_btn_2
import kotlinx.android.synthetic.main.activity_close.tv_btn_3

class CloseActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_close)
        tv_btn_2.setOnClickListener {
            finish()
        }
        tv_btn_1.setOnClickListener {
            val merchantOrderNo = edit_input_merchant_order_no.text.toString()
            val params = PaymentParams()
            if (merchantOrderNo.isEmpty()) {
                val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)
                val orderNo = sharedPreferences.getString("merchant_order_no","").toString()
                params.merchantOrderNo = orderNo
                Log.e("Test","origMerchantOrderNo: $orderNo   ==?  ${params.origMerchantOrderNo}")
            } else {
                params.merchantOrderNo = merchantOrderNo
            }
            params.appId = "wz6012822ca2f1as78"
            params.msgId = "11322"
            MainActivity.mClient.payment.close(params, object :
                ECRHubResponseCallBack {
                override fun onError(errorCode: String?, errorMsg: String?) {
                    runOnUiThread {
                        tv_btn_3.text = tv_btn_3.text.toString() + "\n" + "交易失败" + errorMsg
                    }
                }

                override fun onSuccess(data: String?) {
                    runOnUiThread {
                        tv_btn_3.text =
                            tv_btn_3.text.toString() + "\n" + "交易结果数据" + "\n" + data.toString()
                    }
                }

            })
        }

    }
}