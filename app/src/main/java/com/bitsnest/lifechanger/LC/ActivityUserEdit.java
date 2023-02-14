package com.bitsnest.lifechanger.LC;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bitsnest.lifechanger.R;
import com.bitsnest.lifechanger.Utils;
import com.bitsnest.lifechanger.lottiedialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActivityUserEdit extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    private ImageView img_userProfile;
    private boolean fetchImage=false;
    private Button btn_addPhoto;
    private String refUri;


    Boolean IsUniqueID=false;


    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private Utils utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this);

        img_userProfile=findViewById(R.id.img_userProfile_e);
        EditText edit_fname=findViewById(R.id.edit_fname_e);
        EditText edit_lname=findViewById(R.id.edit_lname_e);
        EditText edit_userAddress=findViewById(R.id.edit_userAddress_e);
        EditText edit_userPhone=findViewById(R.id.edit_userPhone_e);

        img_userProfile.setVisibility(View.GONE);



        //Glide.with(this).load(getIntent().getStringExtra("profile")).into(img_userProfile);
        edit_fname.setText(getIntent().getStringExtra("fname"));
        edit_lname.setText(getIntent().getStringExtra("lname"));
        edit_userAddress.setText(getIntent().getStringExtra("address"));
        edit_userPhone.setText(getIntent().getStringExtra("number"));



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

        btn_addPhoto=findViewById(R.id.btn_addPhoto_e);
        btn_addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseImage();

            }
        });
        Button btn_next=findViewById(R.id.btn_next_update);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!IsEmptyfields(edit_fname,edit_lname,edit_userAddress,edit_userPhone))
                {
                    if(fetchImage)
                        updateUser(filePath,edit_fname,edit_lname,edit_userAddress,edit_userPhone);
                    else
                        updateUserWithoutImage(edit_fname,edit_lname,edit_userAddress,edit_userPhone);
                }

            }
        });





    }

    private void updateUserWithoutImage(EditText edit_fname, EditText edit_lname, EditText edit_userAddress, EditText edit_userPhone) {


        final lottiedialog lottie=new lottiedialog(this);




        Map<String,Object> m= new HashMap<>();
        m.put("fname",edit_fname.getText().toString());
        m.put("lname",edit_lname.getText().toString());
        m.put("address",edit_userAddress.getText().toString());
        m.put("phone",edit_userPhone.getText().toString());




        firestore.collection("Users").document(utils.getToken()).update(m)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull  Task<Void> task) {
                        Toast.makeText(ActivityUserEdit.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ActivityUserEdit.this, ActivityMain.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        lottie.dismiss();
                        Toast.makeText(ActivityUserEdit.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });




    }

    private void updateUser(Uri filePath, EditText edit_fname, EditText edit_lname, EditText edit_userAddress, EditText edit_userPhone) {


        final lottiedialog lottie=new lottiedialog(this);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();



        StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
        ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                Map<String,Object> m= new HashMap<>();
                                m.put("profile",uri.toString());
                                m.put("fname",edit_fname.getText().toString());
                                m.put("lname",edit_lname.getText().toString());
                                m.put("address",edit_userAddress.getText().toString());
                                m.put("phone",edit_userPhone.getText().toString());




                                firestore.collection("Users").document(utils.getToken()).update(m)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull  Task<Void> task) {
                                                Toast.makeText(ActivityUserEdit.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(ActivityUserEdit.this, ActivityMain.class));
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull  Exception e) {
                                                lottie.dismiss();
                                                Toast.makeText(ActivityUserEdit.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });




                            }
                        });



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActivityUserEdit.this, "Connection Error", Toast.LENGTH_SHORT).show();
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





    /////////////////////////  IMAGE  //////////////////
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img_userProfile.setImageBitmap(bitmap);
                btn_addPhoto.setVisibility(View.GONE);
                img_userProfile.setVisibility(View.VISIBLE);
                fetchImage=true;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    ///////////////////////////////////////////////////////////////

    public boolean IsEmptyfields(EditText edit_fname, EditText edit_lname, EditText edit_userAddress, EditText edit_userPhone){
        boolean empty=true;
        if(edit_fname.getText().toString().isEmpty())edit_fname.setError("Enter User First Name");
        else if(edit_lname.getText().toString().isEmpty())edit_lname.setError("Enter User Last Name");
        else if(edit_userAddress.getText().toString().isEmpty())edit_userAddress.setError("Enter User Address");
        else if(edit_userPhone.getText().toString().isEmpty())edit_userPhone.setError("Enter User Phone");
        else empty=false;
        return empty;
    }

}