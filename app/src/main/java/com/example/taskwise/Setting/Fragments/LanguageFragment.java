package com.example.taskwise.Setting.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.taskwise.Main.MainActivity;
import com.example.taskmanager.R;
import com.example.taskwise.Model.Language;
import com.example.taskwise.Setting.Adapter.LanguageAdapter;
import com.example.taskwise.SharedPreferences.AppSettingContainer;

import java.util.ArrayList;
import java.util.List;

public class LanguageFragment extends Fragment implements LanguageAdapter.clickListener {

    View view;
    RecyclerView languageRv;
//    ArrayAdapter<String> adapter;
    LanguageAdapter adapter;
    List<Language> languages;
    AppSettingContainer settingContainer;
    RelativeLayout backBtn;


    public void cast(){
        languageRv = view.findViewById(R.id.languageRv);
        backBtn = view.findViewById(R.id.backBtn);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_language, container, false);
        cast();
        settingContainer = new AppSettingContainer(getActivity());

//        String[] lanList = {getString(R.string.english) , getString(R.string.persian)};
//        adapter = new ArrayAdapter<>(getActivity() , R.layout.item_language_list , lanList);
//        languageList.setAdapter(adapter);
//
//        if (settingContainer.getAppLanguage().equals("en"))
//            languageList.setItemChecked(0 , true);
//        else
//            languageList.setItemChecked(1 , true);
//
//        languageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                settingContainer.saveAppLanguage(getLanguageString(position));
//                if (position == 0){
//
//                }
//                else{
//
//                }
//            }
//        });
        languages = new ArrayList<>();
        languages.add(new Language(R.drawable.en_logo , R.string.english));
        languages.add(new Language(R.drawable.fa_logo , R.string.persian));
        adapter = new LanguageAdapter(getContext() , languages , getLanguageInt(settingContainer.getAppLanguage()) , this);
        languageRv.setLayoutManager(new LinearLayoutManager(getContext() , RecyclerView.VERTICAL , false));
        languageRv.setAdapter(adapter);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    public void resetApp(){
        Intent intent=new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void languageClick(int language) {
        if (language == 0){
            settingContainer.saveAppLanguage("en");
            resetApp();
        } else {
            settingContainer.saveAppLanguage("fa");
            resetApp();
        }
    }

    public int getLanguageInt(String language){
        if (language.equals("en"))
            return 0;
        else
            return 1;
    }
}