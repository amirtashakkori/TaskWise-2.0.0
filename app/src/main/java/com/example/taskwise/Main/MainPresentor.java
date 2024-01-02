package com.example.taskwise.Main;

import android.os.Build;

import com.example.taskwise.DataBase.DBDao;
import com.example.taskwise.Model.Event;
import com.example.taskwise.Model.Task;
import com.example.taskmanager.R;
import com.example.taskwise.SharedPreferences.AppSettingContainer;
import com.example.taskwise.SharedPreferences.UserInfoContainer;

import java.util.List;

public class MainPresentor implements MainContract.presentor {

    DBDao dao;
    UserInfoContainer userInfoContainer;
    AppSettingContainer settingContainer;
    List<Task> tasks;
    List<Event> events;
    MainContract.view view;

    String userName;
    String fullName;
    String expertise;
    String appLanguage;
    boolean firstUse;

    public MainPresentor(DBDao dao , UserInfoContainer userInfoContainer , AppSettingContainer settingContainer) {
        this.dao = dao;
        this.userInfoContainer = userInfoContainer;
        this.settingContainer = settingContainer;
        tasks = dao.getTaskList();
        events = dao.getEventList();
        userName = userInfoContainer.getName().toString();
        fullName = userInfoContainer.getName() + userInfoContainer.getFamily();
        expertise = userInfoContainer.getExpertise();
        appLanguage = settingContainer.getAppLanguage();
        firstUse = settingContainer.getFirstUse();
    }

    @Override
    public void onAttach(MainContract.view view) {
        this.view = view;
        view.setDate();

        if (userName.equals("")){
            view.goToWelcomeActivity();
        }

        view.setNavigationDrawerText(fullName , expertise);

        if (!tasks.isEmpty()){
            view.showTasks(tasks);
            view.setTaskEmptyStateVisibility(false);
        } else {
            view.setTaskEmptyStateVisibility(true);
        }

        if (tasks.isEmpty() && events.isEmpty()){
            view.setHeaderTexts(userName , R.string.empty);
        } else {
            view.setHeaderTexts(userName , R.string.notEmpty);
        }
    }

    @Override
    public void onDetach() {
        this.view = null;
    }

    @Override
    public void clearTaskListClicked() {
        dao.deleteAllTasks();
        view.setTaskEmptyStateVisibility(true);
    }

    @Override
    public void clearEventListClicker() {
        dao.deleteAllEvents();
        view.setEventEmptyStateVisibility(true);
    }

    @Override
    public void validatingFirstUse(boolean firsUse) {
        if (firsUse){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                view.showPermissionDialog();
            }
        }
    }

    @Override
    public void switchTab(int tabPosition) {
        if (tabPosition == 0 ){
            if (!tasks.isEmpty()){
                view.showTasks(tasks);
                view.setTaskEmptyStateVisibility(false);
            } else {
                view.setTaskEmptyStateVisibility(true);
            }
        } else if (tabPosition == 1){
            if (!events.isEmpty()){
                view.showEvents(events);
                view.setEventEmptyStateVisibility(false);
            } else {
                view.setEventEmptyStateVisibility(true);
            }

        }
    }

    @Override
    public void updateTask(Task task) {
        dao.update(task);
    }

    @Override
    public void deleteTask(Task task) {
        dao.deleteTask(task);
        tasks = dao.getTaskList();
        if (tasks.isEmpty())
            view.setTaskEmptyStateVisibility(true);
    }

    @Override
    public void deleteEvent(Event event) {
        dao.deleteEvent(event);
        events = dao.getEventList();
        if (events.isEmpty())
            view.setEventEmptyStateVisibility(true);
    }


}
