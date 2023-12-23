package com.example.taskwise.Main;

import androidx.annotation.Nullable;

import com.example.taskwise.BasePresentor;
import com.example.taskwise.BaseView;
import com.example.taskwise.Model.Event;
import com.example.taskwise.Model.Task;

import java.util.List;

public interface MainContract {
    interface view extends BaseView{
        void setHeaderTexts(String name , int plans);
        void setNavigationDrawerText(String fullName , String expertise);
        void setDate();
        void showTasks(List<Task> tasks);
        void showEvents(List<Event> events);
        void goToWelcomeActivity();
        void setTaskEmptyStateVisibility(boolean visible);
        void setEventEmptyStateVisibility(boolean visible);
    }

    interface presentor extends BasePresentor<view>{
        void validatingUserInfo();
        void clearTaskListClicked();
        void clearEventListClicker();
        void switchTab(int tabPosition);
        void updateTask(Task task);
    }
}
