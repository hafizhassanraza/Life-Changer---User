package com.bitsnest.lifechanger.LC;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class ActivityWithdraw extends AppCompatActivity {


    Boolean IsUniqueID=false;

    private TextView txt_acNumber;

    private CheckBox chk_acNumber;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore firestore;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this);

        EditText ac_amount=findViewById(R.id.edit_ac_amount_w);
        txt_acNumber=findViewById(R.id.txt_acNumber_w);
        chk_acNumber=findViewById(R.id.chk_acNumber);

        txt_acNumber.setText(getIntent().getStringExtra("ac_number"));

        chk_acNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chk_acNumber.isChecked())
                    acNumberdialog();

            }
        });




        Button btn_next=findViewById(R.id.btn_withdraw_w);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(ac_amount.getText().toString().isEmpty())
                    ac_amount.setError("Please enter withdraw amount");
                else if(Integer.parseInt(ac_amount.getText().toString())>Integer.parseInt(getIntent().getStringExtra("balance"))
                    || Integer.parseInt(ac_amount.getText().toString())<1)
                    ac_amount.setError("Insufficient amount");
                else
                {
                    MaterialDialog mDialog = new MaterialDialog.Builder(ActivityWithdraw.this)
                            .setTitle("Withdraw")
                            .setMessage("Are you sure want to Withdraw!")
                            .setCancelable(false)
                            .setPositiveButton("Withdraw", R.drawable.ic_baseline_exit_to_app_24, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                    deposit(utils.getToken(),ac_amount,txt_acNumber.getText().toString());

                                }
                            })
                            .setNegativeButton("Cancel", R.drawable.ic_baseline_cancel_24, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .build();

                    // Show Dialog
                    mDialog.show();
                }


            }
        });





    }



    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){

            Toast.makeText(this, "checked", Toast.LENGTH_SHORT).show();
        }
    }

    private void deposit(String userID, EditText amount,String ac_number) {



        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();

        Map<String,Object> map= new HashMap<>();


        map.put("date",getDateTime());
        map.put("amount",amount.getText().toString());
        map.put("withdraw_account",ac_number);
        map.put("status","pending");
        map.put("userID",userID);






        firestore.collection("Users").document(userID)
                .collection("Transactions")
                .document(userID)
                .collection("Withdraw")
                .add(map)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull  Task<DocumentReference> task) {
                        lottie.dismiss();

                        recipdialog(
                                amount.getText().toString(),
                                ac_number,
                                getIntent().getStringExtra("name"),
                                getDate()
                        );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        lottie.dismiss();
                        Toast.makeText(ActivityWithdraw.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });

    }



    public void acNumberdialog()
    {

        Dialog dialog = new Dialog(ActivityWithdraw.this);
        dialog.setContentView(R.layout.dialog_another_acount);
        Button btn_use = dialog.findViewById(R.id.btn_use);
        EditText txt_depositBalance = dialog.findViewById(R.id.edit_ac_amount);



        dialog.show();

        btn_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txt_depositBalance.getText().toString().isEmpty())
                    txt_depositBalance.setError("Please Enter Account Number");
                else {
                    txt_acNumber.setText(txt_depositBalance.getText().toString());
                    chk_acNumber.setEnabled(false);
                    dialog.cancel();
                }


            }
        });

    }

    public void recipdialog(String depositBalance,String depositAccount,String depositBy,String date)
    {

        Dialog dialog = new Dialog(ActivityWithdraw.this);
        dialog.setContentView(R.layout.dialog_voucherhu_wr);
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
                startActivity(new Intent(ActivityWithdraw.this, ActivityMain.class));
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
}