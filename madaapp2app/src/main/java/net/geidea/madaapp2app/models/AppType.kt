package net.geidea.madaapp2app.models

import androidx.annotation.Keep

@Keep
enum class AppType {
    PACKING, CONNECTING, SENDING, WAITING, RECEVING, UNPACKING, TRANS_RESULT, RETRY
}