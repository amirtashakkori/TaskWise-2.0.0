package com.example.taskwise.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.taskwise.Model.Event;
import com.example.taskwise.Model.Task;

@Database(version = 1 , exportSchema = false , entities = {Task.class , Event.class})
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase appDataBase;

    public static AppDataBase getAppDataBase(Context c) {
        if (appDataBase == null){
            appDataBase = Room.databaseBuilder(c.getApplicationContext() , AppDataBase.class , "db_schedule").allowMainThreadQueries().build();
        }
        return appDataBase;
    }

    public abstract TaskDao getTaskDataBaseDao();

    public abstract EventDao getEventDataBaseDao();
}
