package com.example.taskwise.TaskDetail;

import com.example.taskwise.DataBase.DBDao;
import com.example.taskwise.Model.Task;
import com.example.taskmanager.R;
import com.example.taskwise.SharedPreferences.AppSettingContainer;

public class TaskDetailPresentor implements TaskDetailContract.presentor {
    DBDao dao;
    TaskDetailContract.view view;
    Task task;
    AppSettingContainer settingContainer;

    public TaskDetailPresentor(DBDao dao , Task task , AppSettingContainer settingContainer) {
        this.dao = dao;
        this.task = task;
        this.settingContainer = settingContainer;
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
            int result = dao.deleteTask(task);
            if (result > 0){
                view.deleteTask();
            }
        }
    }

    @Override
    public void saveButtonClicked(String title, String description, int timePeriod, int priority ) {
        if (task != null){
            task.setTitle(title);
            task.setDescription(description);
            task.setTime_period(timePeriod);
            task.setImportance(priority);
            int res = dao.update(task);

            if (res > 0){
                view.updateTask();

                if (settingContainer.isTaskNotificationEnabled()){
                    view.cancelAlarmManager(task.getId());
                    view.setAlarmManager(task.getId() , task);
                }

                view.cancelWorkManger(task);
                view.setWorkManager(task.getId());
            }

        } else {
            task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setTime_period(timePeriod);
            task.setImportance(priority);

            long id = dao.addTask(task);

            view.setWorkManager(id);

            if (settingContainer.isTaskNotificationEnabled())
                view.setAlarmManager(task.getId() , task);
        }
    }



}
