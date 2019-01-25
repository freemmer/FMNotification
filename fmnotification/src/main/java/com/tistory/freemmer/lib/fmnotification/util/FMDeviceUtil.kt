package com.tistory.freemmer.lib.fmnotification.util

import android.content.Context
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



}

