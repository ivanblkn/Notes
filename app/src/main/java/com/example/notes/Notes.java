package com.example.notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Notes {
    private static ArrayList<Note> listNotes;

    private static int activityIndex = -1;

    public Notes() {
        if (listNotes == null) {
            loadNotes();
        }
    }

    private static void loadNotes() {
        listNotes = new ArrayList<Note>();
        for (int i = 0; i < 12; i++) {

            listNotes.add(new Note());
        }
    }

    static {
        loadNotes();
    }

    public static Note getActivityNote() {
        if (activityIndex >= 0) return listNotes.get(activityIndex);
        return null;
    }

    public static int getActivityIndex() {
        return activityIndex;
    }

    public static void resetActivityIndex() {
        activityIndex = -1;
    }

    public static void setActivityIndex(int index) {
        activityIndex = index;
    }
    @SuppressLint("ResourceAsColor")
    public static void showList(View rootView, FragmentManager fragmentManager, boolean isLandScape) {

    }

    public static Note getNote(int index) {
        for (int i=0; i<listNotes.size(); i++) {
            if (listNotes.get(i).getKey() == index) {
                return listNotes.get(i);
            }
        }
        return null;
    }

    public static Note getIndexNote(int index) {
        return listNotes.get(index);
    }

    public static int getSize() {
        return listNotes.size();
    }

    public static void deleteActive() {
        listNotes.remove(activityIndex);
    }

    public static void deleteIndex(int index) {
        for (int i=0; i<listNotes.size(); i++) {
            if (listNotes.get(i).getKey() == index) {
                listNotes.remove(i);
                return;
            }
        }
    }

}
