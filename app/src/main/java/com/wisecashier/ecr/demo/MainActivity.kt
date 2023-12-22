package com.wisecashier.ecr.demo

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.wisecashier.ecr.demo.constant.InvokeConstant
import com.wisecashier.ecr.demo.trans.cloud.CloudActivity
import com.wisecashier.ecr.demo.trans.wlan.CloseActivity
import com.wisecashier.ecr.demo.trans.wlan.PaymentActivity
import com.wisecashier.ecr.demo.trans.wlan.QueryActivity
import com.wisecashier.ecr.demo.trans.wlan.RefundActivity
import com.wisecashier.ecr.sdk.client.ECRHubClient
import com.wisecashier.ecr.sdk.client.ECRHubConfig
import com.wisecashier.ecr.sdk.jmdns.SearchServerListener
import com.wisecashier.ecr.sdk.listener.ECRHubConnectListener
import com.wisecashier.ecr.sdk.listener.ECRHubResponseCallBack
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity(), ECRHubConnectListener, SearchServerListener {
    companion object {
        lateinit var mClient: ECRHubClient
    }

    var isConnected: Boolean = false
    var ip = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val config = ECRHubConfig()
        mClient = ECRHubClient(this, config, this)
        tv_btn_5.setOnClickListener {
            if (ip.isEmpty()) {
                Log.e("test","点击")

                mClient.findServer(this@MainActivity)

            } else {
                mClient.autoConnect(ip)
            }
        }

        tv_btn_cloud.setOnClickListener {
            showConfirmationDialog()
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

        tv_btn_9.setOnClickListener {
            if (!isConnected) {
                runOnUiThread {
                    Toast.makeText(this, "未连接服务器", Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }
            tv_btn_3.text = tv_btn_3.text.toString() + "\n" + "开始配对..."
            /**
             * 初始化连接接口
             * @param url 服务器IP地址
             * @param cackback 请求回调
             */
            mClient.requestPair("my", object :
                ECRHubResponseCallBack {
                override fun onError(errorCode: String?, errorMsg: String?) {
                    runOnUiThread {
                        tv_btn_3.text = tv_btn_3.text.toString() + "\n" + "配对失败" + errorMsg
                    }
                }

                override fun onSuccess(data: String?) {
                    runOnUiThread {
                        tv_btn_3.text =
                            tv_btn_3.text.toString() + "\n" + "配对成功" + data.toString()
                    }
                }
            })
        }

        tv_btn_10.setOnClickListener {
            if (!isConnected) {
                runOnUiThread {
                    Toast.makeText(this, "未连接服务器", Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }
            tv_btn_3.text = tv_btn_3.text.toString() + "\n" + "开始取消配对..."
            /**
             * 初始化连接接口
             * @param url 服务器IP地址
             * @param cackback 请求回调
             */
            mClient.requestUnPair("my", object :
                ECRHubResponseCallBack {
                override fun onError(errorCode: String?, errorMsg: String?) {
                    runOnUiThread {
                        tv_btn_3.text = tv_btn_3.text.toString() + "\n" + "取消配对失败" + errorMsg
                    }
                }

                override fun onSuccess(data: String?) {
                    runOnUiThread {
                        tv_btn_3.text =
                            tv_btn_3.text.toString() + "\n" + "取消配对成功" + data.toString()
                    }
                }
            })
        }

        tv_btn_1.setOnClickListener {
            if (!isConnected) {
                runOnUiThread {
                    Toast.makeText(this, "未连接服务器", Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }
            tv_btn_3.text = tv_btn_3.text.toString() + "\n" + "开始初始化..."
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
    }

    // 显示询问对话框
    private fun showConfirmationDialog() {
        val options = arrayOf("AddPay-生产-PayCloud Test Merchant","生产-PayCloud Test Merchant-PP35272139001292","生产-AddPay Test Merchant-PP35272203002342","AddPay-测试-PP35272139001292", "AddPay-测试-T2-WTYG002313000143", "AddPay-测试-Dave-PP35272108005595", "AddPay-测试-L3 Test Merchant")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("请选择运行环境")
        builder.setItems(options) { dialog, which ->
            // 用户选择了一个选项
            val selectedOption = options[which]
            // 这里可以根据用户选择执行不同的操作
            processSelectedOption(selectedOption)
            dialog.dismiss()
        }

        builder.setNegativeButton("取消", DialogInterface.OnClickListener { dialog, which ->
            // 用户取消了操作
            dialog.dismiss()
        })

        val dialog = builder.create()
        dialog.show()
    }

    private fun processSelectedOption(option: String) {
        var url = ""
        var defaultMerchant = ""
        var defaultStore = ""
        var defaultTerminalSN = ""
        var defaultCurrency = ""

        when (option) {
            "AddPay-生产-PayCloud Test Merchant" -> {
                // 将参数传递给其他函数或进行其他逻辑
                url = InvokeConstant.GATEWAY_URL
                defaultMerchant = "312100009847"
                defaultStore = "4121000105"
                defaultTerminalSN = "WTYG002313000143"
                defaultCurrency = "ZAR"
            }
            "生产-PayCloud Test Merchant-PP35272139001292" -> {
                url = InvokeConstant.GATEWAY_URL
                defaultMerchant = "312100009847"
                defaultStore = "4123002919"
                defaultTerminalSN = "PP35272139001292"
                defaultCurrency = "ZAR"
            }
            "生产-AddPay Test Merchant-PP35272203002342" -> {
                url = InvokeConstant.GATEWAY_URL
                defaultMerchant = "312100009934"
                defaultStore = "4121000135"
                defaultTerminalSN = "PP35272203002342"
                defaultCurrency = "ZAR"
            }
            "AddPay-测试-PP35272139001292" -> {
                url = InvokeConstant.SANDBOX_GATEWAY_URL
                defaultMerchant = "302300000582"
                defaultStore = "4023000003"
                defaultTerminalSN = "PP35272139001292"
                defaultCurrency = "ZAR"
            }
            "AddPay-测试-T2-WTYG002313000143" -> {
                url = InvokeConstant.SANDBOX_GATEWAY_URL
                defaultMerchant = "302300000582"
                defaultStore = "4023000003"
                defaultTerminalSN = "WTYG002313000143"
                defaultCurrency = "ZAR"
            }
            "AddPay-测试-Dave-PP35272108005595" -> {
                url = InvokeConstant.SANDBOX_GATEWAY_URL
                defaultMerchant = "302100091252"
                defaultStore = "4022000118"
                defaultTerminalSN = "PP35272108005595"
                defaultCurrency = "ZAR"
            }
            "AddPay-测试-L3 Test Merchant" -> {
                url = InvokeConstant.SANDBOX_GATEWAY_URL
                defaultMerchant = "302300027272"
                defaultStore = "4023000076"
                defaultTerminalSN = "PP35272204002841"
                defaultCurrency = "ZAR"
            }
            else -> {
                // 处理未知选项
                url = InvokeConstant.SANDBOX_GATEWAY_URL
                defaultMerchant = ""
                defaultStore = ""
                defaultTerminalSN = ""
                defaultCurrency = ""
            }
        }
        val intent = Intent(applicationContext, CloudActivity::class.java)
        intent.putExtra("URL", url)
        intent.putExtra("DEFAULT_MERCHANT", defaultMerchant)
        intent.putExtra("DEFAULT_STORE", defaultStore)
        intent.putExtra("DEFAULT_TERMINAL_SN", defaultTerminalSN)
        intent.putExtra("DEFAULT_CURRENCY", defaultCurrency)
        startActivity(intent)
    }

    override fun onConnect() {
        Log.e("Test", "onConnect")
        runOnUiThread {
            ll_layout1.visibility = View.VISIBLE
            tv_btn_8.visibility = View.VISIBLE
            Toast.makeText(this, "连接成功", Toast.LENGTH_LONG).show()
        }
        isConnected = true
    }

    override fun onDisconnect() {
        Log.e("Test", "onDisconnect")
        runOnUiThread {
            ll_layout1.visibility = View.GONE
            tv_btn_8.visibility = View.GONE
            Toast.makeText(this, "断开连接成功", Toast.LENGTH_LONG).show()
        }
        isConnected = false
    }

    override fun onError(errorCode: String?, errorMsg: String?) {
        Log.e("Test", "onError")
        isConnected = false
    }

    private val discoveredIpAddresses = mutableListOf<String>()


    override fun onServerFind(ip: String?, port: String?, deviceName: String?) {
        runOnUiThread {
            tv_btn_3.text =
                tv_btn_3.text.toString() + "\n" + "发现了设备" + ip + ":" + port + "设备名称：" + deviceName
            Toast.makeText(
                this,
                "发现了设备" + ip + ":" + port + "设备名称：" + deviceName,
                Toast.LENGTH_LONG
            ).show()
            this.ip = "ws://" + ip + ":" + port
            // 将发现的IP地址添加到列表中
            val ipAddress = this.ip
            discoveredIpAddresses.add(ipAddress)
//            mClient.autoConnect(this.ip)
            showIpAddressSelectionDialog()

        }
    }

    // 在用户选择连接时，弹出对话框供用户选择IP地址
    private fun showIpAddressSelectionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("选择要连接的IP地址")
        builder.setItems(discoveredIpAddresses.toTypedArray()) { dialog, which ->
            val selectedIp = discoveredIpAddresses[which]
            // 在这里使用选定的IP地址进行连接
            connectToServer(selectedIp)
        }
        builder.setNegativeButton("取消") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    // 用于连接服务器的函数
    private fun connectToServer(selectedIp: String) {
        // 在这里执行连接逻辑
        // 使用 selectedIp 变量来连接所选的IP地址
        mClient.autoConnect(selectedIp)
    }

}
