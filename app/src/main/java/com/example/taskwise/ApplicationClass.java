package com.example.taskwise;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import com.example.taskmanager.R;

public class ApplicationClass extends Application {

    public static final String eventReminderChannel = "eventReminder";
    public static final String taskReminderChannel = "taskReminder";

    @Override
    public void onCreate() {
        super.onCreate();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            NotificationChannel eventChannel = new NotificationChannel(eventReminderChannel , "Event Reminder" , NotificationManager.IMPORTANCE_HIGH);
            eventChannel.setDescription(getString(R.string.eventChannel));
            manager.createNotificationChannel(eventChannel);

            NotificationChannel taskChannel = new NotificationChannel(taskReminderChannel , "Task Reminder" , NotificationManager.IMPORTANCE_HIGH);
            taskChannel.setDescription(getString(R.string.taskChannel));
            manager.createNotificationChannel(taskChannel);

        }

    }
}
