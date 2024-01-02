package com.example.taskwise.BroadCastReceivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService;

import com.example.taskmanager.R;
import com.example.taskwise.ApplicationClass;

import java.util.Random;

public class Remiders extends BroadcastReceiver {

    String eventTitle;
    long eventId;
    String taskTitle , taskDescription;

    @Override
    public void onReceive(Context context, Intent intent) {

        eventTitle = intent.getStringExtra("eventTitle");
        eventId = intent.getLongExtra("eventId" , -1);
        taskTitle = intent.getStringExtra("taskTitle");
        taskDescription = intent.getStringExtra("taskDescription");

        if (eventId != -1 && eventTitle != null){
            showEventNotification(context , eventId , eventTitle);
        }
        if (taskTitle != null && taskDescription != null){
            showTaskNotification(context , taskTitle , taskDescription);
        }
    }

    public void showEventNotification(Context context , long eventId, String eventTitle){
        Notification notification = new NotificationCompat.Builder(context, ApplicationClass.eventReminderChannel)
                .setSmallIcon(R.drawable.ic_event)
                .setContentTitle(eventTitle)
                .setContentText(context.getString(R.string.eventNotificationDescription))
                .setAutoCancel(true)
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify((int) eventId, notification);
    }

    public void showTaskNotification(Context context , String taskTitle , String taskDescription){
        Notification notification = new NotificationCompat.Builder(context, ApplicationClass.taskReminderChannel)
                .setSmallIcon(R.drawable.ic_task_alt)
                .setContentTitle(taskTitle)
                .setContentText(context.getString(R.string.taskNotificationDescription))
                .setAutoCancel(true)
                .build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(new Random().nextInt() , notification);
    }

}
