package com.example.taskwise.Setting.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.taskwise.Model.Option;
import com.example.taskmanager.R;
import com.example.taskwise.Setting.SettingAdapter.SettingAdapter;
import com.example.taskwise.SharedPreferences.AppSettingContainer;

import java.util.ArrayList;
import java.util.List;

public class SettingListFragment extends Fragment implements SettingAdapter.onCLickListener {

    View view;
    List<Option> optionList;
    RecyclerView settingRv;
    SettingAdapter adapter;
    RelativeLayout appModeBtn , backBtn;
    AppSettingContainer settingContainer;

    String appMode;
    FragmentTransaction transaction;

    public void cast(){
        backBtn = view.findViewById(R.id.backBtn);
        settingRv = view.findViewById(R.id.settingRv);
        appModeBtn = view.findViewById(R.id.appModeBtn);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting_list, container, false);
        cast();

        settingContainer = new AppSettingContainer(getActivity());
        appMode = settingContainer.getAppMode();

        optionList = new ArrayList<>();
        optionList.add(new Option(getString(R.string.language),R.drawable.ic_language));
        optionList.add(new Option(getString(R.string.theme) , R.drawable.ic_theme));

        settingRv.setLayoutManager(new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL , false));
        adapter = new SettingAdapter(getActivity() , optionList , this);
        settingRv.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        appModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appMode.equals("lightMode")){
                    settingContainer.saveAppMode("darkMode");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else {
                    settingContainer.saveAppMode("lightMode");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(int position) {
        if (position == 0){
            transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container , new LanguageFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container , new ThemeFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}