package com.tistory.freemmer.lib.fmnotification

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.tistory.freemmer.lib.fmnotification.util.FMDeviceUtil
import java.lang.ref.WeakReference

/**
 * Created by freemmer on 23/01/2019.
 * History
 *    - 23/01/2019 Create file
 */
class FMNotification private constructor(
    private val context: Context
) {
    companion object {
        var TARGET_LAUNCH_CLASS: Class<*>? = null
        var SMALL_ICON: Int = -1
        var PAYLOAD_TITLE_KEY: String? = null
        var PAYLOAD_BODY_KEY: String? = null
        private var weakReference: WeakReference<FMNotification>? = null

        fun initialize(context: Context): FMNotification {
            return instance(context)
                .createChannel(context.getString(R.string.default_notification_channel_id)
                    , context.getString(R.string.default_notification_channel_name))
        }

        fun instance(context: Context): FMNotification {
            if (weakReference?.get() == null) {
                weakReference = WeakReference(FMNotification(context))
            }
            return weakReference?.get()!!
        }

        fun getPushToken(pFunc:(isSuccessful: Boolean, token: String?) -> Unit) {
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener { task ->
                    pFunc(task.isSuccessful, task.result?.token)
                }
        }
    }


    fun createChannel(channelId: String, channelName: String
                      , importance: Int = NotificationManager.IMPORTANCE_HIGH
                      , enableLights: Boolean = true
                      , enableVibration: Boolean = true
                      , lightColor: Int = Color.GRAY
                      , lockScreenVisibility: Int = Notification.VISIBILITY_PUBLIC): FMNotification
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.enableLights(enableLights)
            channel.enableVibration(enableVibration)
            channel.lightColor = lightColor
            channel.lockscreenVisibility = lockScreenVisibility
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
        return this
    }

    fun sendNotification(notificationId: Int, title: String?, body: String?
                         , bundle: Bundle? = null
                         , channelId: String = context.getString(R.string.default_notification_channel_id)) {
        val targetIntent: Intent = if (TARGET_LAUNCH_CLASS != null) {
            Intent(context, TARGET_LAUNCH_CLASS)
        } else {
            FMDeviceUtil.instance(context).getLaunchIntent()
        }
        targetIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        bundle?.let { targetIntent.putExtras(it) }

        val pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, targetIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val smallIcon: Int
        if (SMALL_ICON < 0) {
            // pushIcon meta 정보 있는지 확인
            val aBundle = FMDeviceUtil.instance(context).getMetaData()
            if (aBundle.get("pushIcon") != null) {
                val split =
                    (aBundle.get("pushIcon") as String).split("/".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                var name = split[split.size - 1]
                name = name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                smallIcon = FMDeviceUtil.instance(context).getResourceID(name, "drawable")
            } else {
                // 없으면 AppIcon 정보 가져옴
                smallIcon = FMDeviceUtil.instance(context).getApplicationInfo().icon
            }
        } else {
            smallIcon =SMALL_ICON
        }
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIcon)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setContentTitle(title.takeIf { title != null } ?: FMDeviceUtil.instance(context).getAppLabel())
        body?.let { notificationBuilder.setContentText(it) }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

}



interface FMINotification {
    fun onClickedNotification(bundle: Bundle)
}


abstract class FMNotificationActivity: Activity(), FMINotification {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.let {
            onClickedNotification(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        intent.extras?.let {
            onClickedNotification(it)
        }
    }
}

abstract class FMNotificationAppCompatActivity: AppCompatActivity(), FMINotification {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.let {
            onClickedNotification(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        intent.extras?.let {
            onClickedNotification(it)
        }
    }
}

abstract class FMNotificationFragmentActivity: FragmentActivity(), FMINotification {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.let {
            onClickedNotification(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        intent.extras?.let {
            onClickedNotification(it)
        }
    }
}

