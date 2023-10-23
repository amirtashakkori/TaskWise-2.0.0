package com.example.taskmanager;

import android.content.Context;
import android.content.res.Configuration;

import com.example.taskmanager.SharedPreferences.AppSettingContainer;

import java.util.Locale;

public class ContextWrapper {

    public static Context wrap(Context context){
        AppSettingContainer settingContainer = new AppSettingContainer(context);
        String language = settingContainer.getAppLanguage();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration=context.getResources().getConfiguration();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }
}
