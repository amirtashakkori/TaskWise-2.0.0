package com.example.taskmanager.TaskDetail;

import com.example.taskmanager.BasePresentor;
import com.example.taskmanager.BaseView;
import com.example.taskmanager.Model.Task;

public interface TaskDetailContract {
    interface view extends BaseView{
        void setTexts();
        void showTask(Task task);
        void setDeleteButtonVisibility(boolean visible);
    }

    interface presentor extends BasePresentor<view>{
        void deleteButtonClicked();
        void saveButtonClicked(String titel , String description , int timePeriod , int priority);
        void backButtonClicked();
    }
}
