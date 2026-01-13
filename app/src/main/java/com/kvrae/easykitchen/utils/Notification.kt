package com.kvrae.easykitchen.utils


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import com.kvrae.easykitchen.R

const val NOTIFICATION_ID = 1
const val CHANNEL_ID = "channel1"
const val TITLE_EXTRA = "channel1"
const val MESSAGE_EXTRA = "channel1"
class Notification: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification : Notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(TITLE_EXTRA))
            .setContentText(intent.getStringExtra(MESSAGE_EXTRA))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}
@SuppressLint("ScheduleExactAlarm")
@RequiresPermission("android.permission.SCHEDULE_EXACT_ALARM")
fun scheduleNotification(
    context: Context,
    title: String,
    message : String

){
    try {
        val intent = Intent(context, Notification::class.java)
        intent.putExtra(TITLE_EXTRA, title)
        intent.putExtra(MESSAGE_EXTRA, message)

        val pendingIntent= PendingIntent.getBroadcast(
            context,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = System.currentTimeMillis() + 5000

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    } catch (e: Exception){
        e.printStackTrace()
    }
}
fun createNotificationChannel(
    name: String ="Notif Channel",
    description: String = "Notification Description",
    context: Context
){
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel("",name,importance)
    channel.description = description
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}