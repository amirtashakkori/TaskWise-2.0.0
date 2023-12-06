package com.example.taskwise;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.example.taskmanager.R;

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel eventChannel = new NotificationChannel("eventReminder" , "Event Reminder" , NotificationManager.IMPORTANCE_HIGH);
            NotificationChannel taskChannel = new NotificationChannel("eventReminder" , "Event Reminder" , NotificationManager.IMPORTANCE_HIGH);
            eventChannel.setDescription(getString(R.string.eventChannel));
            taskChannel.setDescription(getString(R.string.taskChannel));
            manager.createNotificationChannel(taskChannel);
            manager.createNotificationChannel(eventChannel);
        }

    }
}
