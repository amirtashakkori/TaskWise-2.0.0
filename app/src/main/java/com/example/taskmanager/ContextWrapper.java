package com.example.taskmanager;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Paint;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.taskmanager.SharedPreferences.AppSettingContainer;

import java.util.Locale;

public class ContextWrapper {

    public static Context wrap(Context context){
        AppSettingContainer settingContainer = new AppSettingContainer(context);

        //Language
        String language = settingContainer.getAppLanguage();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration=context.getResources().getConfiguration();
        configuration.setLocale(locale);

        //Dark & Light Mode
        String appMode = settingContainer.getAppMode();
        if (appMode.equals("lightMode"))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        return context.createConfigurationContext(configuration);
    }

    public static void setTheme(Context context , int theme){
        if (theme == 0)
            context.setTheme(R.style.Theme_GreenTaskManager);
        else if (theme == 1)
            context.setTheme(R.style.Theme_NudeTaskManager);
        else if (theme == 2)
            context.setTheme(R.style.Theme_RedTaskManager);
        else
            context.setTheme(R.style.Theme_BlueTaskManager);
    }

}
