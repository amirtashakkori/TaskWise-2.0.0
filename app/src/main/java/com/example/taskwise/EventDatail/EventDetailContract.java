package com.example.taskwise.EventDatail;

import com.example.taskwise.BasePresentor;
import com.example.taskwise.BaseView;
import com.example.taskwise.Model.Event;
import com.example.taskwise.Model.Task;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

public interface EventDetailContract {
    public interface view extends BaseView {
        void setTexts(int headerText, int buttonTv , boolean create);
        void showEvent(Event event);
        void setDeleteButtonVisibility(boolean visible);
        void setWorkManager(long id);
        void cancelWorkManger(Event event);
        void setAlarmManager(Event event);
        void cancelAlarmManager(long requestCode);
        void updateEvent();
        void deleteEvent();

    }

    public interface presentor extends BasePresentor<view> {
        void deleteButtonClicked();
        void saveButtonClicked(String title , long firstDate , long secondDate , String date , int notifyMe);
    }
}
