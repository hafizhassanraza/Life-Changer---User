package com.bitsnest.lifechanger.LC;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



import java.io.IOException;

public class ActivitySignUp extends AppCompatActivity {

    ///Image
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    private ImageView img_userProfile;
    private boolean fetchImage=false;
    private Button btn_addPhoto;


    Boolean IsUniqueID=false;


    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this);

        img_userProfile=findViewById(R.id.img_userProfile);
        EditText edit_fname=findViewById(R.id.edit_fname);
        EditText edit_lname=findViewById(R.id.edit_lname);
        EditText edit_userAddress=findViewById(R.id.edit_userAddress);
        EditText edit_userPass=findViewById(R.id.edit_userPass);
        EditText edit_userCPass=findViewById(R.id.edit_userCPass);


        EditText edit_userPhone=findViewById(R.id.edit_userPhone);
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

        EditText edit_userID=findViewById(R.id.edit_userID);
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
        edit_userID.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus && edit_userID.getText().toString().length()==15)
                    IsUniqueID(edit_userID);
            }
        });

        btn_addPhoto=findViewById(R.id.btn_addPhoto);
        btn_addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

            }
        });
        Button btn_next=findViewById(R.id.btn_next_signUp);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filePath == null)
                    Toast.makeText(ActivitySignUp.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                else if(!IsEmptyfields(edit_fname,edit_lname,edit_userAddress,edit_userPhone,edit_userID,edit_userPass,edit_userCPass))
                {
//                    IsUniqueID(edit_userID);
                    if(IsUniqueID)
                        createUser(edit_fname,edit_lname,edit_userAddress,edit_userPhone,edit_userID,edit_userPass);
                    else Toast.makeText(ActivitySignUp.this, "Please Select unique CNIC", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public void createUser(EditText edit_fname,EditText edit_lname,EditText edit_userAddress,
                           EditText edit_userPhone,EditText edit_userID,EditText edit_userPass){//EditText userID,EditText userPass){

        Intent intent = new Intent(getBaseContext(), ActivityAccount.class);

        intent.putExtra("imageUri", filePath.toString());
        intent.putExtra("fname", edit_fname.getText().toString());
        intent.putExtra("lname", edit_lname.getText().toString());
        intent.putExtra("address", edit_userAddress.getText().toString());
        intent.putExtra("phone", edit_userPhone.getText().toString());
        intent.putExtra("id", edit_userID.getText().toString());
        intent.putExtra("pass", edit_userPass.getText().toString());

        startActivity(intent);

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


    public void IsUniqueID(EditText userID){
        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();
        firestore.collection("Users").whereEqualTo("cnic",userID.getText().toString()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().isEmpty()) {
                                lottie.dismiss();
                                IsUniqueID=true;
                            }
                            else {
                                lottie.dismiss();
                                userID.setError("CNIC already exist!");
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
                Toast.makeText(ActivitySignUp.this, "Connection Problem!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public boolean IsEmptyfields(EditText edit_fname,EditText edit_lname,EditText edit_userAddress,
                                EditText edit_userPhone,EditText edit_userID,EditText edit_userPass,EditText edit_userCPass){
        boolean empty=true;
        if(edit_userID.getText().toString().isEmpty())edit_userID.setError("Enter User C.N.I.C");
//        else if(!IsUniqueID)edit_userID.setError("CNIC already exist");
        else if(edit_fname.getText().toString().isEmpty())edit_fname.setError("Enter User First Name");
        else if(edit_lname.getText().toString().isEmpty())edit_lname.setError("Enter User Last Name");
        else if(edit_userAddress.getText().toString().isEmpty())edit_userAddress.setError("Enter User Address");
        else if(edit_userPhone.getText().toString().isEmpty())edit_userPhone.setError("Enter User Phone");
        else if(edit_userPass.getText().toString().isEmpty())edit_userPass.setError("Enter User Password");
        else if(!edit_userCPass.getText().toString().equals(edit_userPass.getText().toString()))edit_userCPass.setError("Password not match!");
        else empty=false;
        return empty;
    }

}