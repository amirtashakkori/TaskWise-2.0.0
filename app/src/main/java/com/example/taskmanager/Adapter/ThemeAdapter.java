package com.example.taskmanager.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.MainActivity;
import com.example.taskmanager.Model.Theme;
import com.example.taskmanager.R;
import com.example.taskmanager.SharedPreferences.AppSettingContainer;

import java.net.PortUnreachableException;
import java.util.List;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.item> {

    Context c;
    List<Theme> themeList;
    int selected;
    AppSettingContainer settingContainer;

    public ThemeAdapter(Context c, List<Theme> themeList) {
        this.c = c;
        this.themeList = themeList;
    }

    @NonNull
    @Override
    public item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.item_theme , parent , false);
        return new item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull item holder, int position) {
        holder.bindThemes(themeList.get(position) , position);
    }

    @Override
    public int getItemCount() {
        return themeList.size();
    }

    public class item extends RecyclerView.ViewHolder{
        LinearLayout colorIv;
        TextView themeNameTv;
        RelativeLayout checkBtn;
        public item(@NonNull View itemView) {
            super(itemView);
            colorIv = itemView.findViewById(R.id.colorIv);
            themeNameTv = itemView.findViewById(R.id.themeNameTv);
            checkBtn = itemView.findViewById(R.id.checkBtn);
        }

        public void bindThemes(Theme theme , int position){
            colorIv.setBackgroundResource(theme.getColorImage());
            themeNameTv.setTextColor(Color.parseColor(theme.getTextColor()));
            themeNameTv.setText(theme.getColorName());

            settingContainer = new AppSettingContainer(c);
            selected = settingContainer.getAppTheme();

            if (position == selected)
                checkBtn.setVisibility(View.VISIBLE);
            else
                checkBtn.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected = position;
                    settingContainer.saveAppTheme(position);
                    notifyDataSetChanged();
                    resetApp();
                }
            });
        }
    }
    public void resetApp(){
        Intent intent=new Intent(c, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(intent);
    }
}
