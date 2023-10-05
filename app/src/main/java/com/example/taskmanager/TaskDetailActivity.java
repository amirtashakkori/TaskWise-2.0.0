package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.DataBase.AppDataBase;
import com.example.taskmanager.DataBase.TaskDao;
import com.example.taskmanager.Model.Task;

public class TaskDetailActivity extends AppCompatActivity {

    EditText taskTitleEt , descriptionEt;
    AppCompatButton submitTaskBtn;
    TextView spinnerTv , headerTv;
    Spinner timePeriodSpinner , importanceSpinner;
    RelativeLayout deleteTaskBtn;

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
                        finish();
                    }
                }
                else if (taskTitleEt.length() == 0)
                    taskTitleEt.setError("Enter the task title!");

                else if(descriptionEt.length() == 0)
                    descriptionEt.setError("Enter a short description for your task!");


            }
        });

        deleteTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = dao.delete(task);
                if (res > 0){
                    Toast.makeText(TaskDetailActivity.this, "Deleted Successfuly!", Toast.LENGTH_SHORT).show();
                    finish();

                }
            }
        });
    }

    public void setSpinners(){
        String[] period_spinner_items = {"in the day ahead" , "In three days" , "in the week ahead" , "in this month"};
        ArrayAdapter<String> periodSpinnerAdapter = new ArrayAdapter<String>(this , R.layout.item_period_spinner , R.id.spinnerTv , period_spinner_items);
        periodSpinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        timePeriodSpinner.setAdapter(periodSpinnerAdapter);

        String[] importance_spinner_items = {"High Importance" , "Normal Priority" , "Low Priority"};
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
}