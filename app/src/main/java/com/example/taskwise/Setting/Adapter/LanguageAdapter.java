package com.example.taskwise.Setting.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskwise.Model.Language;

import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.item> {

    Context context;
    List<Language> languages;
    int selectedPosition;
    clickListener listener;

    public LanguageAdapter(Context context, List<Language> languages, int selectedPosition , clickListener listener) {
        this.context = context;
        this.languages = languages;
        this.selectedPosition = selectedPosition;
        this.listener = listener;
    }

    @NonNull
    @Override
    public item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_language , parent , false);
        return new item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull item holder, @SuppressLint("RecyclerView") int position) {
        holder.lanTv.setText(languages.get(position).getLanName());
        holder.lanImg.setImageResource(languages.get(position).getLanLogo());
        if (position == selectedPosition)
            holder.checkBox.setChecked(true);

        holder.checkBox.setSelected(selectedPosition == position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.languageClick(position);
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.languageClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return languages.size();
    }

    public class item extends RecyclerView.ViewHolder{

        ImageView lanImg;
        TextView lanTv;
        CheckBox checkBox;

        public item(@NonNull View itemView) {
            super(itemView);
            lanImg = itemView.findViewById(R.id.lanImg);
            lanTv = itemView.findViewById(R.id.lanTv);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    public interface clickListener{
        void languageClick(int language);
    }
}
