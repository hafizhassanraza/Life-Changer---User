package com.bitsnest.lifechanger.LC;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bitsnest.lifechanger.R;

public class ActivityForgetPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Button btn_rest_pass=findViewById(R.id.btn_rest_pass);
        btn_rest_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityForgetPassword.this,ActivityRenewPassword.class));
                finish();
            }
        });
    }
}