package net.geidea.madaapp2app.models

import java.io.Serializable

class MadaTransReq : Serializable {
    var merchantId: String? = null
    var terminalId: String? = null
    var transType: TransType? = null
    var customerReceiptFlag: Boolean? = null
    var orderId: String = ""
    var homeButtonStatus: Boolean? = false

    //PURCHASE
    var amount: String? = null
}