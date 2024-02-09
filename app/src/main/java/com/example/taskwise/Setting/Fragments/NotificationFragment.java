package com.example.taskwise.Setting.Fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskwise.ApplicationClass;
import com.example.taskwise.SharedPreferences.AppSettingContainer;


public class NotificationFragment extends Fragment {

    View view;

    TextView headerTv;
    RelativeLayout backBtn;
    LinearLayout channelNotification;
    SwitchCompat notificationSwitch , tasksNotificationSwitch , eventsNotificationSwitch;

    boolean appNotification;
    NotificationManager manager;
    AppSettingContainer settingContainer;

    public void cast(){
        headerTv = view.findViewById(R.id.headerTv);
        backBtn = view.findViewById(R.id.backBtn);
        notificationSwitch = view.findViewById(R.id.notificationSwitch);
        tasksNotificationSwitch = view.findViewById(R.id.tasksNotificationSwitch);
        eventsNotificationSwitch = view.findViewById(R.id.eventsNotificationSwitch);
        channelNotification = view.findViewById(R.id.channelNotification);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        cast();
        settingContainer = new AppSettingContainer(getContext());

        manager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationAvailability();

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    settingContainer.isTaskNotificationEnable(true);
                    settingContainer.isEventNotificationEnable(true);
                    channelNotification.setVisibility(View.VISIBLE);
                    notificationAvailability();
                } else {
                    settingContainer.isTaskNotificationEnable(false);
                    settingContainer.isEventNotificationEnable(false);
                    channelNotification.setVisibility(View.GONE);
                    notificationAvailability();
                }
            }
        });

        eventsNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    settingContainer.isEventNotificationEnable(true);
                    notificationAvailability();
                } else {
                    settingContainer.isEventNotificationEnable(false);
                    notificationAvailability();
                }
            }
        });

        tasksNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    settingContainer.isTaskNotificationEnable(true);
                    notificationAvailability();
                } else {
                    settingContainer.isTaskNotificationEnable(false);
                    notificationAvailability();
                }
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        return view;
    }

    public void notificationAvailability(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(!TextUtils.isEmpty(ApplicationClass.taskReminderChannel) && !TextUtils.isEmpty(ApplicationClass.eventReminderChannel)) {

                boolean isEventNotificationAvailable = settingContainer.isEventNotificationEnabled();
                boolean isTaskNotificationAvailable = settingContainer.isTaskNotificationEnabled();

                if (!isEventNotificationAvailable && !isTaskNotificationAvailable) {
                    tasksNotificationSwitch.setChecked(false);
                    eventsNotificationSwitch.setChecked(false);
                    notificationSwitch.setChecked(false);
                    channelNotification.setVisibility(View.GONE);
                } else if (isEventNotificationAvailable && !isTaskNotificationAvailable){
                    tasksNotificationSwitch.setChecked(false);
                    eventsNotificationSwitch.setChecked(true);
                    notificationSwitch.setChecked(true);
                } else if (!isEventNotificationAvailable && isTaskNotificationAvailable){
                    tasksNotificationSwitch.setChecked(true);
                    eventsNotificationSwitch.setChecked(false);
                    notificationSwitch.setChecked(true);
                } else {
                    tasksNotificationSwitch.setChecked(true);
                    eventsNotificationSwitch.setChecked(true);
                    notificationSwitch.setChecked(true);
                }
            }
        }
    }
}