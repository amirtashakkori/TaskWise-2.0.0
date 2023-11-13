package com.example.taskmanager.TaskDetail;

import com.example.taskmanager.BasePresentor;
import com.example.taskmanager.BaseView;
import com.example.taskmanager.Model.Task;

public interface TaskDetailContract {
    interface view extends BaseView{
        void setTexts(int headerText, int buttonTv);
        void showTask(Task task);
        void setDeleteButtonVisibility(boolean visible);
        void setWorkManager(String taskTitle , int expiredDate);
        void updateTask();
        void deleteTask();
    }

    interface presentor extends BasePresentor<view>{
        void deleteButtonClicked();
        void saveButtonClicked(String title , String description , int timePeriod , int priority);
    }
}
