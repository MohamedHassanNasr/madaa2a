package net.geidea.madaapp2app

import android.content.Context
import android.content.pm.PackageManager

object MadaApp2App{

    fun isMadaInstalled(context: Context): Boolean {
        val pm = context.packageManager
        try {
            pm.getPackageInfo(App2AppConstants.SUNMI_APP_PACKAGE_NAME, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }
}