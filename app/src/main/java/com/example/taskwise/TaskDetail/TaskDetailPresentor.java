package com.example.taskwise.TaskDetail;

import com.example.taskwise.DataBase.TaskDao;
import com.example.taskwise.Model.Task;
import com.example.taskmanager.R;

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
            view.setTexts(R.string.editTask , R.string.saveChanges);
        } else {
            view.setDeleteButtonVisibility(false);
            view.setTexts(R.string.createNewTask , R.string.createTask);
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
                view.deleteTask();
            }
        }
    }

    @Override
    public void saveButtonClicked(String title, String description, int timePeriod, int priority) {
        if (task != null){
            task.setTitle(title);
            task.setDescription(description);
            task.setTime_period(timePeriod);
            task.setImportance(priority);
            int res = dao.update(task);
            if (res > 0)
                view.updateTask();
        } else {
            task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setTime_period(timePeriod);
            task.setImportance(priority);
            dao.addTask(task);
            view.setWorkManager(title , getExpireDate(timePeriod));
        }
    }

    public int getExpireDate(int timePeriod){
        if(timePeriod == 0)
            return 1;
        else if (timePeriod == 1)
            return 3;
        else if (timePeriod == 2)
            return 7;
        else
            return 30;
    }

}
