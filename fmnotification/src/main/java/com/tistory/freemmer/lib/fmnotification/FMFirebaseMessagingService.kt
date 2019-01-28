package com.tistory.freemmer.lib.fmnotification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by freemmer on 23/01/2019.
 * History
 *    - 23/01/2019 Create file
 */
class FMFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Check if message contains a notification payload.
        remoteMessage?.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            FMNotification.instance(this).sendNotification((0..1000000).random(), it.title, it.body
                , remoteMessage.toIntent()?.extras)
            return
        }

        // Check if message contains a data payload.
        remoteMessage?.data?.isNotEmpty()?.let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            var title = "Undefined Title"
            var body = "Undefined Body"
            FMNotification.PAYLOAD_TITLE_KEY?.let { key ->
                title = remoteMessage.data[key] ?: "Empty Title"
            }
            FMNotification.PAYLOAD_BODY_KEY?.let { key ->
                body = remoteMessage.data[key] ?: "Empty Title"
            }
            FMNotification.instance(this).sendNotification((0..1000000).random(), title, body
                , remoteMessage.toIntent()?.extras)
            //FMJobService.scheduleJob(this, "FCM")
        }
    }


    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: $token")
    }

}

