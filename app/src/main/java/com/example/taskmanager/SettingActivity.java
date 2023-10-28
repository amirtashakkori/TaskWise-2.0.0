package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.taskmanager.Adapter.SettingAdapter;
import com.example.taskmanager.Fragments.SettingListFragment;
import com.example.taskmanager.Model.Option;
import com.example.taskmanager.SharedPreferences.AppSettingContainer;

import java.util.List;


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