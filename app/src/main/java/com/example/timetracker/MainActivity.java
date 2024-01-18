package com.example.timetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private int sec = 0;
    private boolean is_running;
    private boolean was_running;
    private int completedWorkDays;
    //private EditText hoursPerDay;

    private static final String PREFS_FILE = "WorkTimerPrefs";
    private static final String COMPLETED_WORK_DAYS_KEY = "completedWorkDays";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button weekOverviewBtn = findViewById(R.id.weekoverview);
        ToggleButton workButton = (ToggleButton) findViewById(R.id.workButton);

        workButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    onClickStart();
                } else {
                    onClickStop();
                }
            }
        });
        weekOverviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeekOverviewActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences prefs = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        completedWorkDays = prefs.getInt(COMPLETED_WORK_DAYS_KEY, 0);

        if (savedInstanceState != null) {
            sec = savedInstanceState.getInt("seconds");
            is_running = savedInstanceState.getBoolean("running");
            was_running = savedInstanceState.getBoolean("wasRunning");
        }
        runningTimer();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("seconds", sec);
        savedInstanceState.putBoolean("running", is_running);
        savedInstanceState.putBoolean("wasRunning", was_running);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (was_running) {
            is_running = true;
        }
    }

    public void onClickStart() {
        is_running = true;
    }

    public void onClickStop() {
        is_running = false;
    }

    private void runningTimer() {
        TextView output = (TextView) findViewById(R.id.output);
        //hoursPerDay = findViewById(R.id.hoursPerDay);
        //int hoursPerDayLimit = Integer.parseInt(hoursPerDay.getText().toString());
        final Handler handle = new Handler();
        handle.post(new Runnable() {
            @Override
            public void run() {
                int hrs = sec / 3600;
                int mins = (sec % 3600) / 60;
                int secs = sec % 60;
                String formattedTime = String.format(Locale.getDefault(), "    %d:%02d:%02d   ", hrs, mins, secs);
                output.setText(formattedTime);

                if (is_running) {
                    sec++;

                    if (sec % (8 * 3600) == 0) {
                        completedWorkDays++;
                        saveCompletedWorkDays();
                    }
                }
                handle.postDelayed(this, 1000);
            }
        });
    }

    private void saveCompletedWorkDays() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_FILE, MODE_PRIVATE).edit();
        editor.putInt(COMPLETED_WORK_DAYS_KEY, completedWorkDays);
        editor.apply();
    }

}
