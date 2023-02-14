package com.bitsnest.lifechanger.LC;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bitsnest.lifechanger.R;
import com.bitsnest.lifechanger.Utils;
import com.bitsnest.lifechanger.lottiedialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.HashMap;
import java.util.Map;

public class ActivityAccountEdit extends AppCompatActivity {

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this);

        EditText ac_name=findViewById(R.id.edit_acName_e);
        EditText ac_bank=findViewById(R.id.edit_acBank_e);
        EditText ac_number=findViewById(R.id.edit_acNumber_e);


        ac_name.setText(getIntent().getStringExtra("acID"));
        ac_bank.setText(getIntent().getStringExtra("acBank"));
        ac_number.setText(getIntent().getStringExtra("acNumber"));

        Button btn_next=findViewById(R.id.btn_next_signUp);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!IsEmptyfields(ac_name,ac_bank,ac_number))
                {
                    updateNominee(ac_name,ac_bank,ac_number);
                }

            }
        });
    }

    private void updateNominee(EditText ac_name, EditText ac_bank, EditText ac_number) {

        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();

        Map<String, Object> map = new HashMap<>();
        map.put("ac_name",ac_name.getText().toString());
        map.put("ac_bank",ac_bank.getText().toString());
        map.put("ac_number",ac_number.getText().toString());



        firestore.collection("Users").document(utils.getToken()).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull  Task<Void> task) {
                        Toast.makeText(ActivityAccountEdit.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ActivityAccountEdit.this, ActivityMain.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        lottie.dismiss();
                        Toast.makeText(ActivityAccountEdit.this, "Connection Error", Toast.LENGTH_SHORT).show();
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