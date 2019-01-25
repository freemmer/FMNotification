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

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        // EX) From: 793976323963
        Log.d(TAG, "From: ${remoteMessage?.from}")

        // Check if message contains a data payload.
        remoteMessage?.data?.isNotEmpty()?.let {
            // EX) Message data payload: {}
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            FMJobService.scheduleJob(this, "FCM")
        }

        //(FE): session_start(_s)
        //      , Bundle[{ firebase_event_origin(_o)=auto
        //                  , firebase_screen_class(_sc)=MainActivity
        //                  , firebase_screen_id(_si)=1460929196528696177 }]
        //(FE): notification_receive(_nr)
        //      , Bundle[{ firebase_event_origin(_o)=fcm
        //                  , firebase_screen_class(_sc)=MainActivity
        //                  , firebase_screen_id(_si)=1460929196528696177
        //                  , message_device_time(_ndt)=0, message_name(_nmn)=알림 라벨
        //                  , message_time(_nmt)=1548311863
        //                  , message_id(_nmid)=4903927122438645137 }]
        //(FE): notification_foreground(_nf)
        //      , Bundle[{ firebase_event_origin(_o)=fcm
        //                  , firebase_screen_class(_sc)=MainActivity
        //                  , firebase_screen_id(_si)=1460929196528696177
        //                  , message_device_time(_ndt)=0
        //                  , message_name(_nmn)=알림 라벨
        //                  , message_time(_nmt)=1548311863
        //                  , message_id(_nmid)=4903927122438645137 }]

        // Check if message contains a notification payload.
        remoteMessage?.notification?.let {
            // EX) Message Notification Body:  알림 텍스트
            Log.d(TAG, "Message Notification Body: ${it.body}")
            FMNotification.instance(this).sendNotification(it.title, it.body)
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: $token")
    }

}

