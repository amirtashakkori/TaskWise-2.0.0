package com.example.taskwise.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Time;
import java.util.Date;

@Entity(tableName = "table_event")
public class Event implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    long id;
    @ColumnInfo(name = "event_title")
    String title;
    long firstDate , secondDate;
    String date;
    int notifyMe;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNotifyMe() {
        return notifyMe;
    }

    public void setNotifyMe(int notifyMe) {
        this.notifyMe = notifyMe;
    }

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

    public long getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(long firstDate) {
        this.firstDate = firstDate;
    }

    public long getSecondDate() {
        return secondDate;
    }

    public void setSecondDate(long secondDate) {
        this.secondDate = secondDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeLong(this.firstDate);
        dest.writeLong(this.secondDate);
        dest.writeString(this.date);
        dest.writeInt(this.notifyMe);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readLong();
        this.title = source.readString();
        this.firstDate = source.readLong();
        this.secondDate = source.readLong();
        this.date = source.readString();
        this.notifyMe = source.readInt();
    }

    public Event() {
    }

    protected Event(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.firstDate = in.readLong();
        this.secondDate = in.readLong();
        this.date = in.readString();
        this.notifyMe = in.readInt();
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
