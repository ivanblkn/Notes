package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Notes notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotesFragment notesFragment = new NotesFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.note_container, notesFragment)
                .commit();


//        if (savedInstanceState != null) {
//            notes = new Notes();
//
////            presenter.restoreState(savedInstanceState);
//        } else {
//            notes = new Notes();
//        }


//        if (savedInstanceState == null) getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.fragment_container, new CitiesFragment()).commit();


//        CitiesFragment citiesFragment = (CitiesFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
//        if (citiesFragment == null) {
//            citiesFragment = new CitiesFragment();
//        }
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_container, citiesFragment, FRAGMENT_TAG).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Notes.resetActivityIndex();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);

    }
}