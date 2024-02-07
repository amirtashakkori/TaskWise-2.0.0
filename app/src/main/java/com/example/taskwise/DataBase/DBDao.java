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
public interface DBDao {
    //Task DataBase
    @Insert
    long addTask(Task task);

    @Query("select * from table_task WHERE time_period != 4 ORDER BY time_period ASC , importance ASC")
    List<Task> getTaskList();

    @Query("select * from table_task where time_period = 4 and is_completed = 0")
    List<Task> getOutDatedTasks();

    @Query("select * from table_task where is_completed = 1")
    List<Task> getCompletedTasks();

    @Query("SELECT * FROM table_task WHERE id = :id")
    Task searchTask(long id);

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

    @Query("select * from table_event WHERE outdated = 0 ORDER BY firstDate + secondDate ")
    List<Event> getEventList();

    @Query("select * from table_event Where date LIKE '%' || :query || '%'")
    List<Event> getDateEventList(String query);

    @Delete
    int deleteEvent(Event event);

    @Query("DELETE FROM table_event")
    void deleteAllEvents();

    @Query("SELECT * FROM table_event WHERE id = :id")
    Event searchEvent(long id);

    @Update
    int update(Event event);
}
