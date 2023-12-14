package com.example.taskwise.EventDatail;

import com.example.taskmanager.R;
import com.example.taskwise.DataBase.EventDao;
import com.example.taskwise.Model.Event;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventDetailPresentor implements EventDetailContract.presentor{

    EventDetailContract.view view;
    EventDao dao;
    Event event;

    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

    public EventDetailPresentor(EventDao dao , Event event){
        this.dao = dao;
        this.event = event;
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
            int result = dao.delete(event);
            if (result > 0)
                view.deleteEvent();
        }
    }

    @Override
    public void saveButtonClicked(String title, long firstDate , long secondDate , String date , int notifyMe) {
        if (event != null){
            event.setTitle(title);
            event.setFirstDate(firstDate);
            event.setSecondDate(secondDate);
            event.setDate(sdf.format(firstDate));
            event.setNotifyMe(notifyMe);
            int result = dao.update(event);
            if (result > 0) {
                view.updateEvent();
            }
        } else {
            event = new Event();
            event.setTitle(title);
            event.setFirstDate(firstDate);
            event.setSecondDate(secondDate);
            event.setDate(sdf.format(firstDate));
            event.setNotifyMe(notifyMe);
            dao.addEvent(event);
            view.setAlarmManager(title , firstDate , notifyMe);
        }
    }

}
