package com.bitsnest.lifechanger.LC;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bitsnest.lifechanger.R;

public class ActivityRenewPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew_password);
        Button btn_rest_pass=findViewById(R.id.btn_rest_pass);
        btn_rest_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityRenewPassword.this,ActivitySignIn.class));
                finish();
            }
        });
    }
}