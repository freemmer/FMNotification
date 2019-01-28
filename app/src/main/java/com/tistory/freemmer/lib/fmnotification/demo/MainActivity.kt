package com.tistory.freemmer.lib.fmnotification.demo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import com.tistory.freemmer.lib.fmnotification.FMNotification
import com.tistory.freemmer.lib.fmnotification.FMNotificationAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FMNotificationAppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            FMNotification.getPushToken { isSuccessful: Boolean, token: String? ->
                if (!isSuccessful) {
                    Log.w(TAG, "getInstanceId failed")
                } else {
                    val msg = getString(R.string.msg_token_fmt, token)
                    Log.d(TAG, msg)
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onClickedNotification(bundle: Bundle) {
        // In case a notification message is tapped.
        // Handle possible data accompanying notification message.
        for (key in bundle.keySet()) {
            val value = bundle.get(key)
            Log.d(TAG, "Key: $key Value: $value")
        }
    }


}
