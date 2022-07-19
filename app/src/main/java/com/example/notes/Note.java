package com.example.notes;

import java.util.Date;
import java.util.Random;

public class Note {
    private static int noteCounter = 1;

    private String name;
    private String description;
    private Date dateOfCreate;
    private Importance importance;
    private boolean isDone;
    private Date remindDate;

    public Note() {
        this.name = "Default name " + String.valueOf(noteCounter);
        this.description = "Default description " + String.valueOf(noteCounter);
        this.dateOfCreate = new Date();
        this.importance = Importance.values()[new Random().nextInt(3)];
        this.isDone = false;

        noteCounter++;
    }

    public String getName() {
        return name;
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

    public Date getDateOfCreate() {
        return dateOfCreate;
    }

    public void saveValues(String name, String description, Importance importance, boolean isDone) {

        this.name = name;
        this.description = description;
        this.importance = importance;
        this.isDone = isDone;

    }
}
