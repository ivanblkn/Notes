package com.example.notes;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM = "index";

    private boolean showedQuestion;
    // TODO: Rename and change types of parameters
    private int mParam;

    private Note note;

    public NoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(Note note) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.note = new Note((Note) getArguments().getParcelable(ARG_PARAM));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        showedQuestion = false;
        if (getArguments() != null) {
            note = new Note((Note) getArguments().getParcelable(ARG_PARAM));
            TextInputEditText nameNote = view.findViewById(R.id.name_note);
            nameNote.setText(note.getName());
            nameNote.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    note.setName(nameNote.getText().toString());
                }
            });

            TextInputEditText Description = view.findViewById(R.id.description_note);
            Description.setText(note.getDescription());

            SeekBar imp = view.findViewById(R.id.seekBar);
            imp.setProgress(note.getImportance().ordinal());

            CheckBox cb = view.findViewById(R.id.isDone);
            cb.setChecked(note.getState());

            TextInputEditText dateCreate = view.findViewById(R.id.data_of_created);
            dateCreate.setText(note.getDateOfCreate().toString());

            Button btnOk = view.findViewById(R.id.btn_ok);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View btnView) {
                    Note note = Notes.getActivityNote();
                    if (note != null) {
                        note.saveValues(
                                ((TextInputEditText) view.findViewById(R.id.name_note)).getText().toString(),
                                ((TextInputEditText) view.findViewById(R.id.description_note)).getText().toString(),
                                Importance.values()[((SeekBar) view.findViewById(R.id.seekBar)).getProgress()],
                                ((CheckBox) view.findViewById(R.id.isDone)).isChecked());

                        NotesFragment notesFragment = (NotesFragment) requireActivity()
                                .getSupportFragmentManager()
                                .getFragments().stream().filter( fragment -> fragment instanceof NotesFragment)
                                .findFirst().get();

                        Notes.resetActivityIndex();
                        notesFragment.showList();
                        requireActivity().getSupportFragmentManager().popBackStack();

                    }
                }
            });

        }
    }
    public boolean showChildQuestion() {
        if (!showedQuestion) {
            getChildFragmentManager()
                    .beginTransaction()
                    .addToBackStack("")
                    .replace(R.id.childQuestion, ChildQuestionFragment.newInstance())
                    .commit();
            showedQuestion = true;
            return false;
        }

        return showedQuestion;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(ARG_PARAM, note);
        super.onSaveInstanceState(outState);
    }
}