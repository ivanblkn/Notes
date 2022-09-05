package com.example.notes;

import java.util.ArrayList;
import java.util.List;

public class NoteSourceImp implements NoteSourceInterface {
    private List<Note> noteSource;

    public NoteSourceImp() {
        noteSource = new ArrayList<>(Notes.getSize());
    }

    public NoteSourceImp init() {

        for (int i = 0; i < Notes.getSize(); i++) {
            noteSource.add(new Note(Notes.getNote(i)));
        }

        return this;

    }

    @Override
    public Note getCardNote(int position) {
        return noteSource.get(position);
    }

    @Override
    public int size() {
        return noteSource.size();
    }

}
