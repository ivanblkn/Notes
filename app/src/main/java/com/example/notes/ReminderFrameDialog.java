package com.example.notes;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;


public class ReminderFrameDialog extends DialogFragment {
    private Note note;

    public static final String TAG = "MyReminderFrameDialog";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        note = Notes.getNote(getArguments().getInt("INDEX_VALUE"));
        View customView = inflater.inflate(R.layout.fragment_reminder_frame, null);
        customView.<LinearLayout>findViewById(R.id.layout_btn).setVisibility(View.VISIBLE);
        CalendarView cw = customView.<CalendarView>findViewById(R.id.calendarView);
        cw.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                note.saveRemindDate(LocalDate.of(i, i1 + 1, i2));
//                Toast.makeText(getContext(), String.valueOf(i)).show();
            }
        });

        Calendar calendar = Calendar.getInstance();
        cw.setMinDate(calendar.getTimeInMillis());
        TimePicker tp = (TimePicker) customView.findViewById(R.id.tmPicker);
        tp.setIs24HourView(true);
        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                // ((NoteFragment) getParentFragment()).setTimeNote(LocalTime.of(i, i1));
            }
        });
        if (note.getRemindDate() == null) {
            cw.setMinDate(calendar.getTimeInMillis());
            cw.setDate(LocalDate.now().toEpochDay());
            tp.setHour(LocalTime.now().getHour());
            tp.setMinute(LocalTime.now().getMinute());
        } else {
            calendar.set(LocalDate.ofEpochDay(note.getRemindDate().toEpochDay()).getYear(),
                    LocalDate.ofEpochDay(note.getRemindDate().toEpochDay()).getMonthValue() - 1,
                    LocalDate.ofEpochDay(note.getRemindDate().toEpochDay()).getDayOfMonth());
            cw.setDate(calendar.getTimeInMillis(), true, true);
            tp.setHour(note.getRemindTime().getHour());
            tp.setMinute(note.getRemindTime().getMinute());
        }



        final View viewForm = customView;
        customView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePicker tp = ((TimePicker) viewForm.findViewById(R.id.tmPicker));
                Notes.getNote(note.getKey()).saveRemindTime(LocalTime.of(tp.getHour(),tp.getMinute()));
                CalendarView cw = viewForm.<CalendarView>findViewById(R.id.calendarView);
                Notes.getNote(note.getKey()).saveRemindDate(note.getRemindDate());
//                String text =
//                 customView.<EditText>findViewById(R.id.editText_custom_view).getText().toString();
//                ((MainActivity) requireActivity()).onDialogResult(text);
//// Метод диалога, который позволяет его просто закрыть
//// (по аналогии с Activity finish())
                dismiss();
            }
        });
        customView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String text =
//                 customView.<EditText>findViewById(R.id.editText_custom_view).getText().toString();
//                ((MainActivity) requireActivity()).onDialogResult(text);
//// Метод диалога, который позволяет его просто закрыть
//// (по аналогии с Activity finish())
                dismiss();
            }
        });

//        customView.findViewById(R.id.button_custom_view).setOnClickListener(new
//                                                                                    View.OnClickListener() {
//                                                                                        @Override
//                                                                                        public void onClick(View view) {
//                                                                                            String text =
//                                                                                                    customView.<EditText>findViewById(R.id.editText_custom_view).getText().toString();
//                                                                                            ((MainActivity) requireActivity()).onDialogResult(text);
//// Метод диалога, который позволяет его просто закрыть
//// (по аналогии с Activity finish())
//                                                                                            dismiss();
//                                                                                        }
//                                                                                    });
        return customView;
    }
}
