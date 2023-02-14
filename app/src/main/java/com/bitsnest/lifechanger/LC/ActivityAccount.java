package com.bitsnest.lifechanger.LC;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.bitsnest.lifechanger.R;

public class ActivityAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        EditText ac_name=findViewById(R.id.edit_acName);
        EditText ac_bank=findViewById(R.id.edit_acBank);
        EditText ac_number=findViewById(R.id.edit_acNumber);


        Button btn_next=findViewById(R.id.btn_next_signUp);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!IsEmptyfields(ac_name,ac_bank,ac_number))
                {
                    Intent intent = new Intent(getBaseContext(), com.bitsnest.lifechanger.LC.ActivityNominee.class);

                    intent.putExtra("ac_name", ac_name.getText().toString());
                    intent.putExtra("ac_bank", ac_bank.getText().toString());
                    intent.putExtra("ac_number", ac_number.getText().toString());


                    intent.putExtra("imageUri", getIntent().getStringExtra("imageUri"));
                    intent.putExtra("fname", getIntent().getStringExtra("fname"));
                    intent.putExtra("lname", getIntent().getStringExtra("lname"));
                    intent.putExtra("address", getIntent().getStringExtra("address"));
                    intent.putExtra("phone", getIntent().getStringExtra("phone"));
                    intent.putExtra("id", getIntent().getStringExtra("id"));
                    intent.putExtra("pass", getIntent().getStringExtra("pass"));
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    public boolean IsEmptyfields(EditText ac_name,EditText ac_bank,EditText ac_number){
        boolean empty=true;
        if(ac_name.getText().toString().isEmpty())ac_name.setError("Enter Account Tittle");
        else if(ac_bank.getText().toString().isEmpty())ac_bank.setError("Enter Bank Name");
        else if(ac_number.getText().toString().isEmpty())ac_number.setError("Enter User Account Number");
        else empty=false;
        return empty;
    }
}