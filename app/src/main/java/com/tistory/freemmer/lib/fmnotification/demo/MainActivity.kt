package com.tistory.freemmer.lib.fmnotification.demo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.tistory.freemmer.lib.fmnotification.FMNotification
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val yy = applicationContext.applicationInfo
        // packageManager.getApplicationLabel(applicationInfo)
        val a1 = applicationContext.packageManager.getApplicationLabel(yy)
        val a3 =applicationContext.applicationInfo.loadLabel(applicationContext.packageManager)
        // getApplicationInfo().loadLabel(getPackageManager()).toString();

        // In case automatically create default channel
        FMNotification.initialize(this)
        // In case manually create default channel
        //FMNotification.instance(this).createChannel("default_channel_id", "Default Channel")

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras.get(key)
                Log.d(TAG, "Key: $key Value: $value")
            }
        }

        subscribeButton.setOnClickListener {
            Log.d(TAG, "Subscribing to weather topic")
            FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener { task ->
                    var msg = getString(R.string.msg_subscribed)
                    if (!task.isSuccessful) {
                        msg = getString(R.string.msg_subscribe_failed)
                    }
                    Log.d(TAG, msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                }
        }

        logTokenButton.setOnClickListener {
            // Get token
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token

                    // Log and toast
                    val msg = getString(R.string.msg_token_fmt, token)
                    Log.d(TAG, msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                })
        }
    }

    companion object {

        private const val TAG = "MainActivity"
    }
}
