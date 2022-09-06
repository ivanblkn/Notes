package com.example.notes;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class RV_NotesFragment extends Fragment {

    private NoteSourceInterface notes;
    private RV_NoteAdapter adapter;
    private RecyclerView rv;



    public RV_NotesFragment() {
    }

    public static RV_NotesFragment newInstance() {
        RV_NotesFragment fragment = new RV_NotesFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_r_v__notes, container, false);

        rv = view.findViewById(R.id.recycler_view_lines);

        notes = new NoteSourceImp().init();
        initRecyclerView(rv, notes);

        return view;
    }

    private void initRecyclerView(RecyclerView recyclerView, NoteSourceInterface data) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RV_NoteAdapter(data, this);
        recyclerView.setAdapter(adapter);

        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NoteFragment noteFragment = NoteFragment.newInstance(data.getCardNote(position));
//                Notes.setActivityIndex(position);
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.details_container, noteFragment)
                        .commit();
                ((MainActivity) requireActivity()).setShowNote(true);

//                LinearLayout.LayoutParams lParam_detailsSide = new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT);
//                LinearLayout.LayoutParams lParam_notesContainer = new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT);
//                FrameLayout fl_detailsSide = (FrameLayout) requireActivity().findViewById(R.id.fr_detailsContainer);
//                FrameLayout fl_notesContainer = (FrameLayout) requireActivity().findViewById(R.id.fr_notesContainer);
//                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    lParam_detailsSide.weight = 1;
//                    lParam_notesContainer.weight = 1;
//                } else {
//                    lParam_detailsSide.weight = 1;
//                    lParam_notesContainer.weight = 0;
//                }
//                fl_detailsSide.setLayoutParams(lParam_detailsSide);
//                fl_notesContainer.setLayoutParams(lParam_notesContainer);

            }
        });
    }
    public void updateNoteSource(Note note) {
        notes.updateNote(note);
        adapter.notifyItemChanged(note.getRV_position());
    }

    public void deleteNoteSource(int position) {
        notes.deleteNote(position);
        adapter.notifyItemRemoved(position);
    }

    public void addNoteSource(Note note) {
        notes.addNote(note);
        adapter.notifyItemInserted(notes.size() - 1);
        rv.scrollToPosition(notes.size() - 1);

        rv.smoothScrollToPosition(notes.size() - 1);

    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.notes_popup, menu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        int position = adapter.getMenuPosition();
        switch (item.getItemId()){
            case R.id.action_popupDelete:
                deleteNoteSource(position);
                return true;
        }

        return super.onContextItemSelected(item);
    }
}