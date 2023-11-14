package com.example.taskmanager.TaskList;

import com.example.taskmanager.BasePresentor;
import com.example.taskmanager.BaseView;
import com.example.taskmanager.Model.Task;

import java.util.List;

public interface TaskListContract {
    interface view extends BaseView {
        void showList(List<Task> tasks);
        void setDeleteButtonVisibility(boolean visible);
        void setEmptyStateVisibility(boolean visible ,int es , int appTheme);
    }

    interface presentor extends BasePresentor<view> {
        void deleteAllButtonClicked();
        void updateTask(Task task);
    }
}
