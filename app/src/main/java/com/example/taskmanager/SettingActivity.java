package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.taskmanager.Adapter.SettingAdapter;
import com.example.taskmanager.Fragments.SettingItemFragment;
import com.example.taskmanager.Model.Option;
import com.example.taskmanager.SharedPreferences.AppSettingContainer;

import java.util.ArrayList;
import java.util.List;


public class SettingActivity extends AppCompatActivity implements SettingAdapter.onCLickListener{

    ImageView backBtn;
    List<Option> optionList;
    RecyclerView settingRv;
    SettingAdapter adapter;
    RelativeLayout appModeBtn;
    AppSettingContainer settingContainer;

    String appMode;

    public void cast(){
        backBtn = findViewById(R.id.backBtn);
        settingRv = findViewById(R.id.settingRv);
        appModeBtn = findViewById(R.id.appModeBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        cast();
        settingContainer = new AppSettingContainer(this);
        appMode = settingContainer.getAppMode();

        optionList = new ArrayList<>();
        optionList.add(new Option(getString(R.string.language),R.drawable.ic_language));
        optionList.add(new Option(getString(R.string.appMode) , R.drawable.ic_them_mode));
        optionList.add(new Option(getString(R.string.theme) , R.drawable.ic_theme));

        settingRv.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        adapter = new SettingAdapter(this , optionList , this);
        settingRv.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        appModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appMode.equals("lightMode"))
                    settingContainer.saveAppMode("darkMode");
                else
                    settingContainer.saveAppMode("lightMode");
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(int position) {
        settingRv.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putInt("position" , position);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SettingItemFragment settingItemFragment = new SettingItemFragment();
        settingItemFragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container , settingItemFragment);
        transaction.addToBackStack("");
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        settingRv.setVisibility(View.VISIBLE);
    }
}