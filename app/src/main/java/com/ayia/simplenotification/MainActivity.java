package com.ayia.simplenotification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static com.ayia.simplenotification.Receiver.NOTIFICATION_CHANNEL_ID;


public class MainActivity extends AppCompatActivity {

    CountDownTimer cTimer = null;
    TextInputEditText etTime;
    TextInputLayout tilTime;
    TextView tvCount;
    int uniqueId = 1;

    long userTime;

    long mMillisUntilFinished;
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        etTime = findViewById(R.id.etTime);
        tilTime = findViewById(R.id.tilTime);

        etTime.requestFocus();
        etTime.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if(etTime !=null)
                    if (!Objects.requireNonNull(etTime.getText()).toString().trim().equals("")){

                        userTime = Long.parseLong(etTime.getText().toString().trim()) * 1000;

                    }


            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        tvCount = findViewById(R.id.tvCount);


        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!etTime.getText().toString().trim().equals("") ){
                    startTimer(userTime);
                    // timeNotification();
                }
                else {

                    tilTime.setError(getString(R.string.enter_time_in_seconds));
                    etTime.requestFocus();

                }

            }
        });
    }
    private void startTimer(long time) {

        cTimer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {

                mMillisUntilFinished = millisUntilFinished;

                tvCount.setVisibility(View.VISIBLE);
                tvCount.setText("time: " + millisUntilFinished / 1000);

            }
            public void onFinish() {
                showNotification();
                tvCount.setText(R.string.done);
                uniqueId++;

            }
        };
        cTimer.start();
    }

    private void timeNotification(){

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent(MainActivity.this, Receiver.class);
        notificationIntent.putExtra(Receiver.NOTIFICATION_ID, uniqueId);
        notificationIntent.putExtra(Receiver.NOTIFICATION_CONTENT, "Countdown "+uniqueId+" done!");
        PendingIntent broadcast = PendingIntent.getBroadcast(MainActivity.this,
                100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,mMillisUntilFinished, broadcast);

    }


    private void showNotification(){

        //show notifications immediately without alarm manager and receiver
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent mainPendingIntent =
                PendingIntent.getActivity(
                        this,
                        uniqueId,
                        notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Notification.Builder builder = new Notification.Builder(this);
        Notification notification = builder.setContentTitle("simple notification")
                .setContentText("count down "+uniqueId+" done")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSmallIcon(R.drawable.ic_twotone_timelapse_24)
                .setContentIntent(mainPendingIntent).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "countdowns",
                    IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(uniqueId, notification);
    }
    private void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cTimer!= null)
            cancelTimer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}