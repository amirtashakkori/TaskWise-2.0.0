package com.example.taskwise.DataBase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RenameTable;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.work.Data;
import androidx.work.impl.Migration_1_2;

import com.example.taskwise.Model.Event;
import com.example.taskwise.Model.Task;

@Database(version = 2 , exportSchema = false ,  entities = {Task.class , Event.class} )
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase appDataBase;

    public static AppDataBase getAppDataBase(Context c) {
        if (appDataBase == null){
            appDataBase = Room.databaseBuilder(c.getApplicationContext() , AppDataBase.class , "db_tasks")
                    .addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries()
                    .build();
        }
        return appDataBase;
    }

    public abstract DBDao getDataBaseDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE table_task ADD COLUMN workManagerId TEXT");
            database.execSQL("CREATE TABLE IF NOT EXISTS `table_event` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`event_title` TEXT, " +
                    "`firstDate` INTEGER NOT NULL, " +
                    "`secondDate` INTEGER NOT NULL, " +
                    "`date` TEXT, " +
                    "`notifyMe` INTEGER NOT NULL, " +
                    "`workManagerId` TEXT NOT NULL, " +
                    "`outdated` INTEGER Default '0' NOT NULL)");
        }
    };

}
