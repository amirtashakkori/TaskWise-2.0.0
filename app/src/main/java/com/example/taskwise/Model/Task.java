package com.example.taskwise.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "table_task")
public class Task implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    long id;
    @ColumnInfo(name = "task_title")
    String title;
    @ColumnInfo(name = "task_description")
    String description;
    int time_period;
    int is_completed;
    int importance;
    public String workManagerId;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTime_period() {
        return time_period;
    }

    public void setTime_period(int time_period) {
        this.time_period = time_period;
    }

    public int getIs_completed() {
        return is_completed;
    }

    public void setIs_completed(int is_completed) {
        this.is_completed = is_completed;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public String getWorkManagerId() {
        return workManagerId;
    }

    public void setWorkManagerId(String workManagerId) {
        this.workManagerId = workManagerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeInt(this.time_period);
        dest.writeInt(this.is_completed);
        dest.writeInt(this.importance);
        dest.writeString(this.workManagerId);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readLong();
        this.title = source.readString();
        this.description = source.readString();
        this.time_period = source.readInt();
        this.is_completed = source.readInt();
        this.importance = source.readInt();
        this.workManagerId = source.readString();
    }

    public Task() {
    }

    protected Task(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.description = in.readString();
        this.time_period = in.readInt();
        this.is_completed = in.readInt();
        this.importance = in.readInt();
        this.workManagerId = in.readString();
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
