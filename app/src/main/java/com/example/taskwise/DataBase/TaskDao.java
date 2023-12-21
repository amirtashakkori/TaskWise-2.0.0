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
public interface TaskDao {
    //Task DataBase
    @Insert
    long addTask(Task task);

    @Query("select * from table_task ORDER BY importance ASC , time_period ASC")
    List<Task> getTaskList();

    @Query("select * from table_task where time_period = 4 and is_completed = 0")
    List<Task> getOutDatedTasks();

    @Query("select * from table_task where is_completed = 1")
    List<Task> getCompletedTasks();

    @Query("SELECT * FROM table_task WHERE task_title LIKE :query")
    Task search(String query);

    @Delete
    int deleteTask(Task task);

    @Query("DELETE FROM table_task")
    void deleteAllTasks();

    @Query("Delete from table_task where is_completed = 1")
    void deleteCompletedTasks();

    @Query("delete from table_task where time_period = 4")
    void deleteOutDatedTasks();

    @Update
    int update(Task task);

    //Task DataBase
    @Insert
    long addEvent(Event event);

    @Query("select * from table_event ORDER BY firstDate + secondDate ")
    List<Event> getEventList();

    @Query("select * from table_event Where date LIKE '%' || :query || '%'")
    List<Event> getDateEventList(String query);

    @Delete
    int deleteEvent(Event event);

    @Query("DELETE FROM table_event")
    void deleteAllEvents();

    @Update
    int update(Event event);
}
