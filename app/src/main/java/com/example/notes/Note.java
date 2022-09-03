package com.example.notes;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Random;

public class Note implements Parcelable {
    private static int noteCounter = 0;

    private String name;
    private String description;
    private Date dateOfCreate;
    private Importance importance;
    private boolean isDone;
    private int key;
    private LocalDate remindDate;
    private LocalTime remindTime;

    public Note() {
        this.name = "Default name " + String.valueOf(noteCounter);
        this.description = "Default description " + String.valueOf(noteCounter);
        this.dateOfCreate = new Date();
        this.importance = Importance.values()[new Random().nextInt(3)];
        this.isDone = (new Random().nextInt(2)) == 1;
        this.key = noteCounter++;
    }

    public Note(Note note) {
        name = note.name;
        description = note.description;
        dateOfCreate = note.dateOfCreate;
        importance = note.importance;
        key = note.key;
        remindDate = note.remindDate;
        isDone = note.isDone;
        remindDate = note.remindDate;
        remindTime = note.remindTime;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected Note(Parcel in) {
        name = in.readString();
        description = in.readString();
        dateOfCreate = new Date(in.readLong());
        importance = Importance.values()[in.readByte()];
        key = in.readInt();
        remindDate = LocalDate.ofEpochDay(in.readLong());
        remindTime = LocalTime.ofSecondOfDay(in.readLong());
        isDone = in.readByte() != 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Importance getImportance() {
        return importance;
    }

    public boolean getState() {
        return isDone;
    }

    public void setState(boolean state) {
        isDone = state;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int index) {
        key = index;
    }

    public Date getDateOfCreate() {
        return dateOfCreate;
    }

    public LocalDate getRemindDate() {
        return remindDate;
    }

    public LocalTime getRemindTime() {
        return remindTime;
    }

    public void saveValues(String name, String description,
                           Importance importance, boolean isDone) {

        this.name = name;
        this.description = description;
        this.importance = importance;
        this.isDone = isDone;
    }

    public void saveRemindDate(LocalDate remindDate) {
        this.remindDate = remindDate;
    }

    public void saveRemindTime(LocalTime remindTime) {
        this.remindTime = remindTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeLong(dateOfCreate.getTime());
        parcel.writeInt(importance.ordinal());
        parcel.writeInt(key);
        parcel.writeLong(remindDate.toEpochDay());
        parcel.writeLong(remindTime.toSecondOfDay());
        parcel.writeByte((byte) (isDone ? 1 : 0));
    }
}
