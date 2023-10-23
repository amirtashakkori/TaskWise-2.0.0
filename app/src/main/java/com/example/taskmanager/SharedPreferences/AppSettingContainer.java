package com.example.taskmanager.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettingContainer {
    SharedPreferences languageSp;
    SharedPreferences appModeSp;

    public AppSettingContainer(Context context){
        languageSp = context.getSharedPreferences("language" , Context.MODE_PRIVATE);
        appModeSp = context.getSharedPreferences("appMode" , Context.MODE_PRIVATE);
    }

    public void saveAppLanguage(String language){
        SharedPreferences.Editor editor = languageSp.edit();
        editor.putString("language" , language);
        editor.apply();
    }

    public void saveAppMode(String mode){
        SharedPreferences.Editor editor = appModeSp.edit();
        editor.putString("appMode" , mode);
        editor.apply();
    }

    public String getAppLanguage(){
        return languageSp.getString("language"  , "en");
    }

    public String getAppMode(){
        return appModeSp.getString("appMode" , "lightMode");
    }
}
