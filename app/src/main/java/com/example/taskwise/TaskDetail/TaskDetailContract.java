package com.example.taskwise.TaskDetail;

import com.example.taskwise.BasePresentor;
import com.example.taskwise.BaseView;
import com.example.taskwise.Model.Task;

public interface TaskDetailContract {
    interface view extends BaseView{
        void setTexts(int headerText, int buttonTv);
        void showTask(Task task);
        void setDeleteButtonVisibility(boolean visible);
        void setWorkManager(String taskTitle , int expiredDate);
        void setAlarmManager(String taskTitle , String taskDescription);
        void updateTask();
        void deleteTask();
    }

    interface presentor extends BasePresentor<view>{
        void deleteButtonClicked();
        void saveButtonClicked(String title , String description , int timePeriod , int priority);
    }
}
