package com.tistory.freemmer.lib.fmnotification.demo

import android.app.Application
import com.tistory.freemmer.lib.fmnotification.FMNotification

/**
 * Created by freemmer on 28/01/2019.
 * History
 *    - 28/01/2019 Create file
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // When only payload type is used like 'GCM'
        FMNotification.PAYLOAD_TITLE_KEY = "title"
        FMNotification.PAYLOAD_BODY_KEY = "body"

        // In case automatically create default channel
        FMNotification.initialize(this)
        // In case manually create default channel
        //FMNotification.instance(this).createChannel("default_channel_id", "Default Channel")
    }

}