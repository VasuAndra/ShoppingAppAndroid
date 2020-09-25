package com.example.proiecttest.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.proiecttest.R;
import com.example.proiecttest.fragments.ButtonsFragment;

public class NotificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        ButtonsFragment buttonsFragment = new ButtonsFragment();

        fragmentTransaction.replace(R.id.buttons_fragment, buttonsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
