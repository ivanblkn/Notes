package com.example.notes;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
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

    private View NotesFragment_rootView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM = "index";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Note activeNote;

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
//        super.onCreate(null);
//
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NotesFragment_rootView = view;
        showList(NotesFragment_rootView, null);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showList() {
        showList(NotesFragment_rootView, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showList(View rootView, Note activeNote) {
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
            n.setKey(i);
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
            llImage.setId(Integer.valueOf(11111111));
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
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.details_container, noteFragment)
                        .commit();
                LinearLayout.LayoutParams lParam_detailsSide = new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT);
                LinearLayout.LayoutParams lParam_notesContainer = new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT);
                FrameLayout fl_detailsSide = (FrameLayout) requireActivity().findViewById(R.id.fr_detailsContainer);
                FrameLayout fl_notesContainer = (FrameLayout) requireActivity().findViewById(R.id.fr_notesContainer);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    lParam_detailsSide.weight = 1;
                    lParam_notesContainer.weight = 1;
                } else {
                    lParam_detailsSide.weight = 1;
                    lParam_notesContainer.weight = 0;
                }
                fl_detailsSide.setLayoutParams(lParam_detailsSide);
                fl_notesContainer.setLayoutParams(lParam_notesContainer);

            });

            linearLayoutItem.setOnLongClickListener(v->{
                Activity activity = requireActivity();
                PopupMenu popupMenu = new PopupMenu(activity, v);
                activity.getMenuInflater().inflate(R.menu.notes_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_popupDelete:
                                if (Notes.getActivityIndex()==index) {

                                    NoteFragment notesFragment = (NoteFragment) requireActivity().getSupportFragmentManager()
                                            .getFragments().stream().filter( fragment -> fragment instanceof NoteFragment)
                                            .findFirst().get();
                                    LinearLayout.LayoutParams lParam_detailsSide = new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT);
                                    LinearLayout.LayoutParams lParam_notesContainer = new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT);
                                    FrameLayout fl_detailsSide = (FrameLayout) requireActivity().findViewById(R.id.fr_detailsContainer);
                                    FrameLayout fl_notesContainer = (FrameLayout) requireActivity().findViewById(R.id.fr_notesContainer);
                                    lParam_detailsSide.weight = 0;
                                    lParam_notesContainer.weight = 1;
                                    fl_detailsSide.setLayoutParams(lParam_detailsSide);
                                    fl_notesContainer.setLayoutParams(lParam_notesContainer);
                                    notesFragment.setHasOptionsMenu(false);
                                    Notes.resetActivityIndex();
                                }
                                Notes.deleteIndex(index);
                                linearLayoutMain.removeView(linearLayoutItem);

                                return true;
                            case R.id.action_popup_setDo:
                                Notes.getNote(index).setState(true);
                                ((LinearLayout)linearLayoutItem.findViewById(Integer.valueOf(11111111))).addView(imgCheckDO);
                                return true;
                        }
                        return true;
                    }
                });
                if (Notes.getNote(index).getState()) popupMenu.getMenu().findItem(R.id.action_popup_setDo).setVisible(false);
                popupMenu.show();

                return true;
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
        LinearLayout.LayoutParams lParam_detailsSide = new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams lParam_notesContainer = new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT);
        FrameLayout fl_detailsSide = (FrameLayout) requireActivity().findViewById(R.id.fr_detailsContainer);
        FrameLayout fl_notesContainer = (FrameLayout) requireActivity().findViewById(R.id.fr_notesContainer);
        if (Notes.getActivityIndex() >= 0) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                lParam_detailsSide.weight = 1;
                lParam_notesContainer.weight = 1;
            } else {
                lParam_detailsSide.weight = 1;
                lParam_notesContainer.weight = 0;
            }
        } else {
            lParam_detailsSide.weight = 0;
            lParam_notesContainer.weight = 1;
        }
        fl_detailsSide.setLayoutParams(lParam_detailsSide);
        fl_notesContainer.setLayoutParams(lParam_notesContainer);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

