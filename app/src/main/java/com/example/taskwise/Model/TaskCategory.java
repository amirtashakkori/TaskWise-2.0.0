package com.example.taskwise.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Struct;

@Entity(tableName = "table_category")
public class TaskCategory {
    @PrimaryKey(autoGenerate = true)
    long id;
    @ColumnInfo(name = "title")
    String categoryTitle;

    public TaskCategory(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }
}
