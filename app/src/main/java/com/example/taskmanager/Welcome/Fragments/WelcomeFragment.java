package com.example.taskmanager.Welcome.Fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taskmanager.R;

public class WelcomeFragment extends Fragment {

    View view;

    AppCompatButton getStartedBtn;

    public void cast(){
        getStartedBtn = view.findViewById(R.id.getStartedBtn);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_welcome , container , false);

        cast();

        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("key" , 2);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                UserInfoFragment userInfoFragment = new UserInfoFragment();
                userInfoFragment.setArguments(bundle);
                transaction.replace(R.id.fragmentContainer , new UserInfoFragment());
                transaction.addToBackStack("");
                transaction.commit();
            }
        });

        return view;
    }


}