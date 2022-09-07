package com.example.notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import java.lang.reflect.Type;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import androidx.activity.result.contract.ActivityResultContracts;
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

import java.lang.reflect.Type;
import java.util.List;

public class RV_NotesFragment extends Fragment {

    private NoteSourceInterface notes;
    private RV_NoteAdapter adapter;
    private RecyclerView rv;

    private static final String KEY = "KEY_NOTES";
    private SharedPreferences sharedPreferences;


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

        notes = new NoteSourceImp();//.init();
        initRecyclerView(rv, notes);

        return view;
    }

    private void initRecyclerView(RecyclerView recyclerView, NoteSourceInterface data) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RV_NoteAdapter(data, this);
        recyclerView.setAdapter(adapter);

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String savedNotes = sharedPreferences.getString(KEY, null);
        if (savedNotes == null) {
            Toast.makeText(getContext(),"Записи отсутствуют, добавьте записи",Toast.LENGTH_LONG).show();
        } else {
            try {
                Type type = new TypeToken<List<Note>>(){}.getType();
                adapter.saveNoteSource(new GsonBuilder().create().fromJson(savedNotes, type));
            } catch (Exception e) {
                Toast.makeText(getContext(),"Ошибка",Toast.LENGTH_LONG).show();
            }
        }

        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NoteFragment noteFragment = NoteFragment.newInstance(data.getCardNote(position));
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.details_container, noteFragment)
                        .commit();
                ((MainActivity) requireActivity()).setShowNote(true);
            }
        });
    }
    public void updateNoteSource(Note note) {
        notes.updateNote(note);
        adapter.notifyItemChanged(note.getRV_position());
        getActivity().getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putString(KEY,
                        new GsonBuilder().create().toJson(notes.loadNotes()))
                .apply();

    }

    public void deleteNoteSource(int position) {
        notes.deleteNote(position);
        adapter.notifyItemRemoved(position);
        getActivity().getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putString(KEY,
                        new GsonBuilder().create().toJson(notes.loadNotes()))
                .apply();

    }

    public void clearNoteSource() {
        int oldSize = notes.size();
        notes.clear();
        adapter.notifyItemRangeRemoved(0, oldSize);
        getActivity().getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putString(KEY,
                        new GsonBuilder().create().toJson(notes.loadNotes()))
                .apply();

    }

    public void add10NoteSource() {
        int oldSize = notes.size();
        notes.add10();
        adapter.notifyItemRangeInserted(oldSize - 1, 10);
        getActivity().getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putString(KEY,
                        new GsonBuilder().create().toJson(notes.loadNotes()))
                .apply();

    }
    public void addNoteSource(Note note) {
        notes.addNote(note);
        adapter.notifyItemInserted(notes.size() - 1);
        rv.scrollToPosition(notes.size() - 1);

        rv.smoothScrollToPosition(notes.size() - 1);
        getActivity().getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putString(KEY,
                        new GsonBuilder().create().toJson(notes.loadNotes()))
                .apply();

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
                getActivity().getPreferences(Context.MODE_PRIVATE)
                        .edit()
                        .putString(KEY,
                                new GsonBuilder().create().toJson(notes.loadNotes()))
                        .apply();

                return true;
        }

        return super.onContextItemSelected(item);
    }
}