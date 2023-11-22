package com.example.taskwise.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.taskwise.Model.Task;

@Database(version = 1 , exportSchema = false , entities = {Task.class})
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase appDataBase;

    public static AppDataBase getAppDataBase(Context c) {
        if (appDataBase == null){
            appDataBase = Room.databaseBuilder(c.getApplicationContext() , AppDataBase.class , "db_tasks").allowMainThreadQueries().build();
        }
        return appDataBase;
    }

    public abstract TaskDao getDataBaseDao();
}
