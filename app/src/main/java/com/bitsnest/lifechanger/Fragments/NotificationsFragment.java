package com.bitsnest.lifechanger.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bitsnest.lifechanger.Utils;
import com.bitsnest.lifechanger.Model.NotificationsViewModel;
import com.bitsnest.lifechanger.R;
import com.bitsnest.lifechanger.LC.ActivityAccountEdit;
import com.bitsnest.lifechanger.LC.ActivityNomineeEdit;
import com.bitsnest.lifechanger.LC.ActivityUserEdit;
import com.bitsnest.lifechanger.Utils;
import com.bitsnest.lifechanger.lottiedialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class NotificationsFragment extends Fragment {

    private Utils utils;
    private FirebaseFirestore firestore;


    private boolean zoomOut =  false;

    private ImageView img_userProfile;
    private TextView txt_name,txt_cnic,txt_number,txt_acID,txt_acBank,txt_acNumber,txt_name_nominee,txt_cnic_nominee,txt_number_nominee;
    private String profile,fname,lname,cnic,address,number,acID,acBank,acNumber,name_nominee,cnic_nominee,number_nominee,address_nominee;
    private ImageButton btn_edit,btn_edit_account,btn_edit_nominee;


    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });


        img_userProfile=root.findViewById(R.id.img_userProfile);
        txt_name=root.findViewById(R.id.txt_name);
        txt_cnic=root.findViewById(R.id.txt_cnic);
        txt_number=root.findViewById(R.id.txt_number);
        txt_acID=root.findViewById(R.id.txt_acID);
        txt_acBank=root.findViewById(R.id.txt_acBank);
        txt_acNumber=root.findViewById(R.id.txt_acNumber);
        txt_name_nominee=root.findViewById(R.id.txt_name_nominee);
        txt_cnic_nominee=root.findViewById(R.id.txt_cnic_nominee);
        txt_number_nominee=root.findViewById(R.id.txt_number_nominee);

        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this.getContext());




        btn_edit=root.findViewById(R.id.btn_edit);
        btn_edit_account=root.findViewById(R.id.btn_edit_account);
        btn_edit_nominee=root.findViewById(R.id.btn_edit_nominee);


        fetchData(utils.getToken());

        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData(utils.getToken());
                pullToRefresh.setRefreshing(false);
            }
        });

        img_userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                photodialog(profile);

//                fullScreen();
//                if(zoomOut) {
//                    Toast.makeText(getContext(), "NORMAL SIZE!", Toast.LENGTH_LONG).show();
//                    img_userProfile.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                    img_userProfile.setAdjustViewBounds(true);
//                    zoomOut =false;
//                }else{
//                    Toast.makeText(getContext(), "FULLSCREEN!", Toast.LENGTH_LONG).show();
//                    img_userProfile.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//                    img_userProfile.setScaleType(ImageView.ScaleType.FIT_XY);
//                    zoomOut = true;
//                }
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), ""+fname+lname+address+number, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ActivityUserEdit.class);
                intent.putExtra("profile", profile);
                intent.putExtra("fname", fname);
                intent.putExtra("lname", lname);
                intent.putExtra("address", address);
                intent.putExtra("number", number);
                startActivity(intent);
            }
        });
        btn_edit_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityAccountEdit.class);
                intent.putExtra("acID", acID);
                intent.putExtra("acBank", acBank);
                intent.putExtra("acNumber", acNumber);
                startActivity(intent);
            }
        });
        btn_edit_nominee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityNomineeEdit.class);
                intent.putExtra("name_nominee", name_nominee);
                intent.putExtra("cnic_nominee", cnic_nominee);
                intent.putExtra("number_nominee", number_nominee);
                intent.putExtra("address_nominee", address_nominee);
                startActivity(intent);
            }
        });






        return root;
    }

    private void fetchData(String userID) {


        final lottiedialog lottie=new lottiedialog(getContext());
        lottie.show();

        DocumentReference docRef = firestore.collection("Users").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {


                        profile=document.getString("profile");
                        Glide.with(getContext()).load(profile).into(img_userProfile);
                        fname=document.getString("fname");
                        lname=document.getString("lname");
                        txt_name.setText(fname+" "+lname);
                        cnic=document.getString("cnic");
                        txt_cnic.setText(cnic);
                        address=document.getString("address");
                        number=document.getString("phone");
                        txt_number.setText(number);
                        acID=document.getString("ac_name");
                        txt_acID.setText(acID);
                        acBank=document.getString("ac_bank");
                        txt_acBank.setText(acBank);
                        acNumber=document.getString("ac_number");
                        txt_acNumber.setText(acNumber);


                        cnic_nominee=document.getString("n_cnic");
                        txt_cnic_nominee.setText(cnic_nominee);
                        name_nominee=document.getString("n_name");
                        txt_name_nominee.setText(name_nominee);
                        number_nominee=document.getString("n_phone");
                        txt_number_nominee.setText(number_nominee);
                        address_nominee=document.getString("n_address");

                        lottie.dismiss();
                    }
                }
            }

        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        lottie.dismiss();
                        Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    public void photodialog(String photo)
    {

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_photo);
        ImageView temp_photo = dialog.findViewById(R.id.img_photo);
        Glide.with(this).load(photo).into(temp_photo);

        dialog.show();

    }

}