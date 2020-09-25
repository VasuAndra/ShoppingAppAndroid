package com.example.proiecttest.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.proiecttest.R;


public class DatePickerFragment extends Fragment {
    private DatePicker datePicker;
    private TextView date_view;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_picker_fragment, container, false);

        datePicker = view.findViewById(R.id.date_picker);
        //date_view = view.findViewById(R.id.dateTV);
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                FragmentManager fragmentManager = getFragmentManager();
                ButtonsFragment buttonsFragment = (ButtonsFragment)fragmentManager.findFragmentById(R.id.buttons_fragment);
                buttonsFragment.setDay(dayOfMonth);
                buttonsFragment.setMonth(monthOfYear);
                buttonsFragment.setYear(year);
                //date_view.setText(dayOfMonth + " - " + (monthOfYear + 1) + " - " + year);
            }
        });

        return view;
    }

}
