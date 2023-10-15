package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.appsearch.GetSchemaResponse;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taskmanager.Fragments.UserInfoFragment;
import com.example.taskmanager.Fragments.WelcomeFragment;

public class WelcomeActivity extends AppCompatActivity {

    int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        key = getIntent().getIntExtra("key" , 0);
        if (key == 1){
            Bundle bundle = new Bundle();
            bundle.putInt("key" , key);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            UserInfoFragment userInfoFragment = new UserInfoFragment();
            userInfoFragment.setArguments(bundle);
            transaction.add(R.id.fragmentContainer , userInfoFragment);
            transaction.commit();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentContainer , new WelcomeFragment());
            transaction.commit();
        }
    }

    @Override
    public void onBackPressed() {
        int backStack = getSupportFragmentManager().getBackStackEntryCount();

        if (backStack > 0){
            super.onBackPressed();
        } else if (key == 1) {
            super.onBackPressed();
        } else {
            finishAffinity();
        }

    }
}