package com.example.minimalnotesapp2.listeners;

import android.util.Pair;
import android.view.View;

import com.example.minimalnotesapp2.entities.Note;

public interface NotesListener {

    void onNoteClicked(Note note, int position, View v, Pair[] pair);
}
