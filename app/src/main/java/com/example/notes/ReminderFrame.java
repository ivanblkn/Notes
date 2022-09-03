package com.example.notes;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TimePicker;


import java.time.LocalDate;

import java.time.LocalTime;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReminderFrame#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReminderFrame extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DATE = "ARG_DATE";
    private static final String ARG_HOUR = "ARG_HOUR";
    private static final String ARG_MINUTES = "ARG_MINUTES";

    private long longDate;
    private int intHour;
    private int intMinutes;

    public ReminderFrame() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReminderFrame.
     */
    // TODO: Rename and change types and number of parameters
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ReminderFrame newInstance(Note note) {
        ReminderFrame fragment = new ReminderFrame();
        Bundle args = new Bundle();
        if (note.getRemindDate() == null) {
            args.putLong(ARG_DATE, 0);
            args.putInt(ARG_HOUR, 0);
            args.putInt(ARG_MINUTES, 0);
        } else {
            args.putLong(ARG_DATE, note.getRemindDate().toEpochDay());
            args.putInt(ARG_HOUR, note.getRemindTime().getHour());
            args.putInt(ARG_MINUTES, note.getRemindTime().getMinute());
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            longDate = getArguments().getLong(ARG_DATE);
            intHour = getArguments().getInt(ARG_HOUR);
            intMinutes = getArguments().getInt(ARG_MINUTES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder_frame, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();

        CalendarView cw = (CalendarView) view.findViewById(R.id.calendarView);

        Calendar calendar = Calendar.getInstance();
        cw.setMinDate(calendar.getTimeInMillis());

        if (longDate != 0) {
            calendar.set(LocalDate.ofEpochDay(longDate).getYear(),
                    LocalDate.ofEpochDay(longDate).getMonthValue() - 1,
                    LocalDate.ofEpochDay(longDate).getDayOfMonth());
        }
        cw.setDate(calendar.getTimeInMillis(), true, true);

        cw.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                ((NoteFragment) getParentFragment()).setDateNote(LocalDate.of(i, i1 + 1, i2));
//                Toast.makeText(getContext(), String.valueOf(i)).show();
            }
        });
        TimePicker tp = (TimePicker) view.findViewById(R.id.tmPicker);
        tp.setIs24HourView(true);
        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                ((NoteFragment) getParentFragment()).setTimeNote(LocalTime.of(i, i1));
            }
        });
        tp.setHour(intHour);
        tp.setMinute(intMinutes);
    }
}