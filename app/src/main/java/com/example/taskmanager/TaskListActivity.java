package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.taskmanager.Adapter.TaskAdapter;
import com.example.taskmanager.DataBase.AppDataBase;
import com.example.taskmanager.DataBase.TaskDao;
import com.example.taskmanager.Model.Task;

import java.time.format.TextStyle;
import java.util.List;

public class TaskListActivity extends AppCompatActivity implements TaskAdapter.changeListener{

    TextView headerTv , emptyStateTv;
    RecyclerView rv_tasks;
    RelativeLayout deleteAllBtn;
    RelativeLayout emptyState;
    NestedScrollView nested;
    ImageView img_empty_state , backBtn;

    TaskDao dao;
    List<Task> tasks;
    TaskAdapter adapter;

    public void cast(){
        headerTv = findViewById(R.id.headerTv);
        rv_tasks = findViewById(R.id.rv_tasks);
        deleteAllBtn = findViewById(R.id.deleteAllBtn);
        emptyState = findViewById(R.id.emptyState);
        nested = findViewById(R.id.nested);
        img_empty_state = findViewById(R.id.img_empty_state);
        emptyStateTv = findViewById(R.id.emptyStateTv);
        backBtn = findViewById(R.id.backBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        cast();

        dao = AppDataBase.getAppDataBase(this).getDataBaseDao();
        rv_tasks.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));

        int listNumber = getIntent().getIntExtra("listNum" , 0);

        bindTasks(listNumber);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listNumber == 1){
                    dao.deleteCompletedTasks();
                    bindTasks(listNumber);
                }
                else if (listNumber == 2){
                    dao.deleteUnspecifiedTasks();
                    bindTasks(listNumber);
                }
            }
        });

    }

    @Override
    public void onUpdate(Task task) {
        dao.update(task);
        adapter.updateTask(task);
    }

    @Override
    public void onClick(Task task) {
        Intent onClickIntent = new Intent(TaskListActivity.this , TaskDetailActivity.class);
        onClickIntent.putExtra("task" , task);
        startActivity(onClickIntent);
    }

    public void bindTasks(int listNumber){

        if (listNumber == 1){
            tasks = dao.getCompletedTasks();
            if (tasks.size() > 0) {
                adapter = new TaskAdapter(this , tasks , this);
                rv_tasks.setAdapter(adapter);
                nested.setVisibility(View.VISIBLE);
                emptyState.setVisibility(View.GONE);
                deleteAllBtn.setVisibility(View.VISIBLE);
            }
            else {
                nested.setVisibility(View.GONE);
                emptyState.setVisibility(View.VISIBLE);
                img_empty_state.setImageResource(R.drawable.il_completed_empty_state);
                emptyStateTv.setText(R.string.completedTaskEmptyState);
                deleteAllBtn.setVisibility(View.GONE);
            }
        }

        if (listNumber == 2){
            tasks = dao.getUnspecifiedTasks();
            if (tasks.size() > 0) {
                adapter = new TaskAdapter(this , tasks , this);
                rv_tasks.setAdapter(adapter);
                nested.setVisibility(View.VISIBLE);
                emptyState.setVisibility(View.GONE);
                deleteAllBtn.setVisibility(View.VISIBLE);
            }
            else {
                nested.setVisibility(View.GONE);
                emptyState.setVisibility(View.VISIBLE);
                img_empty_state.setImageResource(R.drawable.il_unspecified_empty_state);
                emptyStateTv.setText(R.string.unspecifiedTasksEmptyState);
                deleteAllBtn.setVisibility(View.GONE);

            }
        }

    }
}