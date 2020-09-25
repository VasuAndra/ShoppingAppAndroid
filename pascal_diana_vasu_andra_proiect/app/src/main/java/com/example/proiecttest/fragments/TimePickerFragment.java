package com.example.proiecttest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.proiecttest.R;

public class TimePickerFragment extends Fragment {
    private TimePicker timePicker;
    private TextView time_view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time_picker_fragment, container, false);

        timePicker = view.findViewById(R.id.time_picker);
        //time_view = view.findViewById(R.id.dateTV);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                FragmentManager fragmentManager = getFragmentManager();
                ButtonsFragment buttonsFragment = (ButtonsFragment)fragmentManager.findFragmentById(R.id.buttons_fragment);
                buttonsFragment.setMinute(minute);
                buttonsFragment.setHour(hourOfDay);

                //time_view.setText(hourOfDay + ":" + minute);

            }
        });

        return view;
    }

}
