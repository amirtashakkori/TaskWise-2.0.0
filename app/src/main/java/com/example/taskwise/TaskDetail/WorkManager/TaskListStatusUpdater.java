package com.example.taskwise.TaskDetail.WorkManager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;



import com.example.taskwise.DataBase.TaskDao;
import com.example.taskwise.DataBase.AppDataBase;
import com.example.taskwise.Model.Task;


public class TaskListStatusUpdater extends Worker {

    public TaskListStatusUpdater(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            TaskDao dao = AppDataBase.getAppDataBase(this.getApplicationContext()).getDataBaseDao();
            String taskInfo = getInputData().getString("taskInfo");
            Task task = dao.search(taskInfo);
            if (task != null){
                task.setTime_period(4);
                dao.update(task);
            }
            return Result.success();
        } catch (Throwable throwable){
            Log.e("failure", "doWork: " + throwable );
            return Result.failure();
        }
    }
}
