package com.example.taskmanager.Setting.SettingAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.Model.Option;
import com.example.taskmanager.R;

import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.item> {
    Context c;
    List<Option> optionList;
    onCLickListener listener;

    public SettingAdapter(Context c, List<Option> optionList, onCLickListener lickListener) {
        this.c = c;
        this.optionList = optionList;
        this.listener = lickListener;
    }

    @NonNull
    @Override
    public item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.item_setting_list , parent , false);
        return new item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull item holder, @SuppressLint("RecyclerView") int position) {
        holder.bindSetting(optionList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    public class item extends RecyclerView.ViewHolder{
        TextView optionTitle;
        ImageView optionIcon;
        public item(@NonNull View itemView) {
            super(itemView);
            optionTitle = itemView.findViewById(R.id.optionTitle);
            optionIcon = itemView.findViewById(R.id.optionIcon);
        }
        public void bindSetting(Option option){
            optionTitle.setText(option.getOptionTitle());
            optionIcon.setImageResource(option.getOptionIcon());
        }
    }

    public interface onCLickListener{
        void onClick(int position);
    }

}
