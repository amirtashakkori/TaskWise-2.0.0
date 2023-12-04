package com.example.taskwise.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Time;
import java.util.Date;

@Entity(tableName = "table_event")
public class Event {
    @PrimaryKey(autoGenerate = true)
    long id;
    @ColumnInfo(name = "event_title")
    String title;
    @ColumnInfo(name = "event_description")
    String description;
    @ColumnInfo(name = "event_date")
    long date;
    @ColumnInfo(name = "event_time")
    long time;
}
