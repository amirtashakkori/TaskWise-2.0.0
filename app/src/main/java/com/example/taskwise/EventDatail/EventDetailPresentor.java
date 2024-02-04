package com.example.taskwise.EventDatail;

import android.widget.Toast;

import com.example.taskmanager.R;
import com.example.taskwise.DataBase.DBDao;
import com.example.taskwise.Model.Event;
import com.example.taskwise.SharedPreferences.AppSettingContainer;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class EventDetailPresentor implements EventDetailContract.presentor{

    EventDetailContract.view view;
    DBDao dao;
    Event event;
    AppSettingContainer settingContainer;

    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

    public EventDetailPresentor(DBDao dao , Event event , AppSettingContainer settingContainer){
        this.dao = dao;
        this.event = event;
        this.settingContainer = settingContainer;
    }

    @Override
    public void onAttach(EventDetailContract.view view) {
        this.view = view;
        if (event != null){
            view.setDeleteButtonVisibility(true);
            view.showEvent(event);
            view.setTexts(R.string.editEvent , R.string.saveChanges , false);
        } else {
            view.setDeleteButtonVisibility(false);
            view.setTexts(R.string.createNewEvent , R.string.createEvent , true);
        }
    }

    @Override
    public void onDetach() {
        this.view = null;
    }

    @Override
    public void deleteButtonClicked() {
        if (event != null){
            int result = dao.deleteEvent(event);
            if (result > 0)
                view.deleteEvent();
        }
    }

    @Override
    public void saveButtonClicked(String title, long firstDate , long secondDate , String date , int notifyMe ) {
        if (event != null){
            event.setTitle(title);
            event.setFirstDate(firstDate);
            event.setSecondDate(secondDate);
            event.setDate(date);
            event.setNotifyMe(notifyMe);

            if (firstDate - System.currentTimeMillis() < 0 && secondDate - System.currentTimeMillis() < 0) event.setOutdated(true);
            else event.setOutdated(false);

            int result = dao.update(event);

            if (result > 0) {
                view.updateEvent();

                if (firstDate > System.currentTimeMillis()) {
                    if (settingContainer.isEventNotificationEnabled()){
                        view.cancelAlarmManager(event.getId());
                        view.setAlarmManager(event);
                    }

                }

                if (secondDate > System.currentTimeMillis()) {
                    view.cancelWorkManger(event);
                    view.setWorkManager(event.getId());
                }
            }
        } else {
            event = new Event();
            event.setTitle(title);
            event.setFirstDate(firstDate);
            event.setSecondDate(secondDate);
            event.setDate(date);
            event.setNotifyMe(notifyMe);

            if (firstDate - System.currentTimeMillis() < 0 && secondDate - System.currentTimeMillis() < 0) event.setOutdated(true);
            else event.setOutdated(false);

            if (firstDate > System.currentTimeMillis()) {
                if (settingContainer.isEventNotificationEnabled())
                    view.setAlarmManager(event);
            }

            long id = dao.addEvent(event);

            if (secondDate > System.currentTimeMillis())
                view.setWorkManager(id);

        }
    }

}
