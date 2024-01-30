package com.example.taskwise.EventDatail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.taskmanager.R;
import com.example.taskwise.BroadCastReceivers.ListStatusUpdater;
import com.example.taskwise.BroadCastReceivers.Remiders;
import com.example.taskwise.ContextWrapper;
import com.example.taskwise.DataBase.AppDataBase;
import com.example.taskwise.DataBase.DBDao;
import com.example.taskwise.EventDatail.EventDetailContract;
import com.example.taskwise.Main.MainContract;
import com.example.taskwise.Main.MainPresentor;
import com.example.taskwise.Model.Event;
import com.example.taskwise.Model.Task;
import com.example.taskwise.SharedPreferences.AppSettingContainer;
import com.example.taskwise.TaskDetail.TaskDetailActivity;
import com.google.android.material.internal.ViewOverlayImpl;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class EventDetailActivity extends AppCompatActivity implements EventDetailContract.view {

    TextView headerTv , dateTv , startTimeTv , endTimeTv;
    EditText eventTitleEt;
    AppCompatButton submitEventBtn;
    Spinner notifySpinner;
    RelativeLayout deleteEventBtn , backBtn;

    EventDetailPresentor presentor;
    AppSettingContainer settingContainer;
    Calendar selectedCalendar , futureCalendar;
    Date selectedDate , futureDate;
    DBDao dao;

    int notify = 2 ;
    long subtractDate;

    SimpleDateFormat dateSdf , timeSdf;

    public void cast(){
        headerTv = findViewById(R.id.headerTv);
        dateTv = findViewById(R.id.dateTv);
        startTimeTv = findViewById(R.id.startTimeTv);
        endTimeTv = findViewById(R.id.endTimeTv);
        eventTitleEt = findViewById(R.id.eventTitleEt);
        submitEventBtn = findViewById(R.id.submitEventBtn);
        deleteEventBtn = findViewById(R.id.deleteEventBtn);
        notifySpinner = findViewById(R.id.notifySpinner);
        backBtn = findViewById(R.id.backBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingContainer = new AppSettingContainer(this);
        ContextWrapper.setTheme(this , settingContainer.getAppTheme());
        setContentView(R.layout.activity_event_detail);
        cast();
        dateSdf = new SimpleDateFormat("MMM dd, yyyy" , new Locale(settingContainer.getAppLanguage()));
        timeSdf = new SimpleDateFormat("hh:mm a" , new Locale(settingContainer.getAppLanguage()));
        dao = AppDataBase.getAppDataBase(this).getDataBaseDao();
        presentor = new EventDetailPresentor(dao , getIntent().getParcelableExtra("event") , settingContainer);
        presentor.onAttach(this);

        setSpinner();

        dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });

        startTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(0);
            }
        });

        endTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime(1);
            }
        });

        deleteEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentor.deleteButtonClicked();
            }
        });

        submitEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!eventTitleEt.getText().toString().equals("")){
                    String eventTitle = eventTitleEt.getText().toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy" , Locale.ENGLISH);
                    String date = sdf.format(selectedDate.getTime());
                    presentor.saveButtonClicked(eventTitle , selectedDate.getTime() , futureDate.getTime() , date , notify );
                    finish();
                } else {
                    eventTitleEt.setError(R.string.eventTitleError + "");
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presentor.onDetach();
    }

    //Setup the interface
    @Override
    public void setTexts(int headerText, int buttonTv , boolean create) {
        headerTv.setText(headerText);
        submitEventBtn.setText(getString(buttonTv));

        if (create){
            Locale selectedLocale = new Locale("en");
            selectedCalendar = Calendar.getInstance(selectedLocale);

            selectedCalendar.add(Calendar.MINUTE , (30 - selectedCalendar.get(Calendar.MINUTE) % 30));
            selectedDate = selectedCalendar.getTime();

            Locale futureLocale = new Locale("en");
            futureCalendar = Calendar.getInstance(futureLocale);
            futureCalendar.add(Calendar.MINUTE , (30 - futureCalendar.get(Calendar.MINUTE) % 30 + 90));
            futureDate = futureCalendar.getTime();

            dateTv.setText(dateSdf.format(selectedDate));
            startTimeTv.setText(timeSdf.format(selectedDate));
            endTimeTv.setText(timeSdf.format(futureDate));

        }
    }

    public void setDate(){
        int year = selectedCalendar.get(Calendar.YEAR);
        int month = selectedCalendar.get(Calendar.MONTH );
        int day = selectedCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                selectedCalendar.set(year , monthOfYear , dayOfMonth);

                selectedDate = selectedCalendar.getTime();
                dateTv.setText(dateSdf.format(selectedDate));
            }
        } , year , month , day);

        if (new AppSettingContainer(this).getAppMode().equals("lightMode"))
            datePickerDialog.setThemeDark(false);
        else
            datePickerDialog.setThemeDark(true);


        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
        int colorPrimary = typedValue.data;

        datePickerDialog.show(getSupportFragmentManager().beginTransaction() , null);
        datePickerDialog.setAccentColor(colorPrimary);
        datePickerDialog.setOkColor(colorPrimary);
        datePickerDialog.setCancelColor(colorPrimary);
        datePickerDialog.setOkText(R.string.ok);
        datePickerDialog.setCancelText(R.string.cancel);
    }

    public void setTime(int time){
        int hour , minute;
        if (time == 0){
            hour = selectedCalendar.get(Calendar.HOUR_OF_DAY);
            minute = selectedCalendar.get(Calendar.MINUTE);
        } else {
            hour = futureCalendar.get(Calendar.HOUR_OF_DAY);
            minute = futureCalendar.get(Calendar.MINUTE);
        }
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                if (time == 0){
                    selectedCalendar.set(Calendar.HOUR_OF_DAY , hourOfDay);
                    selectedCalendar.set(Calendar.MINUTE , minute);
                    selectedDate = selectedCalendar.getTime();
                    futureCalendar.setTime(selectedDate);
                    futureCalendar.add(Calendar.HOUR_OF_DAY , 2);
                    futureDate = futureCalendar.getTime();;
                    startTimeTv.setText(timeSdf.format(selectedDate));
                    endTimeTv.setText(timeSdf.format(futureDate));
                } else {
                    futureCalendar.set(Calendar.HOUR_OF_DAY , hourOfDay);
                    futureCalendar.set(Calendar.MINUTE , minute);
                    futureDate = futureCalendar.getTime();
                    endTimeTv.setText(timeSdf.format(futureDate));
                }
            }
        } , hour , minute , false);

        if (new AppSettingContainer(this).getAppMode().equals("lightMode"))
            timePickerDialog.setThemeDark(false);
        else
            timePickerDialog.setThemeDark(true);


        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true);
        int colorPrimary = typedValue.data;

        timePickerDialog.show(getSupportFragmentManager().beginTransaction() , null);
        timePickerDialog.setAccentColor(colorPrimary);
        timePickerDialog.setOkColor(colorPrimary);
        timePickerDialog.setCancelColor(colorPrimary);
        timePickerDialog.setOkText(R.string.ok);
        timePickerDialog.setCancelText(R.string.cancel);
    }

    public void setSpinner(){
        String[] notify_spinner_items = {getString(R.string.atTheTime) , getString(R.string.quarterHour) , getString(R.string.halfHour) , getString(R.string.hourBefore)};
        ArrayAdapter<String> periodSpinnerAdapter = new ArrayAdapter<String>(this , R.layout.item_period_spinner , R.id.spinnerTv , notify_spinner_items);
        periodSpinnerAdapter.setDropDownViewResource(R.layout.item_spinner_dropdown);
        notifySpinner.setAdapter(periodSpinnerAdapter);
        notifySpinner.setSelection(notify);

        notifySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                notify = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void setDeleteButtonVisibility(boolean visible) {
        deleteEventBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    //Getting Event
    @Override
    public void showEvent(Event event) {
        eventTitleEt.setText(event.getTitle());
        selectedDate = new Date(event.getFirstDate());
        futureDate = new Date(event.getSecondDate());
        notify = event.getNotifyMe();
        notifySpinner.setSelection(event.getNotifyMe());

        selectedCalendar = Calendar.getInstance();
        futureCalendar = Calendar.getInstance();

        selectedCalendar.setTime(selectedDate);
        futureCalendar.setTime(futureDate);

        dateTv.setText(dateSdf.format(selectedDate));
        startTimeTv.setText(timeSdf.format(selectedDate));
        endTimeTv.setText(timeSdf.format(futureDate));
    }

    @Override
    public void updateEvent() {
        Toast.makeText(this, "Successfuly Edited.!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void deleteEvent() {
        Toast.makeText(this, "Seccessfuly Deleted.!", Toast.LENGTH_SHORT).show();
        finish();
    }


    //WorkManager & AlarmManager
    @Override
    public void setWorkManager(long id , long expiredDate) {
        Data data = new Data.Builder().putLong("eventId" , id) .build();

        long date = expiredDate - System.currentTimeMillis();

        WorkManager manager = WorkManager.getInstance(EventDetailActivity.this);
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(ListStatusUpdater.class)
                .setInputData(data)
                .setInitialDelay(date , TimeUnit.MILLISECONDS)
                .build();
        manager.enqueue(request);
        UUID workManagerId = request.getId();
        Event event = dao.searchEvent(id);
        event.setWorkmanagerId(workManagerId);
        dao.update(event);
        finish();
    }

    @Override
    public void cancelWorkManger(UUID workId) {
        WorkManager.getInstance(EventDetailActivity.this).cancelWorkById(workId);
    }

    @Override
    public void setAlarmManager(long eventId , String eventTitle, long notificationDate , int notifyMe , long requestCode) {
        Intent intent = new Intent(EventDetailActivity.this , Remiders.class);
        intent.putExtra("eventTitle" , eventTitle);
        intent.putExtra("eventId" , eventId);
        PendingIntent pi;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S){
            pi = PendingIntent.getBroadcast(getApplicationContext() , (int) requestCode , intent , PendingIntent.FLAG_IMMUTABLE );
        }
        else {
            pi = PendingIntent.getBroadcast(EventDetailActivity.this , (int) requestCode , intent , PendingIntent.FLAG_UPDATE_CURRENT );
        }

        switch (notifyMe){
            case 0 :
                subtractDate = 0;
                break;
            case 1 :
                subtractDate = TimeUnit.MINUTES.toMillis(15);
                break;
            case 2 :
                subtractDate = TimeUnit.MINUTES.toMillis(30);
                break;
            case 3 :
                subtractDate = TimeUnit.HOURS.toMillis(1);
                break;
        }

        long date = notificationDate - subtractDate;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (manager != null) {
            manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);
        }
    }

    @Override
    public void cancelAlarmManager(long requestCode) {
        Intent intent = new Intent(this, Remiders.class);
        PendingIntent pendingIntent;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S){
            pendingIntent = PendingIntent.getBroadcast(this  , (int) requestCode , intent , PendingIntent.FLAG_IMMUTABLE );
        }
        else {
            pendingIntent = PendingIntent.getBroadcast(this , (int) requestCode , intent , PendingIntent.FLAG_UPDATE_CURRENT );
        }

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (manager != null){
            manager.cancel(pendingIntent);
            pendingIntent.cancel();
        }

    }

}