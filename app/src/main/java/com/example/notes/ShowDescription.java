package com.example.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ShowDescription extends BottomSheetDialogFragment {
    public static final String TAG = "BottomSheetDialogFragmentShowDescription";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        View customView = inflater.inflate(R.layout.fragment_show_description, null);

        customView.<TextView>findViewById(R.id.text_description).setText(
                Notes.getNote(getArguments().getInt("INDEX_VALUE")).getDescription()
        );


        customView.findViewById(R.id.btn_close).setOnClickListener(view -> {
            dismiss();
        });
        return customView;
    }
}
