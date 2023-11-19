package com.example.taskmanager.Setting.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.taskmanager.Setting.SettingAdapter.ThemeAdapter;
import com.example.taskmanager.Model.Theme;
import com.example.taskmanager.R;
import com.example.taskmanager.SharedPreferences.AppSettingContainer;

import java.util.ArrayList;
import java.util.List;

public class ThemeFragment extends Fragment {

    View view;
    AppSettingContainer settingContainer;
    RecyclerView themeRv;
    RelativeLayout backBtn;
    List<Theme> themeList;
    ThemeAdapter themeAdapter;

    public void cast(){
        themeRv = view.findViewById(R.id.themeRv);
        backBtn = view.findViewById(R.id.backBtn);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_theme, container, false);
        cast();
        settingContainer = new AppSettingContainer(getActivity());

        themeList = new ArrayList<>();
        themeList.add(new Theme(R.drawable.bg_theme_green , "Avocado" , "#246832"));
        themeList.add(new Theme(R.drawable.bg_theme_red , "Red Apple" , "#A41D19"));
        themeList.add(new Theme(R.drawable.bg_theme_blue , "BlueBerry" , "#0C356A"));
        themeRv.setLayoutManager(new GridLayoutManager(getActivity() , 2 , RecyclerView.VERTICAL , false));
        themeAdapter = new ThemeAdapter(getActivity() , themeList);
        themeRv.setAdapter(themeAdapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        return view;
    }
}