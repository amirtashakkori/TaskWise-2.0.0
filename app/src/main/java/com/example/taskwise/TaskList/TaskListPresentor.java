package com.example.taskwise.TaskList;

import com.example.taskwise.DataBase.DBDao;
import com.example.taskwise.Model.Task;
import com.example.taskwise.SharedPreferences.AppSettingContainer;

import java.util.List;

public class TaskListPresentor implements TaskListContract.presentor {

    DBDao dao;
    List<Task> completedTasks;
    List<Task> outDatedTasks;
    AppSettingContainer container;
    int position;
    TaskListContract.view view;

    int appTheme;

    public TaskListPresentor(DBDao dao, int position , AppSettingContainer container) {
        this.dao = dao;
        this.position = position;
        this.container = container;
        appTheme = container.getAppTheme();
        completedTasks = dao.getCompletedTasks();
        outDatedTasks = dao.getOutDatedTasks();
    }


    @Override
    public void onAttach(TaskListContract.view view) {
        this.view = view;
        if (position == 1){
            if (!completedTasks.isEmpty()){
                view.showList(completedTasks);
                view.setEmptyStateVisibility(false , 1);
                view.setDeleteButtonVisibility(true);
            } else {
                view.setEmptyStateVisibility(true , 1 );
                view.setDeleteButtonVisibility(false);
            }
        } else {
            if (!outDatedTasks.isEmpty()){
                view.showList(outDatedTasks);
                view.setEmptyStateVisibility(false , 2 );
                view.setDeleteButtonVisibility(true);
            } else {
                view.setEmptyStateVisibility(true , 2 );
                view.setDeleteButtonVisibility(false);
            }
        }
    }

    @Override
    public void onDetach() {
        this.view = null;
    }

    @Override
    public void deleteAllButtonClicked() {
        if (position == 1) {
            dao.deleteCompletedTasks();
            view.setEmptyStateVisibility(true , 1);
        } else {
            dao.deleteOutDatedTasks();
            view.setEmptyStateVisibility(true , 0);
        }
    }

    @Override
    public void updateTask(Task task) {
        dao.update(task);
    }

    @Override
    public void deleteTask(Task task) {
        dao.deleteTask(task);
        if (position == 1){
            completedTasks = dao.getCompletedTasks();
            if (completedTasks.isEmpty())
                view.setEmptyStateVisibility(true , 1 );
        } else{
            outDatedTasks = dao.getOutDatedTasks();
            if (outDatedTasks.isEmpty())
                view.setEmptyStateVisibility(true , 0 );
        }
    }
}
