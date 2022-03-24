package net.geidea.madaapp2app.utils

import android.app.Activity

class MadaApp private constructor(
    private val mActivity: Activity, private val merchantId: String,
    private val terminalId: String) {


    class Init {
        private var activity: Activity? = null
        private var merchantId: String? =null
        private var terminalId: String? =null
        fun with(activity: Activity): Init {
            this.activity = activity
            return this
        }

        fun setMerchantId(merchantId: String): Init {
            check(merchantId.trim { it <= ' ' }.isNotEmpty()) { "Merchant ID Should be Valid!" }
            this.merchantId = merchantId
            return this
        }

        fun setTerminalId(terminalId: String): Init {
            check(terminalId.trim { it <= ' ' }.isNotEmpty()) { "Terminal ID Should be Valid!" }
            this.terminalId = terminalId
            return this
        }


        fun build(): MadaApp {
            checkNotNull(activity) { "Activity must be specified using with() call before build()" }
            checkNotNull(terminalId) { "Must call setTerminalId() before build" }
            checkNotNull(merchantId) { "Must call setMerchantId() before build" }
            return MadaApp(activity!!, merchantId!!,terminalId!!)
        }
    }
}