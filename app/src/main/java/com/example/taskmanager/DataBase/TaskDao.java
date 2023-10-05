package com.example.taskmanager.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.taskmanager.Model.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    long addTask(Task task);

    int[] period = {1 , 2 , 3};
    @Query("select * from table_task where time_period  ORDER BY time_period + importance")
    List<Task> getTaskList();

    @Query("select * from table_task where time_period = 0")
    List<Task> getTodayTaskList();

    @Query("SELECT * FROM table_task WHERE task_title LIKE '%' || :query || '%'")
    List<Task> search(int query);

    @Delete
    int delete(Task task);

    @Query("DELETE FROM table_task")
    void deleteAll();

    @Update
    int update(Task task);
}
