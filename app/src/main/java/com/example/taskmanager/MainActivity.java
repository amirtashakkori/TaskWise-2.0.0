package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.Adapter.TaskAdapter;
import com.example.taskmanager.DataBase.AppDataBase;
import com.example.taskmanager.DataBase.TaskDao;
import com.example.taskmanager.Model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zires.switchsegmentedcontrol.ZiresSwitchSegmentedControl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.changeListener {

    NestedScrollView taskListSv;
    LinearLayout emptyState;
    RecyclerView tasksRv;
    RelativeLayout deleteAllBtn;
    ZiresSwitchSegmentedControl taskListSwitch;
    TextView headerTv , nameTv , plansNumberTv , weekDayTv ,  monthTv , clockTv , allTaskEmptyState;
    FloatingActionButton addTaskBtn;

    TaskDao dao;
    List<Task> allTasks;
    List<Task> todayTasks;
    TaskAdapter adapter;

    public void cast(){
        taskListSv = findViewById(R.id.taskListSv);
        emptyState = findViewById(R.id.emptyState);
        nameTv = findViewById(R.id.nameTv);
        plansNumberTv = findViewById(R.id.plansNumberTv);
        weekDayTv = findViewById(R.id.weekDayTv);
        monthTv = findViewById(R.id.monthTv);
        clockTv = findViewById(R.id.clockTv);
        addTaskBtn = findViewById(R.id.addTaskBtn);
        tasksRv = findViewById(R.id.tasksRv);
        deleteAllBtn = findViewById(R.id.deleteAllBtn);
        taskListSwitch = findViewById(R.id.taskListSwitch);
        allTaskEmptyState = findViewById(R.id.allTaskEmptyState);
        headerTv = findViewById(R.id.headerTv);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cast();

        dao = AppDataBase.getAppDataBase(this).getDataBaseDao();

        bindTasks();

        getDate();

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , TaskDetailActivity.class);
                startActivity(intent);
            }
        });

        deleteAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao.deleteAll();
                headerTv.setText("Task Manager");
                plansNumberTv.setVisibility(View.GONE);
                tasksRv.setVisibility(View.GONE);
                emptyState.setVisibility(View.VISIBLE);
            }
        });

        taskListSwitch.setOnToggleSwitchChangeListener(new ZiresSwitchSegmentedControl.OnSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(boolean b) {
                //this is the second one
                //we have to write code for all tasks here
                if (b == false){
                    if (allTasks.size() > 0){
                        tasksRv.setVisibility(View.VISIBLE);
                        allTaskEmptyState.setVisibility(View.GONE);
                        adapter = new TaskAdapter(MainActivity.this , allTasks , MainActivity.this);
                        tasksRv.setAdapter(adapter);
                    } else {
                        tasksRv.setVisibility(View.GONE);
                        allTaskEmptyState.setVisibility(View.VISIBLE);
                        allTaskEmptyState.setText("You don't have any tasks more than today.!");
                    }
                }

                //This is the first one
                else{
                    if (todayTasks.size() > 0){
                        tasksRv.setVisibility(View.VISIBLE);
                        allTaskEmptyState.setVisibility(View.GONE);
                        adapter = new TaskAdapter(MainActivity.this , todayTasks , MainActivity.this);
                        tasksRv.setAdapter(adapter);
                    } else {
                        tasksRv.setVisibility(View.GONE);
                        allTaskEmptyState.setVisibility(View.VISIBLE);
                        allTaskEmptyState.setText("You don't have any tasks for today.!");
                    }
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindTasks();

        todayTasks = dao.getTodayTaskList();
        allTasks = dao.getTaskList();

        if (todayTasks.size() > 0){
            plansNumberTv.setText("(" + todayTasks.size() + " Plans)");
            emptyState.setVisibility(View.GONE);
            tasksRv.setVisibility(View.VISIBLE);
            allTaskEmptyState.setVisibility(View.GONE);
            adapter = new TaskAdapter(MainActivity.this , todayTasks,MainActivity.this);
            tasksRv.setAdapter(adapter);

        }
    }


    @Override
    public void onUpdate(Task task) {
        dao.update(task);
        adapter.updateTask(task);
    }

    @Override
    public void onClick(Task task) {
        Intent onClickIntent = new Intent(MainActivity.this , TaskDetailActivity.class);
        onClickIntent.putExtra("task" , task);
        startActivity(onClickIntent);
    }


    public void bindTasks(){
        todayTasks = dao.getTodayTaskList();
        allTasks = dao.getTaskList();

        tasksRv.setLayoutManager(new LinearLayoutManager(this , RecyclerView.VERTICAL , false));

        if (todayTasks.size() > 0){
            taskListSwitch.setChecked(false);

            headerTv.setText("Your today Task");
            plansNumberTv.setText("(" + todayTasks.size() + " Plans)");
            plansNumberTv.setVisibility(View.VISIBLE);

            emptyState.setVisibility(View.GONE);
            taskListSv.setVisibility(View.VISIBLE);


            adapter = new TaskAdapter(MainActivity.this , todayTasks,MainActivity.this);
            tasksRv.setAdapter(adapter);

        } else if (todayTasks.size() == 0 && allTasks.size() > 0){
            taskListSwitch.setChecked(false);

            headerTv.setText("Your Future Tasks");
            plansNumberTv.setVisibility(View.VISIBLE);
            plansNumberTv.setText("(" + allTasks.size() + " Plans)");


            allTaskEmptyState.setVisibility(View.VISIBLE);
            allTaskEmptyState.setText("You don't have any task for today!");

            emptyState.setVisibility(View.GONE);
            taskListSv.setVisibility(View.VISIBLE);

        } else if (todayTasks.size() == 0 && allTasks.size() == 0){
            headerTv.setText("Task Manager");
            plansNumberTv.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
            taskListSv.setVisibility(View.GONE);
        }
    }

    //Calendar Section
    public void getDate(){
        Calendar calendar = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat weekDayFormat = new SimpleDateFormat("EEEE");
        weekDayTv.setText(weekDayFormat.format(calendar.getTime()));

        @SuppressLint("SimpleDateFormat") SimpleDateFormat monthNameFormat = new SimpleDateFormat("MMMM");
        monthTv.setText(monthNameFormat.format(calendar.getTime()));
    }

}