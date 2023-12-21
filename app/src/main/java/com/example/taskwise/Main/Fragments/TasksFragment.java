package com.example.taskwise.Main.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.taskmanager.R;

public class TasksFragment extends Fragment {

    View view;

    RecyclerView tasksRv;
    LinearLayout emptyState;

    public void cast(){
        tasksRv = view.findViewById(R.id.tasksRv);
        emptyState = view.findViewById(R.id.emptyState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_events, container, false);
        cast();


        return view;
    }
}