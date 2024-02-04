package com.example.taskwise.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

@Entity(tableName = "table_event")
public class Event implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    long id;
    @ColumnInfo(name = "event_title")
    String title;
    long firstDate , secondDate;
    String date;
    int notifyMe;
    public String workManagerId;
    boolean outdated = false;


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

    public String getWorkManagerId() {
        return workManagerId;
    }

    public void setWorkManagerId(String workManagerId) {
        this.workManagerId = workManagerId;
    }

    public boolean isOutdated() {
        return outdated;
    }

    public void setOutdated(boolean outdated) {
        this.outdated = outdated;
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
        dest.writeString(this.workManagerId);
        dest.writeByte(this.outdated ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readLong();
        this.title = source.readString();
        this.firstDate = source.readLong();
        this.secondDate = source.readLong();
        this.date = source.readString();
        this.notifyMe = source.readInt();
        this.workManagerId = source.readString();
        this.outdated = source.readByte() != 0;
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
        this.workManagerId = in.readString();
        this.outdated = in.readByte() != 0;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
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
