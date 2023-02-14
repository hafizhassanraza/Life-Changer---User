package com.bitsnest.lifechanger.LC;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bitsnest.lifechanger.R;
import com.bitsnest.lifechanger.Utils;
import com.bitsnest.lifechanger.lottiedialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActivityInvest extends AppCompatActivity {


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
        setContentView(R.layout.activity_invest);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this);

        img_userProfile=findViewById(R.id.img_userProfile_invest);
        EditText edit_account=findViewById(R.id.edit_ac_deposit);
        EditText edit_amount=findViewById(R.id.edit_ac_amount);


        img_userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filePath!=null)
                    photodialog(filePath.toString());

            }
        });

        btn_addPhoto=findViewById(R.id.btn_addPhoto_invest);
        btn_addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

            }
        });
        Button btn_next=findViewById(R.id.btn_deposit_invest);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filePath == null)
                    Toast.makeText(ActivityInvest.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                else if(!IsEmptyfields(edit_account,edit_amount))
                {
                    createUser(utils.getToken(),edit_account,edit_amount);
                }

            }
        });



    }

    private void createUser(String userID,EditText edit_account, EditText edit_amount) {



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

                                Map<String,Object> map= new HashMap<>();

                                map.put("receipt",uri.toString());
                                map.put("date",getDateTime());
                                map.put("amount",edit_amount.getText().toString());
                                map.put("deposit_account",edit_account.getText().toString());
                                map.put("status","pending");
                                map.put("userID",userID);






                                firestore.collection("Users").document(userID)
                                        .collection("Transactions")
                                        .document(userID)
                                        .collection("Invest")
                                        .add(map)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull  Task<DocumentReference> task) {
                                                lottie.dismiss();
                                                recipdialog(
                                                        edit_amount.getText().toString(),
                                                        edit_account.getText().toString(),
                                                        getIntent().getStringExtra("name"),
                                                        getDate()
                                                );
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull  Exception e) {
                                                lottie.dismiss();
                                                Toast.makeText(ActivityInvest.this, "Connection Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        });



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActivityInvest.this, "Connection Error", Toast.LENGTH_SHORT).show();
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


    public void recipdialog(String depositBalance,String depositAccount,String depositBy,String date)
    {

        Dialog dialog = new Dialog(ActivityInvest.this);
        dialog.setContentView(R.layout.dialog_voucherhur);
        Button btn_skip = dialog.findViewById(R.id.btn_skip);
        TextView txt_depositBalance = dialog.findViewById(R.id.txt_depositBalance);
        txt_depositBalance.setText(depositBalance+" Rs");
        TextView txt_depositAccount = dialog.findViewById(R.id.txt_depositAccount);
        txt_depositAccount.setText(depositAccount);
        TextView txt_depositBy = dialog.findViewById(R.id.txt_depositBy);
        txt_depositBy.setText(depositBy);
        TextView txt_date = dialog.findViewById(R.id.txt_date);
        txt_date.setText(date);


        dialog.show();

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                startActivity(new Intent(ActivityInvest.this, ActivityMain.class));
                finish();

            }
        });

    }








    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDate() {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public boolean IsEmptyfields(EditText edit_account,EditText edit_amount){
        boolean empty=true;
        if(edit_account.getText().toString().isEmpty())edit_account.setError("Enter deposit account number");
        else if(edit_amount.getText().toString().isEmpty())edit_amount.setError("Enter deposit amount");
        else empty=false;
        return empty;
    }



    public void photodialog(String photo)
    {

        Dialog dialog = new Dialog(ActivityInvest.this);
        dialog.setContentView(R.layout.dialog_photo);
        ImageView temp_photo = dialog.findViewById(R.id.img_photo);
        Glide.with(this).load(photo).into(temp_photo);

        dialog.show();

    }


}