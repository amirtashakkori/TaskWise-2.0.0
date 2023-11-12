package com.example.taskmanager.TaskDetail;

import android.widget.Toast;

import com.example.taskmanager.BaseView;
import com.example.taskmanager.DataBase.TaskDao;
import com.example.taskmanager.Main.MainContract;
import com.example.taskmanager.Model.Task;

public class TaskDetailPresentor implements TaskDetailContract.presentor
{
    TaskDao dao;
    TaskDetailContract.view view;
    Task task;

    public TaskDetailPresentor(TaskDao dao , Task task) {
        this.dao = dao;
        this.task = task;
    }


    @Override
    public void onAttach(TaskDetailContract.view view) {
        this.view = view;
        if (task != null){
            view.setDeleteButtonVisibility(true);
            view.showTask(task);
        }
    }

    @Override
    public void onDetach() {
        this.view = null;
    }

    @Override
    public void deleteButtonClicked() {
        if (task != null){
            int result = dao.delete(task);
            if (result > 0){
            }
        }
    }

    @Override
    public void saveButtonClicked(String titel, String description, int timePeriod, int priority) {

    }

    @Override
    public void backButtonClicked() {

    }
}
