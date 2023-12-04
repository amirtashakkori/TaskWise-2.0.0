package com.example.taskwise.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.taskwise.Model.Event;
import com.example.taskwise.Model.Task;

import java.util.List;

@Dao
public interface EventDao {
    //Task DataBase
    @Insert
    long addTask(Event event);

    @Query("select * from table_event ORDER BY event_date + event_time ")
    List<Event> getEventList();

    @Query("SELECT * FROM table_event WHERE event_date LIKE :query")
    Task search(String query);

    @Delete
    int delete(Event event);

    @Query("DELETE FROM table_event")
    void deleteAll();

    @Update
    int update(Event event);
}
