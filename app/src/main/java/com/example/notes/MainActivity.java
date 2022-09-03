package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

// Toast - Реализован при сохранении заметки
//SnackBar - реализован при неправильно установленно времени.
//SnackBar с действием, вызывается при установке признака сделано в списке в контекстом меню элемента.
//AlertDialog Подтверждение удаления
//AlertDialog + Custom View - Реализован контекстном меню списка элемента "Переименовать".
//DialogFragment + Custom view - Реализован в установке напоминания из контекстного меню
//BottomSheet - Реализован в "показать описание" из контекстного меню
//Push Notification - Реализован из контекстного меню "Показать уведомление"

public class MainActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";
    private Notes notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(null);
//        getSupportFragmentManager().getFragment()
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        initToolBar();

        if (savedInstanceState == null) {

            NotesFragment notesFragment = NotesFragment.newInstance();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.notesContainer, notesFragment, FRAGMENT_TAG)
                    .commit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    @Override
    public void onBackPressed() {
        if (Notes.getActivityIndex()>=0) {
            NoteFragment notesFragment = (NoteFragment) getSupportFragmentManager()
                    .getFragments().stream().filter( fragment -> fragment instanceof NoteFragment)
                    .findFirst().get();
            if (notesFragment.showChildQuestion()) {
                LinearLayout.LayoutParams lParam_detailsSide = new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT);
                LinearLayout.LayoutParams lParam_notesContainer = new LinearLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT);
                FrameLayout fl_detailsSide = (FrameLayout) findViewById(R.id.fr_detailsContainer);
                FrameLayout fl_notesContainer = (FrameLayout) findViewById(R.id.fr_notesContainer);
                lParam_detailsSide.weight = 0;
                lParam_notesContainer.weight = 1;
                fl_detailsSide.setLayoutParams(lParam_detailsSide);
                fl_notesContainer.setLayoutParams(lParam_notesContainer);
                notesFragment.setHasOptionsMenu(false);
                Notes.resetActivityIndex();
            }
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

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
            case R.id.action_exit:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    private void initToolBar()    {
        Toolbar tb = findViewById(R.id.toolBar);
        setSupportActionBar(tb);
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) initDrawer(tb);
    }

    private void initDrawer(Toolbar toolbar){

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
                switch (id){
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