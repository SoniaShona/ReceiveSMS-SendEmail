package com.example.exo2

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.provider.Telephony
import android.util.Log
import androidx.annotation.RequiresApi

class ReceiveSMS : BroadcastReceiver() {
    private val channelId = "channel-01"
    private val channelName = "SIL Channel"
    private val importance = NotificationManager.IMPORTANCE_HIGH
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("BroadcastReceiver", "onReceive")
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            Log.d("BroadcastReceiver", "SMS received")
            val num = intent.getStringArrayExtra("smsto:")
            composeEmail(num,"email intent",context)
            CreateNotifClick(context)

        }
    }


    fun composeEmail(addresses: Array<String>, subject: String,context: Context) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)

        }
        if (intent.resolveActivity(context.packageManager) != null){
            context.startActivity(intent)}

    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun CreateNotifClick( context: Context) {
        // notification principale


        val testIntent =Intent(context, MainActivity::class.java)
        val pTestIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), testIntent, 0)
        val notifIntent1 = Intent(context, MainActivity::class.java)
        val pNotifIntent1 = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), notifIntent1, 0)


        // Icones
        val icon1 = Icon.createWithResource(context, android.R.drawable.btn_minus)

        val action1 = Notification.Action.Builder(icon1, "Zoom", pNotifIntent1).build()


        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                channelId, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
        }

        val noti = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, channelId)
                .setContentTitle("Nouvelle Notification")
                .setContentText("Je viens de recevioir une notification !")
                .setSmallIcon(android.R.drawable.btn_dialog)
                .setContentIntent(pTestIntent)
                .addAction(action1)
                .setAutoCancel(true)

                .build()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        notificationManager.notify(0, noti)
    }

}