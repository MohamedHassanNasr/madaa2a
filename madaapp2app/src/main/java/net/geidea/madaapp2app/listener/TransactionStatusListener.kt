package net.geidea.madaapp2app.listener

import net.geidea.madaapp2app.models.TransactionDetails

interface TransactionStatusListener {
    fun onTransactionSuccess(transactionDetails: TransactionDetails)
    fun onTransactionSubmitted()
    fun onTransactionFailed(isApproved : Boolean, status : String,
                            reason : String,message : String)
    fun onAppNotFound()
}