package com.bitsnest.lifechanger.LC;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.bitsnest.lifechanger.R;
import com.bitsnest.lifechanger.Utils;

public class ActivitySpalsh extends AppCompatActivity {
    private Utils utils;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        progressBar = (ProgressBar)findViewById(R.id.progressBa_splash);
        utils=new Utils(this);
//        getSupportActionBar().hide();


        new CountDownTimer(2000, 1000) {
            public void onFinish() {
                if(utils.isLoggedIn()){
                    Intent intent = new Intent(ActivitySpalsh.this, ActivityMain.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(ActivitySpalsh.this, ActivitySignIn.class);
                    startActivity(intent);
                    finish();
                }
            }

            public void onTick(long millisUntilFinished) {
//                progressBar.setVisibility(View.VISIBLE);
            }
        }.start();
    }
}