package com.wisecashier.ecr.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.wisecashier.ecr.demo.trans.cloud.CloudActivity
import com.wisecashier.ecr.demo.trans.wlan.CloseActivity
import com.wisecashier.ecr.demo.trans.wlan.PaymentActivity
import com.wisecashier.ecr.demo.trans.wlan.QueryActivity
import com.wisecashier.ecr.demo.trans.wlan.RefundActivity
import com.wisecashier.ecr.sdk.client.ECRHubClient
import com.wisecashier.ecr.sdk.client.ECRHubConfig
import com.wisecashier.ecr.sdk.listener.ECRHubConnectListener
import com.wisecashier.ecr.sdk.listener.ECRHubResponseCallBack
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity(), ECRHubConnectListener {
    companion object {
        lateinit var mClient: ECRHubClient
    }

    var isConnected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val config = ECRHubConfig()
        tv_btn_5.setOnClickListener {
            if (isConnected) {
                return@setOnClickListener
            }
            val text = edit_input_ip.text.toString()
            if (text.isEmpty()) {
                Toast.makeText(this, "请输入ip地址", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            mClient = ECRHubClient("ws://" + text, config, this)
            mClient.connect()
        }
        tv_btn_8.setOnClickListener {
            mClient.disConnect()
        }
        tv_btn_2.setOnClickListener {
            if (!isConnected) {
                runOnUiThread {
                    Toast.makeText(this, "未连接服务器", Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }
            startActivity(Intent(applicationContext, PaymentActivity::class.java))
        }
        tv_btn_7.setOnClickListener {
            if (!isConnected) {
                runOnUiThread {
                    Toast.makeText(this, "未连接服务器", Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }
            startActivity(Intent(applicationContext, QueryActivity::class.java))
        }
        tv_btn_6.setOnClickListener {
            if (!isConnected) {
                runOnUiThread {
                    Toast.makeText(this, "未连接服务器", Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }
            startActivity(Intent(applicationContext, RefundActivity::class.java))
        }
        tv_btn_4.setOnClickListener {
            if (!isConnected) {
                runOnUiThread {
                    Toast.makeText(this, "未连接服务器", Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }
            startActivity(Intent(applicationContext, CloseActivity::class.java))
        }

        tv_btn_1.setOnClickListener {
            if (!isConnected) {
                runOnUiThread {
                    Toast.makeText(this, "未连接服务器", Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }
            tv_btn_3.text = "开始初始化..."
            /**
             * 初始化连接接口
             * @param url 服务器IP地址
             * @param cackback 请求回调
             */
            mClient.init(object :
                ECRHubResponseCallBack {
                override fun onError(errorCode: String?, errorMsg: String?) {
                    runOnUiThread {
                        tv_btn_3.text = tv_btn_3.text.toString() + "\n" + "初始化失败" + errorMsg
                    }
                }

                override fun onSuccess(data: String?) {
                    runOnUiThread {
                        tv_btn_3.text =
                            tv_btn_3.text.toString() + "\n" + "初始化成功" + data.toString()
                    }
                }
            })
        }

        tv_btn_cloud.setOnClickListener {
            startActivity(Intent(applicationContext, CloudActivity::class.java))
        }

    }

    override fun onConnect() {
        Log.e("Test", "onConnect")
        runOnUiThread {
            Toast.makeText(this, "连接成功", Toast.LENGTH_LONG).show()
        }
        isConnected = true
    }

    override fun onDisconnect() {
        Log.e("Test", "onDisconnect")
        isConnected = false
    }

    override fun onError(errorCode: String?, errorMsg: String?) {
        Log.e("Test", "onError")
        isConnected = false
    }
}
