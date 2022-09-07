package com.example.notes;

import java.util.List;

public interface NoteSourceInterface {
    Note getCardNote(int position);

    void updateNote(Note note);
    void deleteNote(int position);
    void addNote(Note note);
    void clear();
    void add10();

    void saveNotes(List<Note> noteSource);
    List<Note> loadNotes();
    int size();


}
