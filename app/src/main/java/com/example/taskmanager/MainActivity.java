package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.Adapter.TaskAdapter;
import com.example.taskmanager.DataBase.AppDataBase;
import com.example.taskmanager.DataBase.TaskDao;
import com.example.taskmanager.Model.Task;
import com.example.taskmanager.SharedPreferences.UserInfoContainer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.zires.switchsegmentedcontrol.ZiresSwitchSegmentedControl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.IllegalFormatCodePointException;
import java.util.List;


public class MainActivity extends AppCompatActivity implements TaskAdapter.changeListener {

    RecyclerView tasksRv;
    RelativeLayout searchBtn, taskList ; ;
    LinearLayout emptyState ;
    ZiresSwitchSegmentedControl taskListSwitch;
    TextView headerTv , nameTv , plansNumberTv , weekDayTv ,  monthTv , clockTv ,  userNameTv , userExpertiseTv ;
    FloatingActionButton addTaskBtn;
    ImageView drawerToggle, editBtn , singleCatEmptyState;
    DrawerLayout drawerLayout_parent;
    NavigationView navigationMain;
    View navigationHeader;

    TaskDao dao;
    List<Task> allTasks;
    List<Task> todayTasks;
    TaskAdapter adapter;
    UserInfoContainer container;

    public void cast(){
        taskList = findViewById(R.id.taskList);
        emptyState = findViewById(R.id.emptyState);
        nameTv = findViewById(R.id.nameTv);
        plansNumberTv = findViewById(R.id.plansNumberTv);
        weekDayTv = findViewById(R.id.weekDayTv);
        monthTv = findViewById(R.id.monthTv);
        clockTv = findViewById(R.id.clockTv);
        addTaskBtn = findViewById(R.id.addTaskBtn);
        tasksRv = findViewById(R.id.tasksRv);
        searchBtn = findViewById(R.id.searchBtn);
        taskListSwitch = findViewById(R.id.taskListSwitch);
        singleCatEmptyState = findViewById(R.id.singleCatEmptyState);
        headerTv = findViewById(R.id.headerTv);
        drawerToggle = findViewById(R.id.drawerToggle);
        drawerLayout_parent = findViewById(R.id.drawerLayout_parent);
        navigationMain = findViewById(R.id.navigationMain);

        //NavigationCasting
        navigationHeader = navigationMain.getHeaderView(0);
        userNameTv = navigationHeader.findViewById(R.id.userNameTv);
        userExpertiseTv = navigationHeader.findViewById(R.id.userExpertiseTv);
        editBtn = navigationHeader.findViewById(R.id.editBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cast();

        container = new UserInfoContainer(this);
        String name = container.getName();
        if (name.equals("")){
            Intent intent = new Intent(this , WelcomeActivity.class);
            startActivity(intent);
        }

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

        taskListSwitch.setOnToggleSwitchChangeListener(new ZiresSwitchSegmentedControl.OnSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(boolean b) {
                //this is the second one
                //we have to write code for all tasks here
                if (b == false){
                    if (allTasks.size() > 0){
                        tasksRv.setVisibility(View.VISIBLE);
                        singleCatEmptyState.setVisibility(View.GONE);
                        adapter = new TaskAdapter(MainActivity.this , allTasks , MainActivity.this);
                        tasksRv.setAdapter(adapter);
                    } else {
                        tasksRv.setVisibility(View.GONE);
                        singleCatEmptyState.setVisibility(View.VISIBLE);
                    }
                }

                //This is the first one
                else{
                    if (todayTasks.size() > 0){
                        tasksRv.setVisibility(View.VISIBLE);
                        singleCatEmptyState.setVisibility(View.GONE);
                        adapter = new TaskAdapter(MainActivity.this , todayTasks , MainActivity.this);
                        tasksRv.setAdapter(adapter);
                    } else {
                        tasksRv.setVisibility(View.GONE);
                        singleCatEmptyState.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        navigationDrawer();

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
            singleCatEmptyState.setVisibility(View.GONE);
            adapter = new TaskAdapter(MainActivity.this , todayTasks,MainActivity.this);
            tasksRv.setAdapter(adapter);

        }

        nameTv.setText(R.string.hi + " " + container.getName());
        userNameTv.setText(container.getName() + " " + container.getFamily());
        userExpertiseTv.setText(container.getExpertise());
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

            headerTv.setText(R.string.todayTasks);
            plansNumberTv.setText("(" + todayTasks.size() + " " + R.string.plans + " )");
            plansNumberTv.setVisibility(View.VISIBLE);

            emptyState.setVisibility(View.GONE);
            taskList.setVisibility(View.VISIBLE);


            adapter = new TaskAdapter(MainActivity.this , todayTasks,MainActivity.this);
            tasksRv.setAdapter(adapter);

        } else if (todayTasks.size() == 0 && allTasks.size() > 0){
            taskListSwitch.setChecked(false);

            headerTv.setText("Your Future Tasks");
            plansNumberTv.setVisibility(View.VISIBLE);
            plansNumberTv.setText("(" + allTasks.size() + " Plans)");

            tasksRv.setVisibility(View.GONE);
            singleCatEmptyState.setVisibility(View.VISIBLE);

            emptyState.setVisibility(View.GONE);
            taskList.setVisibility(View.VISIBLE);

        } else if (todayTasks.size() == 0 && allTasks.size() == 0){
            headerTv.setText(R.string.taskManager);
            plansNumberTv.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
            taskList.setVisibility(View.GONE);
        }
    }

    //Calendar Section
    public void getDate(){
        Calendar calendar = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat weekDayFormat = new SimpleDateFormat("EEEE");
        weekDayTv.setText(weekDayFormat.format(calendar.getTime()));

        @SuppressLint("SimpleDateFormat") SimpleDateFormat monthNameFormat = new SimpleDateFormat("dd MMMM");
        monthTv.setText( monthNameFormat.format(calendar.getTime()));
    }

    public void navigationDrawer(){

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawerLayout_parent , R.string.openNavigation , R.string.closeNavigation);
        drawerLayout_parent.addDrawerListener(toggle);
        navigationMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.deleteAllBtn:
                        dao.deleteAll();
                        headerTv.setText("Task Manager");
                        plansNumberTv.setVisibility(View.GONE);
                        taskList.setVisibility(View.GONE);
                        emptyState.setVisibility(View.VISIBLE);
                        drawerLayout_parent.close();
                        break;

                    case R.id.completedBtn:
                        Intent completedIntent = new Intent(MainActivity.this , TaskListActivity.class);
                        completedIntent.putExtra("listNum" , 1);
                        startActivity(completedIntent);
                        drawerLayout_parent.close();
                        break;

                    case R.id.unspecifiedBtn:
                        Intent unspecifiedIntent = new Intent(MainActivity.this , TaskListActivity.class);
                        unspecifiedIntent.putExtra("listNum" , 2);
                        startActivity(unspecifiedIntent);
                        drawerLayout_parent.close();
                        break;
                }
                return false;
            }
        });

        drawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout_parent.openDrawer(Gravity.START);
            }
        });


        userNameTv.setText(container.getName() + " " + container.getFamily());
        userExpertiseTv.setText(container.getExpertise());

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , WelcomeActivity.class);
                intent.putExtra("key" , 1);
                startActivity(intent);
            }
        });
    }


}