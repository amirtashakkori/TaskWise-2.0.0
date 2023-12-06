package com.example.taskwise.EventDatail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.taskmanager.R;
import com.example.taskwise.ContextWrapper;
import com.example.taskwise.DataBase.AppDataBase;
import com.example.taskwise.EventDatail.EventDetailContract;
import com.example.taskwise.Main.MainContract;
import com.example.taskwise.Main.MainPresentor;
import com.example.taskwise.Model.Event;
import com.example.taskwise.Model.Task;
import com.example.taskwise.SharedPreferences.AppSettingContainer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventDetailActivity extends AppCompatActivity implements EventDetailContract.view {

    TextView headerTv , dateTv , startTime , endTime;
    EditText eventTitleEt;
    AppCompatButton submitEventBtn;
    Spinner notifySpinner;
    RelativeLayout deleteEventBtn , backBtn;
    DatePicker datePicker;
    AppCompatButton okBtn , cancelBtn;

    EventDetailPresentor presentor;
    AppSettingContainer settingContainer;
    int selected_day , selected_month , selected_year;
    String dateName;

    int notify = 2;

    public void cast(){
        headerTv = findViewById(R.id.headerTv);
        dateTv = findViewById(R.id.dateTv);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
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

                showDatePickerDialog();


            }
        });
    }

    @Override
    public void setTexts(int headerText, int buttonTv) {
        headerTv.setText(headerText);
        submitEventBtn.setText(getString(buttonTv));
    }

    @Override
    public void showEvent(Event event) {
        eventTitleEt.setText(event.getTitle());
        dateTv.setText((int) event.getDate());
        startTime.setText(event.getStartTime());
        endTime.setText(event.getEndTime());
        notifySpinner.setSelection(notify);
        notifySpinner.setSelection(notify);
    }

    @Override
    public void setDeleteButtonVisibility(boolean visible) {
        deleteEventBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setNotificationManager(String eventTitle, int notifyMe) {

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

    public void showDatePickerDialog(){
        Calendar calendar = Calendar.getInstance();
        int initialYear = calendar.get(Calendar.YEAR);
        int initialMonth = calendar.get(Calendar.MONTH);
        int initialDay = calendar.get(Calendar.DAY_OF_MONTH);

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
        int color = typedValue.data;

        DatePickerDialog dialog = new DatePickerDialog(EventDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Toast.makeText(EventDetailActivity.this, "" + year + month + dayOfMonth, Toast.LENGTH_SHORT).show();
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(year , month , dayOfMonth);
                dateName = sdf.format(calendar1.getTime());
                dateTv.setText(dateName);
            }
        } , initialYear , initialMonth , initialDay);
        dialog.show();
    }
}