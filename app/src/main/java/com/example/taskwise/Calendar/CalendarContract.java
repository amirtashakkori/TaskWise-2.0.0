package com.example.taskwise.Calendar;

import com.example.taskwise.BasePresentor;
import com.example.taskwise.BaseView;
import com.example.taskwise.Model.Event;

import java.util.List;

public interface CalendarContract {
    interface view extends BaseView{
        void showEvents(List<Event> events);
        void setEmptyStateVisibility(boolean visible);
    }
    interface presentor extends BasePresentor<view>{
        void onSearch(String query);
        void updateEvent(Event event);
    }
}
