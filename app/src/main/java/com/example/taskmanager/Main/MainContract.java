package com.example.taskmanager.Main;

import com.example.taskmanager.BasePresentor;
import com.example.taskmanager.BaseView;
import com.example.taskmanager.Model.Task;

import java.util.List;

public interface MainContract {
    interface view extends BaseView{
        void setHeaderTexts(String name , int taskTime , int tasksCount);
        void setDate();
        void showTasks(List<Task> tasks);
        void goToWelcomeActivity();
        void setEmptyStateVisibility(boolean visible , int theme);
        void setListEmptyStateVisibility(boolean visible , int theme);
    }

    interface presentor extends BasePresentor<view>{
        void validatingUserInfo();
        void listSwitch(boolean b);
        void clearListClicked();
        void updateTask(Task task);
    }
}
