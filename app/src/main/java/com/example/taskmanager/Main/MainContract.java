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
        void updateTask(Task task);
        void deleteTask();
        void goToWelcomeActivity();
        void setEmptyStateVisibility(boolean visible , int theme);
        void setListEmptyStateVisibility(boolean visible , int theme);
    }

    interface presentor extends BasePresentor<view>{
        void validatingUserInfo();
        void listSwitch(boolean b);
        void onItemClicked(Task task);
        void drawerToggleClicked();
        void addTaskButtonClicked();
        void onCompletedListClicked();
        void onOutDatedListClicked();
        void cleareListClicked();
        void settingClicked();
        void onResume();
    }
}
