package com.example.taskwise.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

public class AppSettingContainer {
    SharedPreferences languageSp;
    SharedPreferences firstUseSp;
    SharedPreferences notificationSp;
    SharedPreferences appModeSp;
    SharedPreferences themeSp;

    public AppSettingContainer(Context context){
        languageSp = context.getSharedPreferences("language" , Context.MODE_PRIVATE);
        firstUseSp = context.getSharedPreferences("firstUse" , Context.MODE_PRIVATE);
        notificationSp = context.getSharedPreferences("notification" , Context.MODE_PRIVATE);
        appModeSp = context.getSharedPreferences("appMode" , Context.MODE_PRIVATE);
        themeSp = context.getSharedPreferences("theme" , Context.MODE_PRIVATE);
    }

    public void saveAppLanguage(String language){
        SharedPreferences.Editor editor = languageSp.edit();
        editor.putString("language" , language);
        editor.apply();
    }

    public void saveUserFirstUse(boolean firstUse){
        SharedPreferences.Editor editor = firstUseSp.edit();
        editor.putBoolean("firstUse" , firstUse);
        editor.apply();
    }

    public void isEventNotificationEnable(boolean availability){
        SharedPreferences.Editor editor = notificationSp.edit();
        editor.putBoolean("eventNotification" , availability);
        editor.apply();
    }

    public void isTaskNotificationEnable(boolean availability){
        SharedPreferences.Editor editor = notificationSp.edit();
        editor.putBoolean("taskNotification" , availability);
        editor.apply();
    }

    public void saveAppMode(String mode){
        SharedPreferences.Editor editor = appModeSp.edit();
        editor.putString("appMode" , mode);
        editor.apply();
    }

    public void saveAppTheme(int theme){
        SharedPreferences.Editor editor = themeSp.edit();
        editor.putInt("theme" , theme);
        editor.apply();
    }

    public String getAppLanguage(){
            return languageSp.getString("language"  , Locale.getDefault().getLanguage());
    }

    public boolean isEventNotificationEnabled(){
        return notificationSp.getBoolean("eventNotification" , true);
    }

    public boolean isTaskNotificationEnabled(){
        return notificationSp.getBoolean("taskNotification" , true);
    }


    public boolean getFirstUse(){
        return firstUseSp.getBoolean("firstUse" , true);
    }

    public String getAppMode(){
        return appModeSp.getString("appMode" , "lightMode");
    }

    public int getAppTheme(){
        return themeSp.getInt("theme" , 0);
    }
}
