package com.example.taskwise.EventDatail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
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
import com.example.taskwise.BroadCastReceivers.Remiders;
import com.example.taskwise.ContextWrapper;
import com.example.taskwise.DataBase.AppDataBase;
import com.example.taskwise.EventDatail.EventDetailContract;
import com.example.taskwise.Main.MainContract;
import com.example.taskwise.Main.MainPresentor;
import com.example.taskwise.Model.Event;
import com.example.taskwise.Model.Task;
import com.example.taskwise.SharedPreferences.AppSettingContainer;
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
import java.util.concurrent.TimeUnit;

public class EventDetailActivity extends AppCompatActivity implements EventDetailContract.view {

    TextView headerTv , dateTv , startTimeTv , endTimeTv;
    EditText eventTitleEt;
    AppCompatButton submitEventBtn;
    Spinner notifySpinner;
    RelativeLayout deleteEventBtn , backBtn;

    EventDetailPresentor presentor;
    AppSettingContainer settingContainer;

    int notify = 2 ;
    long subtractDate;

    Calendar selectedCalendar , futureCalendar;
    Date selectedDate , futureDate;

    SimpleDateFormat dateSdf = new SimpleDateFormat("MMM dd, yyyy");
    SimpleDateFormat timeSdf = new SimpleDateFormat("hh:mm a");

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

        presentor = new EventDetailPresentor(AppDataBase.getAppDataBase(this).getEventDataBaseDao() , getIntent().getParcelableExtra("event"));
        presentor.onAttach(this);

        setSpinner();

        submitEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!eventTitleEt.getText().toString().equals("")){
                    presentor.saveButtonClicked(eventTitleEt.getText().toString() , selectedDate.getTime() , futureDate.getTime() , dateTv.getText().toString() , notify);
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
    }

    @Override
    public void setTexts(int headerText, int buttonTv , boolean create) {
        headerTv.setText(headerText);
        submitEventBtn.setText(getString(buttonTv));

        if (create){
            selectedCalendar = Calendar.getInstance();
            selectedDate = selectedCalendar.getTime();

            futureCalendar = Calendar.getInstance();
            futureCalendar.add(Calendar.HOUR_OF_DAY , 2);
            futureDate = futureCalendar.getTime();

            dateTv.setText(dateSdf.format(selectedDate));
            startTimeTv.setText(timeSdf.format(selectedDate));
            endTimeTv.setText(timeSdf.format(futureDate));

            notifySpinner.setSelection(notify);
        }
    }

    @Override
    public void showEvent(Event event) {
        eventTitleEt.setText(event.getTitle());
        dateTv.setText(dateSdf.format(event.getFirstDate()));
        startTimeTv.setText(timeSdf.format(event.getFirstDate()));
        endTimeTv.setText(timeSdf.format(event.getSecondDate()));
        notifySpinner.setSelection(event.getNotifyMe());
    }

    @Override
    public void setDeleteButtonVisibility(boolean visible) {
        deleteEventBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setAlarmManager(String eventTitle, long notificationDate , int notifyMe) {
        Intent intent = new Intent(EventDetailActivity.this , Remiders.class);
        intent.putExtra("eventTitle" , eventTitle);
        PendingIntent pi;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S){
            pi = PendingIntent.getBroadcast(getApplicationContext() , 0 , intent , PendingIntent.FLAG_IMMUTABLE );
        }
        else {
            pi = PendingIntent.getBroadcast(EventDetailActivity.this , 0 , intent , PendingIntent.FLAG_UPDATE_CURRENT );
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

        manager.set(AlarmManager.RTC_WAKEUP , calendar.getTimeInMillis() , pi);
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
    protected void onDestroy() {
        super.onDestroy();
        presentor.onDetach();
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

        datePickerDialog.show(getSupportFragmentManager().beginTransaction() , null);

        datePickerDialog.setOkText(R.string.ok);
        datePickerDialog.setCancelColor(R.string.cancel);
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
                    startTimeTv.setText(timeSdf.format(selectedDate));
                } else {
                    futureCalendar.set(Calendar.HOUR_OF_DAY , hourOfDay);
                    futureCalendar.set(Calendar.MINUTE , minute);
                    futureDate = futureCalendar.getTime();
                    endTimeTv.setText(timeSdf.format(futureDate));
                }
            }
        } , hour , minute , false);
        timePickerDialog.show(getSupportFragmentManager().beginTransaction() , null);

        timePickerDialog.setOkText(R.string.ok);
        timePickerDialog.setCancelColor(R.string.cancel);
    }

}