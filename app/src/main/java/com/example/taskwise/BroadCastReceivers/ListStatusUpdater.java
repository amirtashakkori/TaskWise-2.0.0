package com.example.taskwise.BroadCastReceivers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


import com.example.taskwise.DataBase.AppDataBase;
import com.example.taskwise.DataBase.DBDao;
import com.example.taskwise.Model.Event;
import com.example.taskwise.Model.Task;


public class ListStatusUpdater extends Worker {

    public ListStatusUpdater(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            DBDao dao = AppDataBase.getAppDataBase(this.getApplicationContext()).getDataBaseDao();

            long taskId = getInputData().getLong("taskId" , -1);
            Task task = dao.searchTask(taskId);
            if (task != null){
                task.setTime_period(4);
                dao.update(task);
            }

            long eventId = getInputData().getLong("eventId" , -1);
            Event event = dao.searchEvent(eventId);
            if (event != null){
                event.setOutdated(true);
                dao.update(event);
            }

            return Result.success();
        } catch (Throwable throwable){
            Log.e("failure", "doWork: " + throwable );
            return Result.failure();
        }
    }
}
