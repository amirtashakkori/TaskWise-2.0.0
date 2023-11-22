package com.example.taskwise.Setting.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.taskwise.Main.MainActivity;
import com.example.taskmanager.R;
import com.example.taskwise.SharedPreferences.AppSettingContainer;

public class LanguageFragment extends Fragment {

    View view;
    ListView languageList;
    ArrayAdapter<String> adapter;
    AppSettingContainer settingContainer;
    RelativeLayout backBtn;


    public void cast(){
        languageList = view.findViewById(R.id.languageList);
        backBtn = view.findViewById(R.id.backBtn);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_language, container, false);
        cast();
        settingContainer = new AppSettingContainer(getActivity());

        String[] lanList = {getString(R.string.english) , getString(R.string.persian)};
        adapter = new ArrayAdapter<>(getActivity() , R.layout.item_language_list , lanList);
        languageList.setAdapter(adapter);

        if (settingContainer.getAppLanguage().equals("en"))
            languageList.setItemChecked(0 , true);
        else
            languageList.setItemChecked(1 , true);

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

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    public static String getLanguageString(int language){
        if (language == 0)
            return "en";
        else
            return "fa";
    }

    public void resetApp(){
        Intent intent=new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}