package com.example.notes;

public interface NoteSourceInterface {
    Note getCardNote(int position);

    void updateNote(Note note);
    void deleteNote(int position);
    void addNote(Note note);
    int size();
}
