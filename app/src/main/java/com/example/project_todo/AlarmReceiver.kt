package com.example.project_todo

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.project_todo.entity.Task

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?:return
        intent?:return

        val task = intent.getParcelableExtra<Task>(Constants.TASK_EXTRA)?:return

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = Notification.Builder(context)
        builder.setSmallIcon(R.drawable.ic_action_save)
//            .setContentIntent(pendingIntent)
            .setContentText(task.text)
            .setContentTitle("It's time to complete the task")
            .setAutoCancel(true)
        builder.build().apply { notificationManager.notify(0, this) }
    }
}