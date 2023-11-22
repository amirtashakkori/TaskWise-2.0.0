package com.example.taskwise.TaskList;

import com.example.taskwise.BasePresentor;
import com.example.taskwise.BaseView;
import com.example.taskwise.Model.Task;

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
