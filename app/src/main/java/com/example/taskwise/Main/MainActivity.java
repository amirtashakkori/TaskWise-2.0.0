package com.example.taskwise.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
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
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.taskwise.Calendar.CalendarActivity;
import com.example.taskwise.ContextWrapper;
import com.example.taskwise.DataBase.AppDataBase;
import com.example.taskwise.EventDatail.EventDetailActivity;
import com.example.taskwise.Main.Adapter.EventAdapter;
import com.example.taskwise.Main.Adapter.TaskAdapter;
import com.example.taskwise.Model.Event;
import com.example.taskwise.Model.Task;
import com.example.taskmanager.R;
import com.example.taskwise.Setting.SettingActivity;
import com.example.taskwise.SharedPreferences.AppSettingContainer;
import com.example.taskwise.SharedPreferences.UserInfoContainer;
import com.example.taskwise.TaskDetail.TaskDetailActivity;
import com.example.taskwise.TaskList.TaskListActivity;
import com.example.taskwise.Welcome.WelcomeActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements TaskAdapter.changeListener, EventAdapter.changeListener,  MainContract.view {

    LinearLayout emptyState , taskList;
    TextView headerTv , helloTv , plansMessage , weekDayTv ,  monthTv ,  userNameTv , userExpertiseTv ;
    ExtendedFloatingActionButton addPlanBtn;
    FloatingActionButton addEventBtn , addTaskBtn;
    ImageView drawerToggle, editBtn , singleListEmptyState , emptyStateImg;
    DrawerLayout drawerLayout_parent;
    NavigationView navigationMain;
    View navigationHeader;
    TextClock clockTv;
    RelativeLayout calendarBtn;
    TabLayout tabLayout;
    NestedScrollView nestedScrollView;
    RecyclerView listRv;

    TaskAdapter taskAdapter;
    EventAdapter eventAdapter;
    AppSettingContainer settingContainer;
    MainPresentor presentor;

    int selectedTab = 0;
    boolean allFabsVisible;

    public void cast(){
        taskList = findViewById(R.id.taskList);
        emptyState = findViewById(R.id.emptyState);
        helloTv = findViewById(R.id.helloTv);
        plansMessage = findViewById(R.id.plansMessage);
        weekDayTv = findViewById(R.id.weekDayTv);
        monthTv = findViewById(R.id.monthTv);
        addPlanBtn = findViewById(R.id.addPlanBtn);
        addEventBtn = findViewById(R.id.addEventBtn);
        addTaskBtn = findViewById(R.id.addTaskBtn);
        clockTv = findViewById(R.id.clockTv);
        singleListEmptyState = findViewById(R.id.singleListEmptyState);
        headerTv = findViewById(R.id.headerTv);
        drawerToggle = findViewById(R.id.drawerToggle);
        drawerLayout_parent = findViewById(R.id.drawerLayout_parent);
        navigationMain = findViewById(R.id.navigationMain);
        calendarBtn = findViewById(R.id.calendarBtn);
        tabLayout = findViewById(R.id.tabLayout);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        emptyStateImg = findViewById(R.id.emptyStateImg);
        listRv = findViewById(R.id.listRv);

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
        taskAdapter = new TaskAdapter(this , this);
        eventAdapter = new EventAdapter(this , this);
        presentor.onAttach(this);
        presentor.validatingUserInfo();

        listRv.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tasks));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.events));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTab = tab.getPosition();
                presentor.switchTab(selectedTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        extendedFab();

        navigationDrawer();

        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , CalendarActivity.class);
                startActivity(intent);
            }
        });

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
        presentor.switchTab(selectedTab);
        if (selectedTab==0){
            listRv.setAdapter(taskAdapter);
        } else
            listRv.setAdapter(eventAdapter);

    }

    @Override
    public void onUpdate(Task task) {
        presentor.updateTask(task);
        taskAdapter.updateTask(task);
    }

    @Override
    public void onClick(Task task) {
        Intent onClickIntent = new Intent(MainActivity.this , TaskDetailActivity.class);
        onClickIntent.putExtra("task" , task);
        startActivity(onClickIntent);
    }

    @Override
    public void onClick(Event event) {
        Intent onClickIntent = new Intent(MainActivity.this , EventDetailActivity.class);
        onClickIntent.putExtra("event" , event);
        startActivity(onClickIntent);
    }


    public void navigationDrawer(){

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this , drawerLayout_parent , R.string.openNavigation , R.string.closeNavigation);
        drawerLayout_parent.addDrawerListener(toggle);
        navigationMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.deleteAllTasksBtn:
                        presentor.clearTaskListClicked();
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

                    case R.id.deleteAllEventsBtn:
                        presentor.clearEventListClicker();
                        drawerLayout_parent.close();
                        break;

                    case R.id.settingBtn:
                        Intent settingIntent = new Intent(MainActivity.this , SettingActivity.class);
                        startActivity(settingIntent);
                        break;

                    case R.id.contactDev:

                        break;

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
    public void setHeaderTexts(String name , int plans) {
        helloTv.setText( getString(R.string.hi) + " " + name );
        plansMessage.setText(plans);
    }

    @Override
    public void setNavigationDrawerText(String fullName, String expertise) {
        userNameTv.setText(fullName);
        userExpertiseTv.setText(expertise);
    }

    @Override
    public void setDate() {
        Calendar calendar = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat weekDayFormat = new SimpleDateFormat("EEEE" );
        weekDayTv.setText(weekDayFormat.format(calendar.getTime()));

        @SuppressLint("SimpleDateFormat") SimpleDateFormat monthNameFormat = new SimpleDateFormat("dd MMMM");
        monthTv.setText( monthNameFormat.format(calendar.getTime()));
    }

    @Override
    public void showTasks(List<Task> tasks) {
        taskAdapter = new TaskAdapter(MainActivity.this , this);
        taskAdapter.setTasks(tasks);
        listRv.setAdapter(taskAdapter);
    }

    @Override
    public void showEvents(List<Event> events) {
        eventAdapter = new EventAdapter(MainActivity.this , this);
        eventAdapter.setEvents(events);
        listRv.setAdapter(eventAdapter);
    }


    @Override
    public void goToWelcomeActivity() {
        Intent intent = new Intent(this , WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setTaskEmptyStateVisibility(boolean visible) {
        emptyState.setVisibility(visible ? View.VISIBLE : View.GONE);
        listRv.setVisibility(visible ? View.GONE : View.VISIBLE);
        emptyStateImg.setImageResource(R.drawable.il_task_empty_state);
    }

    @Override
    public void setEventEmptyStateVisibility(boolean visible) {
        emptyState.setVisibility(visible ? View.VISIBLE : View.GONE);
        listRv.setVisibility(visible ? View.GONE : View.VISIBLE);
        emptyStateImg.setImageResource(R.drawable.il_event_empty_state);
    }

    public void extendedFab(){
        allFabsVisible = false;
        addPlanBtn.shrink();

        addTaskBtn.setVisibility(View.GONE);
        addEventBtn.setVisibility(View.GONE);

        addPlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!allFabsVisible){

                    extendedFabExtend();

                } else {
                    extendedFabShrink();
                }
            }
        });

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extendedFabShrink();

                Intent intent = new Intent(MainActivity.this , TaskDetailActivity.class);
                startActivity(intent);
            }
        });

        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extendedFabShrink();

                Intent intent = new Intent(MainActivity.this , EventDetailActivity.class);
                startActivity(intent);
            }
        });

    }

    public void extendedFabShrink(){
        allFabsVisible = false;
        addTaskBtn.hide();
        addEventBtn.hide();
        addPlanBtn.shrink();
    }

    public void extendedFabExtend(){
        allFabsVisible = true;
        addTaskBtn.show();
        addEventBtn.show();
        addPlanBtn.extend();
    }


}