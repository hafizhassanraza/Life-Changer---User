package com.bitsnest.lifechanger.LC;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActivityNominee extends AppCompatActivity {


    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nominee);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this);

        EditText edit_Nname=findViewById(R.id.edit_Nname);
        EditText edit_NAddress=findViewById(R.id.edit_NAddress);




        EditText edit_userPhone=findViewById(R.id.edit_NPhone);
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

        EditText edit_userID=findViewById(R.id.edit_NID);
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









        Button btn_save=findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!IsEmptyfields(edit_Nname,edit_NAddress,edit_userPhone,edit_userID))
                    UploadData(edit_Nname,edit_NAddress,edit_userPhone,edit_userID);

            }
        });
    }


    private void UploadData(EditText Nname,EditText Naddress,EditText Nphone,EditText Nid){

        final lottiedialog lottie=new lottiedialog(this);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();


        StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
        ref.putFile(Uri.parse(getIntent().getStringExtra("imageUri")))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                DocumentReference documentReference = firestore.collection("Users").document();

                                Map<String, Object> map = new HashMap<>();

                                map.put("balance", "0");
                                map.put("takaful", "0");
                                map.put("sumcovered", "0");
                                map.put("profile", uri.toString());



                                map.put("n_name",Nname.getText().toString());
                                map.put("n_address",Naddress.getText().toString());
                                map.put("n_phone",Nphone.getText().toString());
                                map.put("n_cnic",Nid.getText().toString());

                                map.put("ac_name", getIntent().getStringExtra("ac_name"));
                                map.put("ac_bank", getIntent().getStringExtra("ac_bank"));
                                map.put("ac_number", getIntent().getStringExtra("ac_number"));

                                map.put("cnic", getIntent().getStringExtra("id"));
                                map.put("fname", getIntent().getStringExtra("fname"));
                                map.put("lname", getIntent().getStringExtra("lname"));
                                map.put("phone", getIntent().getStringExtra("phone"));
                                map.put("password", getIntent().getStringExtra("pass"));
                                map.put("address", getIntent().getStringExtra("address"));
                                map.put("token", documentReference.getId());


                                documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        utils.putToken(documentReference.getId());
                                        progressDialog.dismiss();
                                        lottie.show();

                                        Map<String, Object> map = new HashMap<>();
                                        String msg="We're so excited you are a part of LIFE CHANGER. We love all our customers, and that includes you too! To show our happiness at having you here.";
                                        //map.put("account_number", "1263264");//edt_firstName.getText().toString().trim());//userName.getEditText().getText().toString().intern());
                                        map.put("data",msg);
                                        map.put("date",getdate());
                                        map.put("heading","Welcome! Mr. "+getIntent().getStringExtra("fname"));
                                        map.put("status","unread");

                                        firestore.collection("Users")
                                                .document(utils.getToken())
                                                .collection("Notifications")
                                                .add(map)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {

                                                        Map<String, Object> map = new HashMap<>();
                                                        String msg="New Investor, Mr. "
                                                                +getIntent().getStringExtra("fname")
                                                                +" "
                                                                +getIntent().getStringExtra("lname")
                                                                +" has been created account";
                                                        //map.put("account_number", "1263264");//edt_firstName.getText().toString().trim());//userName.getEditText().getText().toString().intern());
                                                        map.put("data",msg);
                                                        map.put("date",getdate());
                                                        map.put("heading","New Investor");
                                                        map.put("status","unread");

                                                        firestore.collection("Admin").document("ZTCzJoCKYReCLEPoUEYI")
                                                                .collection("NotificationsAdmin").add(map)
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {

                                                                        lottie.dismiss();
                                                                        Toast.makeText(ActivityNominee.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
                                                                        Intent myIntent = new Intent(ActivityNominee.this, ActivitySignUpCong.class);
                                                                        startActivity(myIntent);
                                                                        finish();

                                                                    }

                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        lottie.dismiss();
                                                                        Toast.makeText(ActivityNominee.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });



                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                lottie.dismiss();
                                                Toast.makeText(ActivityNominee.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                lottie.dismiss();
                                                Toast.makeText(ActivityNominee.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull  Exception e) {

                                        Toast.makeText(ActivityNominee.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });



                            }
                        });



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActivityNominee.this, "Connection Error", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        //Toast.makeText(ActivityNominee.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploading Data "+(int)progress+"%");
                    }
                });

    }


    private String getdate() {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
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