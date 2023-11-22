package com.example.taskwise.TaskList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.taskwise.ContextWrapper;
import com.example.taskwise.Main.Adapter.TaskAdapter;
import com.example.taskwise.DataBase.AppDataBase;
import com.example.taskwise.Model.Task;
import com.example.taskmanager.R;
import com.example.taskwise.SharedPreferences.AppSettingContainer;
import com.example.taskwise.TaskDetail.TaskDetailActivity;

import java.util.List;

public class TaskListActivity extends AppCompatActivity implements TaskAdapter.changeListener, TaskListContract.view{

    TextView headerTv , emptyStateTv;
    RecyclerView rv_tasks;
    RelativeLayout deleteAllBtn , backBtn , emptyState;
    NestedScrollView nested;
    ImageView img_empty_state;

    TaskAdapter adapter;
    AppSettingContainer settingContainer;

    TaskListContract.presentor presentor;

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
        settingContainer = new AppSettingContainer(this);
        ContextWrapper.setTheme(this , settingContainer.getAppTheme());
        setContentView(R.layout.activity_task_list);
        cast();
        presentor = new TaskListPresentor(AppDataBase.getAppDataBase(this).getDataBaseDao() , getIntent().getIntExtra("listNum" , 0) , new AppSettingContainer(this));
        adapter = new TaskAdapter(this , this);
        presentor.onAttach(this);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentor.deleteAllButtonClicked();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        presentor = new TaskListPresentor(AppDataBase.getAppDataBase(this).getDataBaseDao() , getIntent().getIntExtra("listNum" , 0) , new AppSettingContainer(this));
        presentor.onAttach(this);
    }

    @Override
    public void onUpdate(Task task) {
        presentor.updateTask(task);
        adapter.updateTask(task);

        presentor = new TaskListPresentor(AppDataBase.getAppDataBase(this).getDataBaseDao() , getIntent().getIntExtra("listNum" , 0) , new AppSettingContainer(this));
        presentor.onAttach(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ContextWrapper.wrap(newBase));
    }

    @Override
    public void onClick(Task task) {
        Intent onClickIntent = new Intent(TaskListActivity.this , TaskDetailActivity.class);
        onClickIntent.putExtra("task" , task);
        startActivity(onClickIntent);
    }


    public int setCompletedIlls(int theme){
        if (theme == 0)
            return R.drawable.il_completed_es_green;

        else if (theme == 1)
            return R.drawable.il_completed_es_ivory;

        else
            return R.drawable.il_completed_es_blue;
    }

    public int setOutdatedIlls(int theme){
        if (theme == 0)
            return R.drawable.il_outdated_es_green;

        else if (theme == 1)
            return R.drawable.il_outdated_es_ivory;

        else
            return R.drawable.il_outdated_es_blue;
    }

    @Override
    public void showList(List<Task> tasks) {
        adapter.setTasks(tasks);
        rv_tasks.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        rv_tasks.setAdapter(adapter);
    }

    @Override
    public void setDeleteButtonVisibility(boolean visible) {
        deleteAllBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setEmptyStateVisibility(boolean visible , int es , int appTheme) {
        emptyState.setVisibility(visible ? View.VISIBLE : View.GONE);
        nested.setVisibility(visible ? View.GONE : View.VISIBLE);
        if (es == 1)
            img_empty_state.setImageResource(setCompletedIlls(appTheme));

        else
            img_empty_state.setImageResource(setOutdatedIlls(appTheme));
    }
}