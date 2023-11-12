package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.taskmanager.Adapter.TaskAdapter;
import com.example.taskmanager.DataBase.AppDataBase;
import com.example.taskmanager.DataBase.TaskDao;
import com.example.taskmanager.Main.MainContract;
import com.example.taskmanager.Main.MainPresentor;
import com.example.taskmanager.Model.Task;
import com.example.taskmanager.Model.TaskCategory;
import com.example.taskmanager.SharedPreferences.AppSettingContainer;
import com.example.taskmanager.SharedPreferences.UserInfoContainer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.zires.switchsegmentedcontrol.ZiresSwitchSegmentedControl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements TaskAdapter.changeListener , MainContract.view {

    RecyclerView tasksRv;
    LinearLayout emptyState , taskList ;
    ZiresSwitchSegmentedControl taskListSwitch;
    TextView headerTv , nameTv , plansNumberTv , weekDayTv ,  monthTv ,  userNameTv , userExpertiseTv ;
    FloatingActionButton addTaskBtn;
    ImageView drawerToggle, editBtn , singleCatEmptyState , emptyStateImg;
    DrawerLayout drawerLayout_parent;
    NavigationView navigationMain;
    View navigationHeader;
    TextClock clockTv;


    TaskDao dao;
    TaskAdapter adapter;
    UserInfoContainer container;
    AppSettingContainer settingContainer;

    MainPresentor presentor;

    public void cast(){
        taskList = findViewById(R.id.taskList);
        emptyState = findViewById(R.id.emptyState);
        nameTv = findViewById(R.id.nameTv);
        plansNumberTv = findViewById(R.id.plansNumberTv);
        weekDayTv = findViewById(R.id.weekDayTv);
        monthTv = findViewById(R.id.monthTv);
        addTaskBtn = findViewById(R.id.addTaskBtn);
        tasksRv = findViewById(R.id.tasksRv);
        clockTv = findViewById(R.id.clockTv);
        taskListSwitch = findViewById(R.id.taskListSwitch);
        singleCatEmptyState = findViewById(R.id.singleCatEmptyState);
        headerTv = findViewById(R.id.headerTv);
        drawerToggle = findViewById(R.id.drawerToggle);
        drawerLayout_parent = findViewById(R.id.drawerLayout_parent);
        navigationMain = findViewById(R.id.navigationMain);
        emptyStateImg = findViewById(R.id.emptyStateImg);

        //NavigationCasting
        navigationHeader = navigationMain.getHeaderView(0);
        userNameTv = navigationHeader.findViewById(R.id.userNameTv);
        userExpertiseTv = navigationHeader.findViewById(R.id.userExpertiseTv);
        editBtn = navigationHeader.findViewById(R.id.editBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingContainer = new AppSettingContainer(this);
        ContextWrapper.setTheme(this , settingContainer.getAppTheme());
        setContentView(R.layout.activity_main);

        cast();
        presentor = new MainPresentor(AppDataBase.getAppDataBase(this).getDataBaseDao() , new UserInfoContainer(this) , new AppSettingContainer(this));
        adapter = new TaskAdapter(this , this);
        presentor.onAttach(this);
        presentor.validatingUserInfo();

        getDate();

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , TaskDetailActivity.class);
                startActivity(intent);
            }
        });

        taskListSwitch.setRightToggleText(getString(R.string.allTasksSwitch));
        taskListSwitch.setLeftToggleText(getString(R.string.todayTasksSwitch));
        taskListSwitch.setOnToggleSwitchChangeListener(new ZiresSwitchSegmentedControl.OnSwitchChangeListener() {
            @Override
            public void onToggleSwitchChangeListener(boolean b) {
                    presentor.listSwitch(b);
                }
        });

        navigationDrawer();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presentor = new MainPresentor(AppDataBase.getAppDataBase(this).getDataBaseDao() , new UserInfoContainer(this) , new AppSettingContainer(this));
        presentor.onAttach(this);

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


    public int setIlls(int theme){
        if (theme == 0)
            return R.drawable.il_empty_state_green;

        else if (theme == 1)
            return R.drawable.il_empty_state_nude;

        else if (theme == 2)
            return R.drawable.il_empty_state_ivory;

        else
            return R.drawable.il_empty_state_blue;
    }

    //Calendar Section
    public void getDate(){
        Calendar calendar = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat weekDayFormat = new SimpleDateFormat("EEEE" );
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

                    case R.id.settingBtn:
                        Intent settingIntent = new Intent(MainActivity.this , SettingActivity.class);
                        startActivity(settingIntent);
                }
                return false;
            }
        });

        drawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout_parent.openDrawer(GravityCompat.START);
            }
        });




        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , WelcomeActivity.class);
                intent.putExtra("key" , 1);
                startActivity(intent);
            }
        });
    }


    @Override
    public void setHeaderTexts(String name, int taskTime, int tasksCount) {
        nameTv.setText( getString(R.string.hi) + " " + name );
        headerTv.setText(taskTime);
        plansNumberTv.setText("( " + tasksCount + " " + getString(R.string.plans) + " )");
    }

    @Override
    public void setDate() {

    }

    @Override
    public void showTasks(List<Task> tasks) {
        tasksRv.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        adapter.setTasks(tasks);
        tasksRv.setAdapter(adapter);
    }


    @Override
    public void updateTask(Task task) {

    }

    @Override
    public void deleteTask() {

    }


    @Override
    public void goToWelcomeActivity() {
        Intent intent = new Intent(this , WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setEmptyStateVisibility(boolean visible , int theme) {
        emptyState.setVisibility(visible ? View.VISIBLE : View.GONE);
        emptyStateImg.setImageResource(setIlls(theme));
        plansNumberTv.setVisibility(visible ? View.GONE : View.VISIBLE);
        taskList.setVisibility(visible ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setListEmptyStateVisibility(boolean visibile , int theme) {
        tasksRv.setVisibility(visibile ? View.GONE : View.VISIBLE);
        singleCatEmptyState.setVisibility(visibile ? View.VISIBLE : View.GONE);
        singleCatEmptyState.setImageResource(setIlls(theme));
    }
}