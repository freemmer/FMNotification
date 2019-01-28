package com.tistory.freemmer.lib.fmnotification.util

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import java.lang.ref.WeakReference

/**
 * Created by freemmer on 25/01/2019.
 * History
 *    - 25/01/2019 Create file
 */
class FMDeviceUtil private constructor(
    val context: Context
){
    companion object {
        private var weakReference: WeakReference<FMDeviceUtil>? = null

        fun instance(context: Context): FMDeviceUtil {
            if (weakReference?.get() == null) {
                weakReference = WeakReference(FMDeviceUtil(context))
            }
            return weakReference?.get()!!
        }
    }


    fun getPackageName(): String {
        return context.applicationContext.packageName
    }

    fun getApplicationInfo(): ApplicationInfo {
        return context.applicationContext.applicationInfo
    }

    fun getAppLabel() : String {
        return getApplicationInfo().loadLabel(context.applicationContext.packageManager).toString()
    }

    fun getMetaData(): Bundle {
        return context.applicationContext.packageManager
            .getApplicationInfo(getPackageName() , PackageManager.GET_META_DATA).metaData
    }

    fun getLaunchIntent(): Intent {
        return context.packageManager.getLaunchIntentForPackage(getPackageName()) as Intent
    }

    fun getResourceID(variableName:String, resourceType: String): Int {
        return context.resources.getIdentifier(variableName, resourceType, getPackageName())
    }



}

