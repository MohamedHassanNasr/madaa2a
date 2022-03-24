package net.geidea.madaapp2app.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import net.geidea.madaapp2app.App2AppConstants
import net.geidea.madaapp2app.R
import net.geidea.madaapp2app.models.MadaTransReq
import net.geidea.madaapp2app.utils.Singleton
import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResult

import androidx.activity.result.ActivityResultCallback

import androidx.activity.result.contract.ActivityResultContracts

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import net.geidea.madaapp2app.models.TransactionDetails
import org.json.JSONObject


class TransactionActivity : AppCompatActivity() {

    private var singleton: Singleton? = null

    private val purchaseResultLauncher =
        registerForActivityResult(StartActivityForResult()) {
                result -> if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data

            if(data!=null){
                parsePurchaseResult(data)
            }else{
                callbackTransactionFailed(false,"Failed","","")
            }

        }else{
            callbackTransactionFailed(false,"Failed","","")
        }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        singleton = Singleton.getInstance()

        val transReq: MadaTransReq = intent.getSerializableExtra("trans_req_data") as MadaTransReq

        val paymentIntent = Intent()
        paymentIntent.action = App2AppConstants.MEEZA_MADA_PACKAGE_NAME+".PURCHASE"
        paymentIntent.putExtra("CUSTOMER_RECEIPT_FLAG",transReq.customerReceiptFlag)
        paymentIntent.putExtra("ORDER_ID", transReq.orderId)
        paymentIntent.putExtra("HOME_BUTTON_STATUS", transReq.homeButtonStatus)
        paymentIntent.putExtra(Intent.EXTRA_TEXT,transReq.amount)
        paymentIntent.type = "text/plain"

        if (paymentIntent.resolveActivity(packageManager) != null) {
            val shareIntent = Intent.createChooser(paymentIntent, "Purchase Transaction")
            purchaseResultLauncher.launch(shareIntent)
            callbackTransactionSubmitted()
        } else {
            Toast.makeText(
                this, "No MADA app found! Please Install to Proceed!",
                Toast.LENGTH_SHORT
            ).show()
            callbackOnAppNotFound()
        }

    }


    private fun isListenerRegistered(): Boolean {
        return Singleton.getInstance()!!.isListenerRegistered
    }

    private fun callbackOnAppNotFound() {
        Log.e("", "No UPI app found on device.")
        if (isListenerRegistered()) {
            singleton!!.getListener().onAppNotFound()
        }
        finish()
    }

    private fun callbackTransactionSuccess(transactionDetails: TransactionDetails) {
        if (isListenerRegistered()) {
            singleton!!.getListener().onTransactionSuccess(transactionDetails)
        }
        finish()
    }

    private fun callbackTransactionSubmitted() {
        if (isListenerRegistered()) {
            singleton!!.getListener().onTransactionSubmitted()
        }
    }

    private fun callbackTransactionFailed(isApproved : Boolean, status : String,
                                          reason : String,message : String) {
        if (isListenerRegistered()) {
            singleton!!.getListener().onTransactionFailed(isApproved,status,reason, message)
        }
        finish()
    }

    private fun getTransactionDetails(jsonObject: JSONObject,
                                      isApproved : Boolean,
                                      status: String): TransactionDetails {

        val rrn = jsonObject.optString("rrn")
        val orderId = jsonObject.optString("ORDER_ID")
        val pan = jsonObject.optString("pan")
        val stan = jsonObject.optString("stan")


        return TransactionDetails(orderId, pan, stan,
            isApproved,status, rrn)
    }

    private fun parsePurchaseResult(data: Intent){
        val status = data.getStringExtra("status")
        val reason = data.getStringExtra("reason")
        var isApproved = false
        if (status.equals("Approved") || status.equals("Declined")) {
            isApproved = status.equals("Approved")
            val result :String? = data.getStringExtra("result")
            Log.e("AppToApp","data==>"+result)
            val stringBuilder = StringBuilder()
            try {
                if (result != null) {
                    val jsonObject = JSONObject(result)

                    callbackTransactionSuccess(getTransactionDetails(jsonObject,isApproved,status!!))
                } else {
                    status?.let {

                        callbackTransactionFailed(isApproved,status,reason!!,"")
                    }
                }
            } catch (e: Exception) {
                status?.let {
                    callbackTransactionFailed(isApproved,status,reason!!,"")
                }
            }
        } else {
            val reason = data.getStringExtra("reason")
            reason?.let {
                callbackTransactionFailed(isApproved,status!!,reason,"")
            }
        }
    }
}