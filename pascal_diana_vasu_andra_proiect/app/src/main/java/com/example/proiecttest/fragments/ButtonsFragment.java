package com.example.proiecttest.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.proiecttest.R;
import com.example.proiecttest.Receiver;

import java.util.Calendar;

public class ButtonsFragment extends Fragment {
    private Button timePickerButton;
    private Button datePickerButton;
    private Button saveButton;
    private int hour;
    private int minute;
    private int year;
    private int month;
    private int day;

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.buttons_fragment, container, false);

        timePickerButton = view.findViewById(R.id.time_picker_button);
        datePickerButton = view.findViewById(R.id.date_picker_button);
        saveButton = view.findViewById(R.id.save);



        timePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.time_date_container, timePickerFragment, "TIME_PICKER");
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.time_date_container, datePickerFragment, "DATE_PICKER");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);System.out.println(getArguments());

                sendOnChannel(calendar);

                Fragment timePickerFragment = getFragmentManager().findFragmentByTag("TIME_PICKER");
                if(timePickerFragment != null)
                {
                    getFragmentManager().beginTransaction().remove(timePickerFragment).commit();
                    getActivity().getSupportFragmentManager().popBackStack();
                }

                Fragment datePickerFragment = getFragmentManager().findFragmentByTag("DATE_PICKER");
                if(datePickerFragment != null)
                {
                    getFragmentManager().beginTransaction().remove(datePickerFragment).commit();
                    getActivity().getSupportFragmentManager().popBackStack();
                }

                //getFragmentManager().beginTransaction().remove(ButtonsFragment.this).commit();

                //getActivity().getSupportFragmentManager().popBackStack();
                closeFragment();

            }
        });
        return view;
    }

    public void sendOnChannel( Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), Receiver.class);
        Toast.makeText(getContext(), "Alarm created", Toast.LENGTH_SHORT).show();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }

    private void closeFragment() {

        FragmentTransaction fragmentTransaction;

        fragmentTransaction = getFragmentManager().beginTransaction();

        Fragment fragment = getFragmentManager().findFragmentById(R.id.linearLayout6);
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
