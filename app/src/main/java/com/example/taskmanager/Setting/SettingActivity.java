package com.example.taskmanager.Setting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;

import com.example.taskmanager.ContextWrapper;
import com.example.taskmanager.R;
import com.example.taskmanager.Setting.Fragments.SettingListFragment;
import com.example.taskmanager.SharedPreferences.AppSettingContainer;


public class SettingActivity extends AppCompatActivity {

    AppSettingContainer settingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingContainer = new AppSettingContainer(this);
        ContextWrapper.setTheme(this , settingContainer.getAppTheme());
        setContentView(R.layout.activity_setting);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container , new SettingListFragment());
        transaction.commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        int backStack = getFragmentManager().getBackStackEntryCount();

        if (backStack == 0){
            super.onBackPressed();
        } else if (backStack > 0){
            getFragmentManager().popBackStack();
        }
    }
}