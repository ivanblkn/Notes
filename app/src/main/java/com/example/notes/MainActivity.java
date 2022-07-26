package com.example.notes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    private Notes notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {

            NotesFragment notesFragment = NotesFragment.newInstance();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.note_container, notesFragment, FRAGMENT_TAG)
                    .commit();
        }



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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    @Override
    public void onBackPressed() {
        if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            && (Notes.getActivityIndex()>=0)) {
            NoteFragment notesFragment = (NoteFragment) getSupportFragmentManager()
                    .getFragments().stream().filter( fragment -> fragment instanceof NoteFragment)
                    .findFirst().get();

            if (notesFragment.showChildQuestion()) {
                Notes.resetActivityIndex();

                FrameLayout fl = (FrameLayout) findViewById(R.id.detailsSide);
                fl.setVisibility(View.GONE);
            }
        } else {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();
                //additional code
            } else {


                NoteFragment noteFragment = (NoteFragment) getSupportFragmentManager()
                        .getFragments().stream().filter( fragment -> fragment instanceof NoteFragment)
                        .findFirst().get();

                if (noteFragment.showChildQuestion()) {
                    Notes.resetActivityIndex();

                    getSupportFragmentManager().popBackStack();

                }
            }
        }
////        finish();
////        overridePendingTransition(0, 0);
////        startActivity(getIntent());
////        overridePendingTransition(0, 0);
//
    }
}