package com.example.taskwise.Main.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskwise.Model.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(event.getFirstDate());

            yearTv.setText(yearSdf.format(calendar.getTime()));
            monthTv.setText(monthSdf.format(calendar.getTime()));
            dayTv.setText(daySdf.format(calendar.getTime()));
            startTimeTv.setText(timeSdf.format(event.getFirstDate()));
            endTimeTv.setText(timeSdf.format(event.getSecondDate()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(event);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenu popupMenu = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                        popupMenu = new PopupMenu(c, v , Gravity.END , 0 , R.style.popUpMenuStyle);
                        MenuInflater inflater = popupMenu.getMenuInflater();
                        inflater.inflate(R.menu.hold_item_menu, popupMenu.getMenu());
                        popupMenu.show();

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()){
                                    case R.id.deleteBtn:
                                        listener.onDelete(event);
                                        return true;

                                    default:
                                        return false;
                                }
                            }
                        });
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    public interface changeListener{
        public void onDelete(Event event);

        public void onClick(Event event);
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public void deleteEvent(Event event){
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getId() == event.getId()) {
                events.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

}
