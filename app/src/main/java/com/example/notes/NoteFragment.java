package com.example.notes;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.TimeZone;

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

    private LocalDate dataInForm;
    private LocalTime timeInForm;

    public NoteFragment() {
        showedQuestion = false;
        // Required empty public constructor
    }

    public void setDateNote(LocalDate remindDate) {
        note.saveRemindDate(remindDate);
    }

    public void setTimeNote(LocalTime remindTime) {
        note.saveRemindTime(remindTime);
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
        if (((MainActivity)requireActivity()).getStatusShowNote()) {
            setHasOptionsMenu(true);
        } else {
            setHasOptionsMenu(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.note_menu, menu);
        menu.findItem(R.id.action_exit).setVisible(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reminder:
                if (note.getRemindDate() != null) {
                    note.saveRemindDate(null);
                    getChildFragmentManager().popBackStack();
                } else {
                    note.saveRemindDate(LocalDate.now());
                    note.saveRemindTime(LocalTime.now());
                    getChildFragmentManager()
                            .beginTransaction()
                            .addToBackStack("")
                            .replace(R.id.reminderFrame, ReminderFrame.newInstance(note))
                            .commit();
                }
                break;
            case R.id.delete:
                new AlertDialog.Builder(getContext())
                        .setTitle("подтверждение удаления!")
                        .setMessage("Вы действительно хотите удалить заметку")
                        .setIcon(R.drawable.ic_baseline_delete_24)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                RV_NotesFragment rv_notesFragment = (RV_NotesFragment) requireActivity()
                                        .getSupportFragmentManager()
                                        .getFragments().stream().filter(fragment -> fragment instanceof RV_NotesFragment)
                                        .findFirst().get();
                                ((MainActivity) requireActivity()).setShowNote(false);
                                rv_notesFragment.deleteNoteSource(note.getRV_position());
                                setHasOptionsMenu(false);

//                                Notes.deleteActive();
//                                Notes.resetActivityIndex();
//                                setHasOptionsMenu(false);
//                                NotesFragment notesFragment = (NotesFragment) requireActivity()
//                                        .getSupportFragmentManager()
//                                        .getFragments().stream().filter(fragment -> fragment instanceof NotesFragment)
//                                        .findFirst().get();
//                                notesFragment.showList();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showedQuestion = false;
        note = null;
        if (getArguments() != null) {
            note = new Note((Note) getArguments().getParcelable(ARG_PARAM));
        }
        if (note != null) {

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

            if (note.getRemindDate() != null) {
                ReminderFrame rf = ReminderFrame.newInstance(note);
                getChildFragmentManager()
                        .beginTransaction()
                        .addToBackStack("")
                        .replace(R.id.reminderFrame, rf)
                        .commit();
            }

            Button btnOk = view.findViewById(R.id.btn_ok);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View btnView) {
//                    Note activeNote = Notes.getActivityNote();
                    if (note != null) {
                        if (note.getRemindDate() != null) {
                            if (note.getRemindDate().equals(LocalDate.now())
                                    && note.getRemindTime().isBefore(LocalTime.now())) {
                                Snackbar.make(view.findViewById(R.id.reminderFrame), "Неправильное время напоминания",
                                        Snackbar.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        note.saveValues(
                                ((TextInputEditText) view.findViewById(R.id.name_note)).getText().toString(),
                                ((TextInputEditText) view.findViewById(R.id.description_note)).getText().toString(),
                                Importance.values()[((SeekBar) view.findViewById(R.id.seekBar)).getProgress()],
                                ((CheckBox) view.findViewById(R.id.isDone)).isChecked());
                        note.saveRemindDate(note.getRemindDate());
                        note.saveRemindTime(note.getRemindTime());
//                        Notes.resetActivityIndex();
                        RV_NotesFragment rv_notesFragment = (RV_NotesFragment) requireActivity()
                                .getSupportFragmentManager()
                                .getFragments().stream().filter(fragment -> fragment instanceof RV_NotesFragment)
                                .findFirst().get();
                        ((MainActivity) requireActivity()).setShowNote(false);
                        if (note.getRV_position()>0) {
                            rv_notesFragment.updateNoteSource(note);
                        }  else {
                            rv_notesFragment.addNoteSource(note);
                        }

                        setHasOptionsMenu(false);

//                        requireActivity().getSupportFragmentManager().popBackStack();
                        Toast.makeText(requireContext(),
                                "Заметка сохранена",
                                Toast.LENGTH_SHORT).show();
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
        } else {
            setHasOptionsMenu(false);
        }

        return showedQuestion;
    }

    public void resetShow() {
        showedQuestion = false;
    }

    public Note getNote() {
        return note;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(ARG_PARAM, note);
        super.onSaveInstanceState(outState);
    }
}