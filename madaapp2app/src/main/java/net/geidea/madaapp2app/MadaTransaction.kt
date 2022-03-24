package net.geidea.madaapp2app

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import net.geidea.madaapp2app.listener.TransactionStatusListener
import net.geidea.madaapp2app.models.MadaTransReq
import net.geidea.madaapp2app.models.TransType
import net.geidea.madaapp2app.ui.TransactionActivity
import net.geidea.madaapp2app.utils.Singleton.Companion.getInstance

class MadaTransaction private constructor(
    private val mActivity: Activity,
    private val mTransReq: MadaTransReq) {

    fun startTransaction() {
        val payIntent = Intent(mActivity, TransactionActivity::class.java)
        mTransReq.transType = TransType.PURCHASE
        payIntent.putExtra("trans_req_data", mTransReq)
        mActivity.startActivity(payIntent)
    }

    fun setPaymentStatusListener(mListener: TransactionStatusListener) {
        val singleton = getInstance()
        singleton!!.setListener(mListener)
    }

    fun detachListener() {
        getInstance()?.detachListener()
    }


    private fun isPackageInstalled(
        packageName: String,
        packageManager: PackageManager
    ): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun isDefaultAppExist(): Boolean {
        /*return if (mPayment.defaultPackage != null) {
            !isPackageInstalled(App2AppConstants.SUNMI_APP_PACKAGE_NAME, mActivity.packageManager)
        } else {
            Log.w(
                "UnSpecified", "Default app is not Specified. Specify it using " +
                        "'setDefaultApp()' method of Builder class"
            )
            false
        }*/
        return isPackageInstalled(App2AppConstants.MEEZA_MADA_PACKAGE_NAME, mActivity.packageManager)

    }

    class Init {
        private var activity: Activity? = null
        private var mTransReq: MadaTransReq = MadaTransReq()
        fun with(activity: Activity): Init {
            this.activity = activity
            return this
        }

        fun setMerchantId(merchantId: String): Init {
            check(merchantId.trim { it <= ' ' }.isNotEmpty()) { "Merchant ID Should be Valid!" }
            mTransReq.merchantId = merchantId
            return this
        }

        fun setTerminalId(terminalId: String): Init {
            check(terminalId.trim { it <= ' ' }.isNotEmpty()) { "Terminal ID Should be Valid!" }
            mTransReq.terminalId = terminalId
            return this
        }

        fun setTransType(transType: TransType): Init {
            mTransReq.transType = transType
            return this
        }

        fun setAmount(amount: String): Init {
            check(amount.trim { it <= ' ' }.isNotEmpty()) { "Transaction Amount Should be Valid!" }
            mTransReq.amount = amount
            return this
        }

        fun setOrderId(orderId: String): Init {
            mTransReq.orderId = orderId
            return this
        }

        fun setCustomerReceiptFlag(customerReceiptFlag: Boolean): Init {
            mTransReq.customerReceiptFlag = customerReceiptFlag
            return this
        }

        fun setHomeButtonStatus(homeButtonStatus: Boolean): Init {
            mTransReq.homeButtonStatus = homeButtonStatus
            return this
        }

        fun build(): MadaTransaction {
            checkNotNull(activity) { "Activity must be specified using with() call before build()" }
            checkNotNull(mTransReq) { "com.nandroidex.upipayments.models.Payment Details must be initialized before build()" }
            checkNotNull(mTransReq.terminalId) { "Must call setTerminalId() before build" }
            checkNotNull(mTransReq.merchantId) { "Must call setMerchantId() before build" }
            checkNotNull(mTransReq.amount) { "Must call setAmount() before build" }

            return MadaTransaction(activity!!, mTransReq)
        }
    }
}