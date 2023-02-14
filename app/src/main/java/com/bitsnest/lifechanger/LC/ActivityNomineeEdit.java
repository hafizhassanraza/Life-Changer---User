package com.bitsnest.lifechanger.LC;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class ActivityNomineeEdit extends AppCompatActivity {


    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nominee_edit);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this);


        EditText edit_Nname=findViewById(R.id.edit_Nname_e);
        EditText edit_NAddress=findViewById(R.id.edit_NAddress_e);
        EditText edit_userPhone=findViewById(R.id.edit_NPhone_e);
        EditText edit_userID=findViewById(R.id.edit_NID_e);



        edit_Nname.setText(getIntent().getStringExtra("name_nominee"));
        edit_NAddress.setText(getIntent().getStringExtra("address_nominee"));
        edit_userPhone.setText(getIntent().getStringExtra("number_nominee"));
        edit_userID.setText(getIntent().getStringExtra("cnic_nominee"));


        edit_userPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable text) {
                if (text.length() == 4 ) {
                    text.append('-');
                }
            }
        });

        edit_userID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable text) {
                if (text.length() == 5 || text.length() == 13) {
                    text.append('-');
                }
            }
        });


        Button btn_save=findViewById(R.id.btn_save_e);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!IsEmptyfields(edit_Nname,edit_NAddress,edit_userPhone,edit_userID))
                    updateNominee(edit_Nname,edit_NAddress,edit_userPhone,edit_userID);

            }
        });
    }


    private void updateNominee(EditText Nname,EditText Naddress,EditText Nphone,EditText Nid) {


        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();

        Map<String, Object> map = new HashMap<>();
        map.put("n_name",Nname.getText().toString());
        map.put("n_address",Naddress.getText().toString());
        map.put("n_phone",Nphone.getText().toString());
        map.put("n_cnic",Nid.getText().toString());


        firestore.collection("Users").document(utils.getToken()).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull  Task<Void> task) {
                        Toast.makeText(ActivityNomineeEdit.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ActivityNomineeEdit.this, ActivityMain.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        lottie.dismiss();
                        Toast.makeText(ActivityNomineeEdit.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    public boolean IsEmptyfields(EditText edit_name, EditText edit_userAddress, EditText edit_userPhone, EditText edit_userID){
        boolean empty=true;
        if(edit_userID.getText().toString().isEmpty())edit_userID.setError("Enter User C.N.I.C");
        else if(edit_name.getText().toString().isEmpty())edit_name.setError("Enter User Name");
        else if(edit_userAddress.getText().toString().isEmpty())edit_userAddress.setError("Enter User Address");
        else if(edit_userPhone.getText().toString().isEmpty())edit_userPhone.setError("Enter User Phone");
        else empty=false;
        return empty;
    }
}