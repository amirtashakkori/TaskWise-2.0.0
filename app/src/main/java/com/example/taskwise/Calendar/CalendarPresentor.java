package com.example.taskwise.Calendar;

import com.example.taskwise.BaseView;
import com.example.taskwise.DataBase.DBDao;
import com.example.taskwise.Model.Event;

import java.util.List;

public class CalendarPresentor implements CalendarContract.presentor {

    CalendarContract.view view;
    DBDao dao;
    List<Event> events;

    public CalendarPresentor(DBDao dao , String query) {
        this.dao = dao;
        events = dao.getDateEventList(query);
    }


    @Override
    public void onAttach(CalendarContract.view view) {
        this.view = view;
        if (!events.isEmpty()){
            view.showEvents(events);
            view.setEmptyStateVisibility(false);
        } else
            view.setEmptyStateVisibility(true);
    }

    @Override
    public void onDetach() {
        this.view = null;
    }

    @Override
    public void onSearch(String query) {
        if (query != null){
            events = dao.getDateEventList(query);
            if (!events.isEmpty()){
                view.showEvents(events);
                view.setEmptyStateVisibility(false);
            } else {
                view.setEmptyStateVisibility(true);
            }
        }
    }

    @Override
    public void deleteEvent(Event event , String query) {
        dao.deleteEvent(event);
        events = dao.getDateEventList(query);
        if (events.isEmpty())
            view.setEmptyStateVisibility(true);
    }
}
