package com.bitsnest.lifechanger.LC;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class ActivitySignIn extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private Utils utils;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        final EditText edit_userPass=findViewById(R.id.edit_userPass);
        final EditText edit_userID=findViewById(R.id.edit_userID);
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this);

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



        Button btn_signin=findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!IsEmptyfield(edit_userID,edit_userPass))auth(edit_userID,edit_userPass);

            }
        });
        Button btn_signup=findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitySignIn.this,ActivitySignUp.class));

            }
        });
        TextView txt_forget_pass=findViewById(R.id.txt_forget_pass);
        txt_forget_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivitySignIn.this,ActivityForgetPassword.class));

            }
        });
    }




    public void auth(EditText userID,EditText userPass){

        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();


        firestore.collection("Users").whereEqualTo("cnic",userID.getText().toString()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().isEmpty()) {
                                lottie.dismiss();
                                userID.setError("Incorrect C.N.I.C");
                            }
                            else {
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    if(document.getString("password").equals(userPass.getText().toString())){
                                        lottie.dismiss();
                                        utils.putToken(document.getString("token"));
                                        startActivity(new Intent(ActivitySignIn.this,ActivityMain.class));
                                        finish();
                                    }
                                    else {
                                        lottie.dismiss();
                                        userPass.setError("Incorrect Password");
                                    }
                                }
                            }

                        }
                        else{
                            lottie.dismiss();
                            userID.setError("Connection Problem");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                lottie.dismiss();
                Toast.makeText(ActivitySignIn.this, "Connection Problem!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean IsEmptyfield(EditText userID,EditText userPass){
        boolean empty=true;
        if(userID.getText().toString().isEmpty())userID.setError("Enter User C.N.I.C");
        else if(userPass.getText().toString().isEmpty())userPass.setError("Enter User Password");
        else empty=false;
        return empty;
    }



}