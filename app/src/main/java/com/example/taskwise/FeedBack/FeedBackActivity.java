package com.example.taskwise.FeedBack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.taskmanager.R;
import com.example.taskwise.ContextWrapper;
import com.example.taskwise.SharedPreferences.AppSettingContainer;

public class FeedBackActivity extends AppCompatActivity {

    ImageView il1;
    AppCompatButton sendEmailBtn;

    AppSettingContainer settingContainer;

    public static final String email = "atm.workspace@gmail.com";
    public static final String subject = "FeedBack, Criticism, Proposal";

    public void cast(){
        il1 = findViewById(R.id.il1);
        sendEmailBtn = findViewById(R.id.sendEmailBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingContainer = new AppSettingContainer(this);
        ContextWrapper.setTheme(this , settingContainer.getAppTheme());
        setContentView(R.layout.activity_feed_back);
        cast();
        il1.setImageResource(setIlls(settingContainer.getAppTheme()));

        sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + email));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

                Intent chooser = Intent.createChooser(emailIntent, "Send Email");
                if (chooser.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ContextWrapper.wrap(newBase));
    }

    public int setIlls(int theme){
        if (theme == 0)
            return R.drawable.il_feedback_green;

        else if (theme == 1)
            return R.drawable.il_feedback_gray;

        else
            return R.drawable.il_feedback_blue;
    }
}