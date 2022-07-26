package com.example.notes;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment {

    View NotesFragment_rootView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotesFragment() {
        // Required empty public constructor
    }

    public static NotesFragment newInstance() {
        NotesFragment fragment = new NotesFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Notes.getActivityIndex() >= 0) {
            requireActivity().getSupportFragmentManager().popBackStack();
        }
        NotesFragment_rootView = view;
        showList(NotesFragment_rootView);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void showList() {
        showList(NotesFragment_rootView);
    }

    public void showList(View rootView) {
        Context rootContext = getContext();
        LinearLayout linearLayout = (LinearLayout) rootView;
        linearLayout.removeAllViews();
        LinearLayout linearLayoutMain = new LinearLayout(rootContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);
        linearLayoutMain.setLayoutParams(layoutParams);
        linearLayoutMain.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParamsItem = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsItem.setMargins(10, 10, 10, 10);
        for (int i = 0; i < Notes.getSize(); i++) {
            Note n = Notes.getNote(i);
            LinearLayout linearLayoutItem = new LinearLayout(rootContext);
            linearLayoutItem.setLayoutParams(layoutParamsItem);
            linearLayoutItem.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutItem.setBackgroundResource(n.getImportance().getColorRes());

            LinearLayout llText = new LinearLayout(rootContext);
            LinearLayout.LayoutParams layoutParamsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,100);
            llText.setLayoutParams(layoutParamsText);
            llText.setOrientation(LinearLayout.VERTICAL);
            TextView textView = new TextView(rootContext);
            textView.setTextSize(25);
            textView.setText(n.getName());

            LinearLayout llImage = new LinearLayout(rootContext);
            LinearLayout.LayoutParams layoutParamsImg = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1);
            llImage.setLayoutParams(layoutParamsImg);
            llImage.setOrientation(LinearLayout.VERTICAL);

            ImageView imgCheckDO = new ImageView(rootContext);
            LinearLayout.LayoutParams lpImg = new LinearLayout.LayoutParams(50, 50);
            imgCheckDO.setLayoutParams(lpImg);
            imgCheckDO.setImageResource(R.drawable.check_do);

            final int index = i;
            linearLayoutItem.setOnClickListener(v -> {
                NoteFragment noteFragment = NoteFragment.newInstance(Notes.getNote(index));
                Notes.setActivityIndex(index);
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    FrameLayout fl = (FrameLayout) requireActivity().findViewById(R.id.detailsSide);
                    fl.setVisibility(View.VISIBLE);
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
            String description = n.getDescription();
            String textDescription = description.substring(0, Math.min(description.length(), 20));
            if (textDescription.length() < description.length())
                textDescription = textDescription + "...";
            textViewDescription.setText(textDescription);
            llText.addView(textView);
            llText.addView(textViewDescription);
            if (n.getState())
            llImage.addView(imgCheckDO);
            linearLayoutItem.addView(llText);
            linearLayoutItem.addView(llImage);
            linearLayoutMain.addView(linearLayoutItem);
        }

        linearLayout.addView(linearLayoutMain);
        if (Notes.getActivityIndex() >= 0) {
            final int index = Notes.getActivityIndex();
            NoteFragment noteFragment = NoteFragment.newInstance(Notes.getNote(index));
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                FrameLayout fl = (FrameLayout) requireActivity().findViewById(R.id.detailsSide);
                fl.setVisibility(View.VISIBLE);
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
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                FrameLayout fl = (FrameLayout) requireActivity().findViewById(R.id.detailsSide);
                fl.setVisibility(View.GONE);
            }
        }

    }
}

