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
        if (listNotes == null) loadNotes();
    }

    private void loadNotes() {
        listNotes = new ArrayList<Note>();
        for (int i = 0; i < 12; i++) {

            listNotes.add(new Note());
        }
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

    @SuppressLint("ResourceAsColor")
    public static void showList(View rootView, FragmentManager fragmentManager, boolean isLandScape) {
        Context rootContext = rootView.getContext();
        LinearLayout linearLayout = (LinearLayout) rootView;
        LinearLayout linearLayoutMain = new LinearLayout(rootContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);
        linearLayoutMain.setLayoutParams(layoutParams);
        linearLayoutMain.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParamsItem = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsItem.setMargins(10, 10, 10, 10);
        for (int i = 0; i < listNotes.size(); i++) {
            LinearLayout linearLayoutItem = new LinearLayout(rootContext);
            linearLayoutItem.setLayoutParams(layoutParamsItem);
            linearLayoutItem.setOrientation(LinearLayout.VERTICAL);
            linearLayoutItem.setBackgroundResource(listNotes.get(i).getImportance().getColorRes());

            TextView textView = new TextView(rootContext);
            textView.setTextSize(25);
            textView.setText(listNotes.get(i).getName());

            final int index = i;
            linearLayoutItem.setOnClickListener(v -> {
                NoteFragment noteFragment = NoteFragment.newInstance(index);
                if (isLandScape) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.details_container, noteFragment)
                            .commit();

                } else {
                    fragmentManager.beginTransaction()
                            .add(R.id.note_container, noteFragment)
                            .addToBackStack("")
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                }
            });

            TextView textViewDescription = new TextView(rootContext);
            textViewDescription.setTextSize(15);
            String description = listNotes.get(i).getDescription();
            String textDescription = description.substring(0, Math.min(description.length(), 20));
            if (textDescription.length() < description.length())
                textDescription = textDescription + "...";
            textViewDescription.setText(textDescription);

            linearLayoutItem.addView(textView);
            linearLayoutItem.addView(textViewDescription);
            linearLayoutMain.addView(linearLayoutItem);
        }

        linearLayout.addView(linearLayoutMain);
        if (activityIndex >= 0) {
            final int index = activityIndex;
            NoteFragment noteFragment = NoteFragment.newInstance(index);
            if (isLandScape) {
                fragmentManager.beginTransaction()
                        .replace(R.id.details_container, noteFragment)
                        .commit();

            } else {
                fragmentManager.beginTransaction()
                        .add(R.id.note_container, noteFragment)
                        .addToBackStack("")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        }
    }

    public static void showNote(View rootView, int index) {
        TextInputEditText nameNote = rootView.findViewById(R.id.name_note);
        nameNote.setText(listNotes.get(index).getName());

        TextInputEditText Description = rootView.findViewById(R.id.description_note);
        Description.setText(listNotes.get(index).getDescription());

        SeekBar imp = rootView.findViewById(R.id.seekBar);
        imp.setProgress(listNotes.get(index).getImportance().ordinal());

        CheckBox cb = rootView.findViewById(R.id.isDone);
        cb.setChecked(listNotes.get(index).getState());

        TextInputEditText dateCreate = rootView.findViewById(R.id.data_of_created);
        dateCreate.setText(listNotes.get(index).getDateOfCreate().toString());

        activityIndex = index;

        Button btnOk = rootView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = Notes.getActivityNote();
                if (note != null) {
                    note.saveValues(
                            ((TextInputEditText) rootView.findViewById(R.id.name_note)).getText().toString(),
                            ((TextInputEditText) rootView.findViewById(R.id.description_note)).getText().toString(),
                            Importance.values()[((SeekBar) rootView.findViewById(R.id.seekBar)).getProgress()],
                            ((CheckBox) rootView.findViewById(R.id.isDone)).isChecked());
                }
            }
        });
    }
}
