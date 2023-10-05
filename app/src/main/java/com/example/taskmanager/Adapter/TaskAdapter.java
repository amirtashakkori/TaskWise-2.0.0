package com.example.taskmanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.Model.Task;
import com.example.taskmanager.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.item> {

    Context c;
    List<Task> tasks;
    changeListener listener;

    public TaskAdapter(Context c, List<Task> tasks) {
        this.c = c;
        this.tasks = tasks;
    }

    public TaskAdapter(Context c, List<Task> tasks, changeListener listener) {
        this.c = c;
        this.tasks = tasks;
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
        public item(@NonNull View itemView) {
            super(itemView);

            taskTitleTv = itemView.findViewById(R.id.taskTitleTv);
            taskDescriptionTv = itemView.findViewById(R.id.taskDescriptionTv);
            btnCheck = itemView.findViewById(R.id.btnCheck);
            importanceBtn = itemView.findViewById(R.id.importanceBtn);

        }

        public void bindTasks(Task task){
            taskTitleTv.setText(task.getTitle());
            taskDescriptionTv.setText(task.getDescription());

            if (task.getImportance() == 2){
                importanceBtn.setText("Low Priority");
                importanceBtn.setBackgroundResource(R.drawable.bg_importance_low);
            }
            else if (task.getImportance() == 0){
                importanceBtn.setText("Important");
                importanceBtn.setBackgroundResource(R.drawable.bg_importance_high);
            } else {
                importanceBtn.setVisibility(View.GONE);
                importanceBtn.setVisibility(View.GONE);
            }

            if (task.getIs_completed() == 0)
                btnCheck.setBackgroundResource(R.drawable.bg_checkbox);
            else
                btnCheck.setBackgroundResource(R.drawable.bg_checkbox_checked);

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
        }

    }

    public interface changeListener{
        public void onUpdate(Task task);

        public void onClick(Task task);
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

    public void updateTask(Task task){
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()){
                tasks.set(i , task);
                notifyItemChanged(i);
                break;
            }
        }
    }
}
