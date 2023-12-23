package com.example.taskwise.Main;

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

    int appTheme;
    String userName;
    String fullName;
    String expertise;
    String appLan;

    public MainPresentor(DBDao dao , UserInfoContainer userInfoContainer , AppSettingContainer settingContainer) {
        this.dao = dao;
        this.userInfoContainer = userInfoContainer;
        this.settingContainer = settingContainer;
        tasks = dao.getTaskList();
        events = dao.getEventList();
        appTheme = settingContainer.getAppTheme();
        userName = userInfoContainer.getName().toString();
        fullName = userInfoContainer.getName() + " " + userInfoContainer.getFamily();
        expertise = userInfoContainer.getExpertise();
        appLan = settingContainer.getAppLanguage();
    }

    @Override
    public void onAttach(MainContract.view view) {
        this.view = view;
        view.setDate();
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
    public void validatingUserInfo() {
        if (userName.equals("")){
            view.goToWelcomeActivity();

        }
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


}
