package net.geidea.madaapp2app.utils

import net.geidea.madaapp2app.listener.TransactionStatusListener

class Singleton {
    private var listener: TransactionStatusListener? = null
    fun getListener(): TransactionStatusListener {
        return instance!!.listener!!
    }

    fun setListener(listener: TransactionStatusListener) {
        instance!!.listener = listener
    }

    val isListenerRegistered: Boolean
        get() = instance!!.listener != null

    fun detachListener() {
        instance!!.listener = null
    }

    companion object {
        private var instance: Singleton? = null
        fun getInstance(): Singleton? {
            if (instance == null) {
                instance =
                    Singleton()
            }
            return instance
        }
    }
}