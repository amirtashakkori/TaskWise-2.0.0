package com.example.taskmanager.Fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.R;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MonthlyCalendarFragment extends Fragment{

    View view;
    TextView monthTv;
    RecyclerView calendarRv;
    private LocalDate selectedDate;
    int daysInMonth;
    int dayOfWeek;

    public void find(){
        monthTv = view.findViewById(R.id.monthTv);
        calendarRv = view.findViewById(R.id.calendarRv);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_monthly_calendar , container , false);
        find();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            selectedDate = LocalDate.now();
        }

        return view;
    }

}