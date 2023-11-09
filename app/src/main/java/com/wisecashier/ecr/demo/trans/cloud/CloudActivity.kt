package com.wisecashier.ecr.demo.trans.cloud

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.wisecashier.ecr.demo.R
import kotlinx.android.synthetic.main.activity_cloud.*
import kotlinx.android.synthetic.main.activity_cloud.tv_btn_black

class CloudActivity : Activity(){

    var isConnected: Boolean = false



    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloud)


        val url = intent.getStringExtra("URL")
        val defaultMerchant = intent.getStringExtra("DEFAULT_MERCHANT")
        val defaultStore = intent.getStringExtra("DEFAULT_STORE")
        val defaultTerminalSN = intent.getStringExtra("DEFAULT_TERMINAL_SN")
        val defaultCurrency = intent.getStringExtra("DEFAULT_CURRENCY")
        val merchantEditText = findViewById<EditText>(R.id.edit_input_merchant)
        val storeEditText = findViewById<EditText>(R.id.edit_input_store)
        val terminalSNEditText = findViewById<EditText>(R.id.edit_input_terminal_sn)
        val currencyEditText = findViewById<EditText>(R.id.edit_input_currency)
        // 使用传递的参数来设置默认值
        merchantEditText.setText(defaultMerchant)
        storeEditText.setText(defaultStore)
        terminalSNEditText.setText(defaultTerminalSN)
        currencyEditText.setText(defaultCurrency)
        Log.e("parameter" , "url -->> [$url]  " +
                "merchant -->> [$defaultMerchant]  " +
                "store -->> [$defaultStore]  " +
                "sn --> [$defaultTerminalSN]  " +
                "defaultCurrency --> [$defaultCurrency]  " )
        // 初始化SharedPreferences，使用应用的包名作为名称
        val sharedPreferences = getSharedPreferences(packageName, MODE_PRIVATE)


        //保存参数
        tv_btn_copy.setOnClickListener{
            if (!isConnected) {
                val priceCurrency = edit_input_currency.text.toString()
                val terminalSn = edit_input_terminal_sn.text.toString()
                val storeNo = edit_input_store.text.toString()
                val merchantNo = edit_input_merchant.text.toString()

                // 保存用户输入的值到SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putString("price_currency", priceCurrency)
                editor.putString("terminal_sn", terminalSn)
                editor.putString("store_no", storeNo)
                editor.putString("merchant_no", merchantNo)
                editor.putString("url", url)

                editor.apply()
                onConnect()
                // 提示用户保存成功或执行其他操作
                runOnUiThread {
                    Toast.makeText(this, "参数已保存", Toast.LENGTH_SHORT).show()
                }
            }
            return@setOnClickListener

        }

        //PURCHASE
        tv_btn_2.setOnClickListener{
            if (!isConnected) {
                runOnUiThread {
                    Toast.makeText(this, "未保存参数", Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }
            startActivity(Intent(applicationContext, CloudPurchaseActivity::class.java))
        }

        //Refund
        tv_btn_refund.setOnClickListener{
            if (!isConnected) {
                runOnUiThread {
                    Toast.makeText(this, "未保存参数", Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }
            startActivity(Intent(applicationContext, CloudRefundActivity::class.java))
        }

        //Void
        tv_btn_void.setOnClickListener{
            if (!isConnected) {
                runOnUiThread {
                    Toast.makeText(this, "未保存参数", Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }
            startActivity(Intent(applicationContext, CloudVoidActivity::class.java))
        }

        //PerAuth
        tv_btn_per.setOnClickListener{
            if (!isConnected) {
                runOnUiThread {
                    Toast.makeText(this, "未保存参数", Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }
            startActivity(Intent(applicationContext, CloudPerAuthActivity::class.java))
        }

        //PerAuth-complete
        tv_btn_complete.setOnClickListener{
            if (!isConnected) {
                runOnUiThread {
                    Toast.makeText(this, "未保存参数", Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }
            startActivity(Intent(applicationContext, CloudPerAuthCompleteActivity::class.java))
        }

        //PerAuth-cancel
        tv_btn_per_cancel.setOnClickListener{
            if (!isConnected) {
                runOnUiThread {
                    Toast.makeText(this, "未保存参数", Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }
            startActivity(Intent(applicationContext, CloudPerAuthCancelActivity::class.java))
        }

        //PerAuth-complete-refund
        tv_btn_per_complete_refund.setOnClickListener{
            if (!isConnected) {
                runOnUiThread {
                    Toast.makeText(this, "未保存参数", Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }
            startActivity(Intent(applicationContext, CloudPerAuthCompleteRefundActivity::class.java))
        }

        //Query
        tv_btn_query.setOnClickListener{
            if (!isConnected) {
                runOnUiThread {
                    Toast.makeText(this, "未保存参数", Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }
            startActivity(Intent(applicationContext, CloudQueryActivity::class.java))
        }

        //Close
        tv_btn_close.setOnClickListener{
            if (!isConnected) {
                runOnUiThread {
                    Toast.makeText(this, "未保存参数", Toast.LENGTH_LONG).show()
                }
                return@setOnClickListener
            }
            startActivity(Intent(applicationContext, CloudCloseActivity::class.java))
        }


        //返回
        tv_btn_black.setOnClickListener{
            finish()
        }

    }

    fun onConnect() {
        Log.e("Test", "onConnect")
        runOnUiThread {
            Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show()
        }
        isConnected = true
    }

    fun onDisconnect() {
        Log.e("Test", "onDisconnect")
        isConnected = false
    }

}