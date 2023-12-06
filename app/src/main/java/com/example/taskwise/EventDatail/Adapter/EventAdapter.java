package com.example.taskwise.EventDatail.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskwise.Model.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.item> {

    Context c;
    List<Event> events;

    public EventAdapter(Context c, List<Event> events) {
        this.c = c;
        this.events = events;
    }

    @NonNull
    @Override
    public item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull item holder, int position) {
        holder.bindEvents(events.get(position));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class item extends RecyclerView.ViewHolder{

        TextView eventTitleTv;
        TextView dayTv , monthTv , yearTv , startTimeTv , endTimeTv;

        public item(@NonNull View itemView) {
            super(itemView);
            eventTitleTv = itemView.findViewById(R.id.eventTitleEt);
            dayTv = itemView.findViewById(R.id.dayTv);
            monthTv = itemView.findViewById(R.id.monthTv);
            yearTv = itemView.findViewById(R.id.yearTv);
            startTimeTv = itemView.findViewById(R.id.startTimeTv);
            endTimeTv = itemView.findViewById(R.id.endTimeTv);
        }

        public void bindEvents(Event event){
            eventTitleTv.setText(event.getTitle());

            dayTv.setText(event.getDay());
            monthTv.setText(event.getMonth());
            yearTv.setText(event.getYear());
            startTimeTv.setText(event.getStartTime());
            endTimeTv.setText(event.getEndTime());
        }
    }
}
