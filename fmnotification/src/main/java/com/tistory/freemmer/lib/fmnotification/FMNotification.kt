package com.tistory.freemmer.lib.fmnotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
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

    fun sendNotification(title: String?, body: String?, channelId: String = context.getString(R.string.default_notification_channel_id)) {
        val packageName = context.applicationContext.packageName
        val ai = context.applicationContext.packageManager.getApplicationInfo(packageName
            , PackageManager.GET_META_DATA)

        val intent: Intent
        intent = if (TARGET_LAUNCH_CLASS != null) {
            Intent(context, TARGET_LAUNCH_CLASS)
        } else {
            context.packageManager.getLaunchIntentForPackage(packageName) as Intent
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)
        //val channelId = context.getString(R.string.default_notification_channel_id)
        //val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val smallIcon: Int
        if (SMALL_ICON < 0) {
            // pushIcon meta 정보 있는지 확인

            val aBundle = ai.metaData
            if (aBundle.get("pushIcon") != null) {
                val split =
                    (aBundle.get("pushIcon") as String).split("/".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                var name = split[split.size - 1]
                name = name.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                smallIcon = context.resources.getIdentifier(name, "drawable", packageName)
            } else {
                // 없으면 AppIcon 정보 가져옴
                smallIcon = ai.icon
            }
        } else {
            smallIcon =SMALL_ICON
        }
        val xx = title ?: ai.name
        val yy = context.applicationContext.applicationInfo
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIcon)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setContentTitle(title.takeIf { title != null } ?: ai.name)

        //if (title != null) notificationBuilder.setContentTitle(title)
        if (body != null) notificationBuilder.setContentText(body)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(channelId,
//                "Channel human readable title",
//                NotificationManager.IMPORTANCE_DEFAULT)
//            notificationManager.createNotificationChannel(channel)
//        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}