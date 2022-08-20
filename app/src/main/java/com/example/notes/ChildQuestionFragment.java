package com.example.notes;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChildQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChildQuestionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChildQuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChildQuestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChildQuestionFragment newInstance() {
        ChildQuestionFragment fragment = new ChildQuestionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_child_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton btnYes = (MaterialButton) view.findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                NotesFragment notesFragment = (NotesFragment) requireActivity()
                        .getSupportFragmentManager()
                        .getFragments().stream().filter( fragment -> fragment instanceof NotesFragment)
                        .findFirst().get();

                Notes.resetActivityIndex();

                getParentFragmentManager().popBackStack();

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

                } else {
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
                notesFragment.showList();

            }
        });
        MaterialButton btnNO = (MaterialButton) view.findViewById(R.id.btn_no);
        btnNO.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                NoteFragment noteFragment = (NoteFragment) requireActivity().getSupportFragmentManager()
                        .getFragments().stream().filter( fragment -> fragment instanceof NoteFragment)
                        .findFirst().get();
                noteFragment.resetShow();
                getParentFragmentManager().popBackStack();
            }
        });
    }
}