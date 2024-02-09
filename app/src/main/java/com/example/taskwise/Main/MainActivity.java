package com.example.taskwise.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.example.taskwise.Calendar.CalendarActivity;
import com.example.taskwise.ContextWrapper;
import com.example.taskwise.DataBase.AppDataBase;
import com.example.taskwise.DataBase.DBDao;
import com.example.taskwise.EventDatail.EventDetailActivity;
import com.example.taskwise.FeedBack.FeedBackActivity;
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
import com.example.taskwise.Welcome.UserInfoActivity;
import com.example.taskwise.Welcome.WelcomeActivity;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements TaskAdapter.changeListener, EventAdapter.changeListener,  MainContract.view  {

    LinearLayout emptyState , taskList , tap;
    TextView headerTv , helloTv , plansMessage , weekDayTv ,  monthTv ,  userNameTv , userExpertiseTv ;
    ExtendedFloatingActionButton addPlanBtn;
    FloatingActionButton addEventBtn , addTaskBtn;
    ImageView drawerToggle , singleListEmptyState , emptyStateImg;
    DrawerLayout drawerLayout_parent;
    NavigationView navigationMain;
    View navigationHeader;
    TextClock clockTv;
    RelativeLayout calendarBtn , editBtn;
    TabLayout tabLayout;
    NestedScrollView nestedScrollView;
    RecyclerView listRv;
    Dialog dialog;

    TaskAdapter taskAdapter;
    EventAdapter eventAdapter;
    MainPresentor presentor;
    DBDao dao;
    AppSettingContainer settingContainer;
    UserInfoContainer userInfoContainer;

    int selectedTab = 0;
    boolean allFabsVisible;
    public static final int NOTIFICATION_REQUEST_CODE = 101;

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
        tap = findViewById(R.id.tap);

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

        dao = AppDataBase.getAppDataBase(this).getDataBaseDao();
        userInfoContainer = new UserInfoContainer(this);

        presentor = new MainPresentor(dao , userInfoContainer , settingContainer);
        taskAdapter = new TaskAdapter(this , this);
        eventAdapter = new EventAdapter(this , this);
        listRv.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        presentor.onAttach(this);
        presentor.validatingFirstUse(settingContainer.getFirstUse());

        extendedFab();
        setUpTabLayout();
        navigationDrawer();

        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , CalendarActivity.class);
                startActivity(intent);
            }
        });

    }

    //Activity Cycle
    @Override
    protected void onResume() {
        super.onResume();
        nestedScrollView.scrollTo(0,0);
        //Setting the view settings
        ContextWrapper.wrap(this);
        ContextWrapper.setTheme(this , settingContainer.getAppTheme());
        //When we get back to activity fab should shrink
        extendedFabShrink();
        presentor = new MainPresentor(dao , userInfoContainer , settingContainer);
        presentor.onAttach(this);
        presentor.switchTab(selectedTab);
        if (selectedTab==0){
            listRv.setAdapter(taskAdapter);
        } else
            listRv.setAdapter(eventAdapter);

        if (settingContainer.getFirstUse())
            settingContainer.saveUserFirstUse(false);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ContextWrapper.wrap(newBase));
    }

    //Item's Events
    @Override
    public void onUpdate(Task task) {
        presentor.updateTask(task);
        taskAdapter.updateTask(task);
        extendedFabShrink();
    }

    @Override
    public void onDelete(Task task) {
        presentor.deleteTask(task);
        taskAdapter.deleteTask(task);
    }

    @Override
    public void onClick(Task task) {
        Intent onClickIntent = new Intent(MainActivity.this , TaskDetailActivity.class);
        onClickIntent.putExtra("task" , task);
        startActivity(onClickIntent);
    }

    @Override
    public void onDelete(Event event) {
        presentor.deleteEvent(event);
        eventAdapter.deleteEvent(event);
    }

    @Override
    public void onClick(Event event) {
        Intent onClickIntent = new Intent(MainActivity.this , EventDetailActivity.class);
        onClickIntent.putExtra("event" , event);
        startActivity(onClickIntent);
    }

    //Home Page Interface
    @Override
    public void setHeaderTexts(String name , int plans) {
        helloTv.setText( getString(R.string.hi) + " " + name );
        plansMessage.setText(plans);
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
                        presentor = new MainPresentor(dao , userInfoContainer , settingContainer);
                        presentor.onAttach(MainActivity.this);
                        if (selectedTab == 1)
                            presentor.switchTab(1);
                        else
                            presentor.switchTab(0);
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
                        presentor = new MainPresentor(dao , userInfoContainer , settingContainer);
                        presentor.onAttach(MainActivity.this);
                        presentor.switchTab(selectedTab);
                        drawerLayout_parent.close();
                        break;

                    case R.id.settingBtn:
                        Intent settingIntent = new Intent(MainActivity.this , SettingActivity.class);
                        startActivity(settingIntent);
                        drawerLayout_parent.close();
                        break;

                    case R.id.contactDev:
                        Intent intent = new Intent(MainActivity.this , FeedBackActivity.class);
                        startActivity(intent);
                        drawerLayout_parent.close();
                        break;

                }
                return false;
            }
        });

        drawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout_parent.openDrawer(GravityCompat.START);
                extendedFabShrink();
            }
        });

        tap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extendedFabShrink();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , UserInfoActivity.class);
                intent.putExtra("edit" , true);
                startActivity(intent);
                drawerLayout_parent.close();
            }
        });
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

    //Showing Lists
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

    //FirsUse Impression
    @Override
    public void goToWelcomeActivity() {
        Intent intent = new Intent(this , WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showPermissionDialog() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, "android.permission.POST_NOTIFICATIONS") != PackageManager.PERMISSION_GRANTED){
            dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.activity_permission_dialog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            AppCompatButton permissionBtn = dialog.findViewById(R.id.permissionBtn);

            permissionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {"android.permission.POST_NOTIFICATIONS"}, NOTIFICATION_REQUEST_CODE);
                    dialog.dismiss();
                    settingContainer.saveUserFirstUse(false);
                    presentor.validatingFirstUse(false);
                }
            });

            dialog.show();
        }
    }

    //Empty States
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

    //Extended Floating Action Buttons
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
                Intent intent = new Intent(MainActivity.this , TaskDetailActivity.class);
                startActivity(intent);
            }
        });

        addEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    //TabLayout
    public void setUpTabLayout(){
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tasks));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.events));
        TabLayout.Tab tab = tabLayout.getTabAt(selectedTab);
        animateTab(tab , true);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTab = tab.getPosition();
                presentor.switchTab(selectedTab);
                animateTab(tab , true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                animateTab(tab , false);
                extendedFabShrink();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void animateTab(@NonNull TabLayout.Tab tab, boolean isSelected) {
        //Old version with default ripple effect on!
//        View tabView = tab.view;
//
//        float scaleFactor = isSelected ? 1.2f : 1f;
//        float alphaValue = isSelected ? 1f : 0.7f;
//
//        // Set up the ObjectAnimator for scale property
//        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(tabView, View.SCALE_X, scaleFactor);
//        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(tabView, View.SCALE_Y, scaleFactor);
//
//        // Set up the ObjectAnimator for alpha property
//        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(tabView, View.ALPHA, alphaValue);
//
//        // Set the interpolators for smooth acceleration and deceleration
//        scaleXAnimator.setInterpolator(isSelected ? new AccelerateInterpolator() : new DecelerateInterpolator());
//        scaleYAnimator.setInterpolator(isSelected ? new AccelerateInterpolator() : new DecelerateInterpolator());
//        alphaAnimator.setInterpolator(isSelected ? new AccelerateInterpolator() : new DecelerateInterpolator());
//
//        // Set the duration for the animations
//        int animationDuration = 200;
//        scaleXAnimator.setDuration(animationDuration);
//        scaleYAnimator.setDuration(animationDuration);
//        alphaAnimator.setDuration(animationDuration);
//
//        // Create an AnimatorSet to play all animations together
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(scaleXAnimator, scaleYAnimator, alphaAnimator);
//
//        // Start the animation
//        animatorSet.start();


        View tabView = tab.view;

        float scaleFactor = isSelected ? 1.2f : 1f;

        // Set up the ObjectAnimator for scale property
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(tabView, View.SCALE_X, scaleFactor);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(tabView, View.SCALE_Y, scaleFactor);

        // Set the interpolators for smooth acceleration and deceleration
        scaleXAnimator.setInterpolator(isSelected ? new AccelerateInterpolator() : new DecelerateInterpolator());
        scaleYAnimator.setInterpolator(isSelected ? new AccelerateInterpolator() : new DecelerateInterpolator());

        // Set the duration for the animations
        int animationDuration = 200;
        scaleXAnimator.setDuration(animationDuration);
        scaleYAnimator.setDuration(animationDuration);

        // Create an AnimatorSet to play scale animations together
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);

        // Set a transparent background to remove the ripple effect
        tabView.setBackgroundResource(0);

        // Start the animation
        animatorSet.start();
    }

}