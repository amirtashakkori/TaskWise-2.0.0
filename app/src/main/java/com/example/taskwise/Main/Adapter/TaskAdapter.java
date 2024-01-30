package com.example.taskwise.Main.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskwise.Model.Task;
import com.example.taskmanager.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.item> {

    Context c;
    List<Task> tasks;
    changeListener listener;

    public TaskAdapter(Context c, changeListener listener) {
        this.c = c;
        this.listener = listener;
    }

    @NonNull
    @Override
    public item onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new item(LayoutInflater.from(c).inflate(R.layout.item_task , viewGroup , false));
    }

    @Override
    public void onBindViewHolder(@NonNull item item, int postition) {
        item.bindTasks(tasks.get(postition));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class item extends RecyclerView.ViewHolder{
        TextView taskTitleTv , taskDescriptionTv;
        LinearLayout btnCheck;
        AppCompatButton importanceBtn;
        ImageView checkImg;
        public item(@NonNull View itemView) {
            super(itemView);

            taskTitleTv = itemView.findViewById(R.id.taskTitleTv);
            taskDescriptionTv = itemView.findViewById(R.id.taskDescriptionTv);
            btnCheck = itemView.findViewById(R.id.btnCheck);
            importanceBtn = itemView.findViewById(R.id.importanceBtn);
            checkImg = itemView.findViewById(R.id.checkImg);

        }

        public void bindTasks(Task task){
            taskTitleTv.setText(task.getTitle());
            taskDescriptionTv.setText(task.getDescription());

            if (task.getImportance() == 2){
                importanceBtn.setVisibility(View.VISIBLE);
                importanceBtn.setText("Low Priority");
                importanceBtn.setBackgroundResource(R.drawable.bg_importance_low);
            }
            else if (task.getImportance() == 0){
                importanceBtn.setVisibility(View.VISIBLE);
                importanceBtn.setText("Important");
                importanceBtn.setBackgroundResource(R.drawable.bg_importance_high);
            } else {
                importanceBtn.setVisibility(View.GONE);
            }

            if (task.getIs_completed() == 0){
                btnCheck.setBackgroundResource(R.drawable.bg_checkbox);}
            else {
                btnCheck.setBackgroundResource(R.drawable.bg_checkbox_checked);
                checkImg.setImageResource(R.drawable.ic_check);
            }

            btnCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (task.getIs_completed() == 0) {
                        task.setIs_completed(1);
                        btnCheck.setBackgroundResource(R.drawable.bg_checkbox);
                        listener.onUpdate(task);
                    }
                    else{
                        task.setIs_completed(0);
                        btnCheck.setBackgroundResource(R.drawable.bg_checkbox_checked);
                        listener.onUpdate(task);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(task);
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
                                        listener.onDelete(task);
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
        public void onUpdate(Task task);

        public void onDelete(Task task);

        public void onClick(Task task);
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public void updateTask(Task task){
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()){
                tasks.set(i , task);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void deleteTask(Task task){
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }
}
