package com.example.taskwise.Welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;

import com.example.taskwise.ContextWrapper;
import com.example.taskmanager.R;
import com.example.taskwise.SharedPreferences.AppSettingContainer;
import com.example.taskwise.Welcome.Fragments.UserInfoFragment;
import com.example.taskwise.Welcome.Fragments.WelcomeFragment;

public class WelcomeActivity extends AppCompatActivity {

    int key;
    AppSettingContainer settingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingContainer = new AppSettingContainer(this);
        ContextWrapper.setTheme(this , settingContainer.getAppTheme());
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ContextWrapper.wrap(newBase));
    }
}