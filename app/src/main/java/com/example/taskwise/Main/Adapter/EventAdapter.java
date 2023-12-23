package com.example.taskwise.Main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskwise.Model.Event;

import java.text.SimpleDateFormat;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.item> {

    Context c;
    changeListener listener;
    List<Event> events;

    public EventAdapter(Context c, changeListener listener, List<Event> events) {
        this.c = c;
        this.listener = listener;
        this.events = events;
    }

    SimpleDateFormat yearSdf = new SimpleDateFormat("yyyy");
    SimpleDateFormat monthSdf = new SimpleDateFormat("MMM");
    SimpleDateFormat daySdf = new SimpleDateFormat("dd");
    SimpleDateFormat timeSdf = new SimpleDateFormat("hh:mm a");

    public EventAdapter(Context c, changeListener listener) {
        this.c = c;
        this.listener = listener;
    }

    @NonNull
    @Override
    public item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.item_event , parent , false);
        return new item(view);
    }

    @Override
    public void onBindViewHolder(@NonNull item holder, int position) {
        holder.bindEvent(events.get(position));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class item extends RecyclerView.ViewHolder{

        TextView eventTitleTv , yearTv , monthTv , dayTv , startTimeTv , endTimeTv;

        public item(@NonNull View itemView) {
            super(itemView);
            eventTitleTv = itemView.findViewById(R.id.eventTitleTv);
            yearTv = itemView.findViewById(R.id.yearTv);
            monthTv = itemView.findViewById(R.id.monthTv);
            dayTv = itemView.findViewById(R.id.dayTv);
            startTimeTv = itemView.findViewById(R.id.startTimeTv);
            endTimeTv = itemView.findViewById(R.id.endTimeTv);
        }

        public void bindEvent(Event event){
            eventTitleTv.setText(event.getTitle());
            yearTv.setText(yearSdf.format(event.getFirstDate()));
            monthTv.setText(monthSdf.format(event.getFirstDate()));
            dayTv.setText(daySdf.format(event.getFirstDate()));
            startTimeTv.setText(timeSdf.format(event.getFirstDate()));
            endTimeTv.setText(timeSdf.format(event.getSecondDate()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(event);
                }
            });
        }
    }

    public interface changeListener{
        public void onClick(Event event);
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public void updateEvent(Event event){
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getId() == event.getId()){
                events.set(i , event);
                notifyItemChanged(i);
                break;
            }
        }
    }
}
