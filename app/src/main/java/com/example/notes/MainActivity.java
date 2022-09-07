package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    private Notes notes;
    private boolean isShowNote;

    public void setShowNote(boolean statusNote) {
        isShowNote = statusNote;
        setVisibleNotesDetail();
    }

    public boolean getStatusShowNote() {
        return isShowNote;
    }

    public void setVisibleNotesDetail() {
        LinearLayout.LayoutParams lParam_detailsSide = new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams lParam_notesContainer = new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT);
        FrameLayout fl_detailsSide = findViewById(R.id.fr_detailsContainer);
        FrameLayout fl_notesContainer = findViewById(R.id.fr_notesContainer);
        if (isShowNote) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                lParam_detailsSide.weight = 1;
                lParam_notesContainer.weight = 1;
            } else {
                lParam_detailsSide.weight = 1;
                lParam_notesContainer.weight = 0;
            }
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                lParam_detailsSide.weight = 0;
                lParam_notesContainer.weight = 1;
            } else {
                lParam_detailsSide.weight = 0;
                lParam_notesContainer.weight = 1;
            }
        }
        fl_detailsSide.setLayoutParams(lParam_detailsSide);
        fl_notesContainer.setLayoutParams(lParam_notesContainer);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        initToolBar();

        if (savedInstanceState == null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fr_notesContainer, RV_NotesFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
            isShowNote = false;
        } else {
            isShowNote = savedInstanceState.getBoolean("isShowNote");
        }
        setVisibleNotesDetail();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    @Override
    public void onBackPressed() {
        if (isShowNote) {
            NoteFragment noteFragment = (NoteFragment) getSupportFragmentManager()
                    .getFragments().stream().filter(fragment -> fragment instanceof NoteFragment)
                    .findFirst().get();
            if (noteFragment.showChildQuestion()) {
                setShowNote(false);
            }
        } else {
            super.onBackPressed();
            if (getSupportFragmentManager().getFragments().size() == 0) {
                finish();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("")
                        .add(R.id.topLayout, new AboutFragment())
                        .commit();
                break;
            case R.id.action_add:
                NoteFragment noteFragment = NoteFragment.newInstance(new Note(true));
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.details_container, noteFragment)
                        .commit();
                setShowNote(true);
                break;
            case R.id.action_clear:
                RV_NotesFragment rv = (RV_NotesFragment) getSupportFragmentManager()
                        .getFragments().stream().filter(fragment -> fragment instanceof RV_NotesFragment)
                        .findFirst().get();
                rv.clearNoteSource();
                break;
            case R.id.action_add10:
                RV_NotesFragment rv_add = (RV_NotesFragment) getSupportFragmentManager()
                        .getFragments().stream().filter(fragment -> fragment instanceof RV_NotesFragment)
                        .findFirst().get();
                rv_add.add10NoteSource();
                break;
            case R.id.action_exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("isShowNote",isShowNote);
        super.onSaveInstanceState(outState);
    }

    private void initToolBar() {
        Toolbar tb = findViewById(R.id.toolBar);
        setSupportActionBar(tb);
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) initDrawer(tb);
    }

    private void initDrawer(Toolbar toolbar) {

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_drawer_about:
                        if (getSupportFragmentManager().getFragments().stream().filter(fragment -> fragment instanceof AboutFragment).count() == 0) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .addToBackStack("")
                                    .add(R.id.topLayout, new AboutFragment())
                                    .commit();
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.action_drawer_exit:
                        finish();
                        return true;
                }
                return false;
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        String name = "Name";
        String descriptionText = "Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name,
                importance);
        channel.setDescription(descriptionText);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

    }

}