package com.example.taskmanager.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.DataBase.TaskDao;
import com.example.taskmanager.MainActivity;
import com.example.taskmanager.R;
import com.example.taskmanager.SettingActivity;
import com.example.taskmanager.SharedPreferences.AppSettingContainer;
import com.zires.switchsegmentedcontrol.ZiresSwitchSegmentedControl;

import java.util.List;
import java.util.Locale;

public class SettingItemFragment extends Fragment {

    View view;
    TextView selectLan;
    ListView languageList;
    LinearLayout languagePage , modePage;
    ArrayAdapter<String> adapter;
    AppSettingContainer settingContainer;
    ZiresSwitchSegmentedControl appModeSwitch;

    public void cast(){
        selectLan = view.findViewById(R.id.selectLan);
        languageList = view.findViewById(R.id.languageList);
        languagePage = view.findViewById(R.id.languagePage);
        modePage = view.findViewById(R.id.modePage);
        appModeSwitch = view.findViewById(R.id.appModeSwitch);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting_item, container, false);
        cast();
        settingContainer = new AppSettingContainer(getActivity());

        int position = getArguments().getInt("position");
        if (position == 0){
            languagePage.setVisibility(View.VISIBLE);
            modePage.setVisibility(View.GONE);

            String[] lanList = {getString(R.string.english) , getString(R.string.persian)};
            adapter = new ArrayAdapter<>(getActivity() , R.layout.item_language_list , lanList);
            languageList.setAdapter(adapter);
            String appLocale = settingContainer.getAppLanguage();

            if (appLocale.equals("en"))
                languageList.setItemChecked(0 ,true);
            else
                languageList.setItemChecked(1 ,true);

            languageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    settingContainer.saveAppLanguage(getLanguageString(position));
                    if (position == 0){
                        settingContainer.saveAppLanguage("en");
                        resetApp();
                    }
                    else{
                        settingContainer.saveAppLanguage("fa");
                        resetApp();
                    }
                }
            });

        }

        return view;
    }

    public static String getLanguageString(int language){
        if (language == 0)
            return "en";
        else
            return "fa";
    }

    public void resetApp(){
        Intent intent=new Intent(getActivity(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}