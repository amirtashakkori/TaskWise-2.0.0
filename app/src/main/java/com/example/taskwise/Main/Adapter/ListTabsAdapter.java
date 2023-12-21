package com.example.taskwise.Main.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.taskwise.Main.Fragments.EventsFragment;
import com.example.taskwise.Main.Fragments.TasksFragment;

import java.util.List;

public class ListTabsAdapter extends FragmentStateAdapter {

    public ListTabsAdapter(@NonNull FragmentActivity fragmentActivity , List<?> list) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                new TasksFragment();
                break;
            case 1:
                new EventsFragment();
                break;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
