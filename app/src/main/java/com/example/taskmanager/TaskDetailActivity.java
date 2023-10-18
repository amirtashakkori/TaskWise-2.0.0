package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.DataBase.AppDataBase;
import com.example.taskmanager.DataBase.TaskDao;
import com.example.taskmanager.Model.Task;
import com.example.taskmanager.WorkManager.TaskListStatusUpdater;

import java.util.concurrent.TimeUnit;


public class TaskDetailActivity extends AppCompatActivity {

    EditText taskTitleEt , descriptionEt;
    AppCompatButton submitTaskBtn;
    TextView spinnerTv , headerTv;
    Spinner timePeriodSpinner , importanceSpinner;
    RelativeLayout deleteTaskBtn;
    ImageView backBtn;

    Task task;
    TaskDao dao;
    int time_period , importance = 1;

    public void cast(){

        taskTitleEt = findViewById(R.id.taskTitleEt);
        descriptionEt = findViewById(R.id.descriptionEt);
        timePeriodSpinner = findViewById(R.id.timePeriodSpinner);
        spinnerTv = findViewById(R.id.spinnerTv);
        submitTaskBtn = findViewById(R.id.submitTaskBtn);
        importanceSpinner = findViewById(R.id.importanceSpinner);
        headerTv = findViewById(R.id.headerTv);
        deleteTaskBtn = findViewById(R.id.deleteTaskBtn);
        backBtn = findViewById(R.id.backBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        cast();

        dao = AppDataBase.getAppDataBase(this).getDataBaseDao();
        setSpinners();

        task = getIntent().getParcelableExtra("task");

        if (task != null){
            taskTitleEt.setText(task.getTitle());
            descriptionEt.setText(task.getDescription());
            timePeriodSpinner.setSelection(task.getTime_period());
            importanceSpinner.setSelection(task.getImportance());

            headerTv.setText("Update Task");
            submitTaskBtn.setText("Save Changes");

            deleteTaskBtn.setVisibility(View.VISIBLE);

        }


        submitTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String taskTitle = taskTitleEt.getText().toString();
                String taskDescription = descriptionEt.getText().toString();

                if (taskTitle.length() > 0 && taskDescription.length() > 0){
                    if (task != null){
                        task.setTitle(taskTitleEt.getText().toString());
                        task.setDescription(descriptionEt.getText().toString());
                        task.setTime_period(time_period);
                        task.setImportance(importance);

                        int res = dao.update(task);
                        if (res > 0){
                            Toast.makeText(TaskDetailActivity.this, "Seccessfuly updated!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                    else{
                        task = new Task();
                        task.setTitle(taskTitle);
                        task.setDescription(taskDescription);
                        task.setTime_period(time_period);
                        task.setImportance(importance);

                        dao.addTask(task);

                        Data data = new Data.Builder().putString("taskInfo" , taskTitle ).build();

                        WorkManager manager = WorkManager.getInstance(TaskDetailActivity.this);
                        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(TaskListStatusUpdater.class)
                                .setInputData(data)
                                .setInitialDelay(getTaskExpiredTime(task) , TimeUnit.DAYS)
                                .build();
                        manager.enqueue(request);

                        finish();
                    }
                }
                else if (taskTitleEt.length() == 0)
                    taskTitleEt.setError(R.string.titleError + "");

                else if(descriptionEt.length() == 0)
                    descriptionEt.setError(R.string.descriptionError + "");

            }
        });

        deleteTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = dao.delete(task);
                if (res > 0){
                    Toast.makeText(TaskDetailActivity.this, R.string.edited, Toast.LENGTH_SHORT).show();
                    finish();

                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setSpinners(){
        String[] period_spinner_items = {getString(R.string.inDayAhead) , getString(R.string.inThreeDays) , getString(R.string.inThisWeek) , getString(R.string.inThisMonth)};
        ArrayAdapter<String> periodSpinnerAdapter = new ArrayAdapter<String>(this , R.layout.item_period_spinner , R.id.spinnerTv , period_spinner_items);
        periodSpinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        timePeriodSpinner.setAdapter(periodSpinnerAdapter);

        String[] importance_spinner_items = {getString(R.string.highImportance) , getString(R.string.normalPriority) , getString(R.string.lowPriority)};
        ArrayAdapter<String> importanceSpinnerAdapter = new ArrayAdapter<String>(this , R.layout.item_importance_spinner , R.id.spinnerTv , importance_spinner_items);
        importanceSpinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        importanceSpinner.setAdapter(importanceSpinnerAdapter);
        importanceSpinner.setSelection(1);


        timePeriodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                time_period = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        importanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                importance = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public int getTaskExpiredTime(Task task){
        if (task.getTime_period() == 1)
            return 1;
        else if (task.getTime_period() == 2)
            return 3;
        else if (task.getTime_period() == 3)
            return 7;
        else
            return 30;
    }
}