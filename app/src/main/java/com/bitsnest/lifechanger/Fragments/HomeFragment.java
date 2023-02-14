package com.bitsnest.lifechanger.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.bitsnest.lifechanger.Model.HomeViewModel;
import com.bitsnest.lifechanger.R;
import com.bitsnest.lifechanger.SliderItem;
import com.bitsnest.lifechanger.LC.ActivityAnnouncement;
import com.bitsnest.lifechanger.LC.ActivityNotification;
import com.bitsnest.lifechanger.LC.ActivitySignIn;
import com.bitsnest.lifechanger.Utils;
import com.bitsnest.lifechanger.lottiedialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
//import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
//import com.smarteist.autoimageslider.SliderAnimations;
//import com.smarteist.autoimageslider.SliderView;



import java.util.ArrayList;
import java.util.List;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import static android.provider.Telephony.BaseMmsColumns.SUBJECT;
import static android.provider.Telephony.TextBasedSmsColumns.BODY;

public class HomeFragment extends Fragment {


    private Utils utils;
    private FirebaseFirestore firestore;


    private String adm_tittle_acc1,adm_bank_acc1,adm_number_acc1,adm_tittle_acc2,adm_bank_acc2,adm_number_acc2;
    private String adm_name,adm_mail,adm_call,adm_whatsapp;

    private int nCounter=0,aCounter=0;
//    SliderView sliderView;
//    private SliderAdapterExample adapter;
    private HomeViewModel homeViewModel;
    private LinearLayout lout_logout,lout_contactUs,lout_account,lout_notifications,lout_announcement;
    private TextView txt_notifiCounter,txt_anouncCounter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        ImageView img=root.findViewById(R.id.not);
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this.getContext());
//        sliderView = root.findViewById(R.id.imageSlider);


        fetchContacts();
        fetchNotifi(utils.getToken());
//        newsSlider();

        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchContacts();
                fetchNotifi(utils.getToken());
//                newsSlider();
                pullToRefresh.setRefreshing(false);
            }
        });







        txt_notifiCounter = root.findViewById(R.id.txt_notifiCounter);
        txt_anouncCounter = root.findViewById(R.id.txt_anouncCounter);

        lout_announcement = root.findViewById(R.id.lout_announcement);
        lout_announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ActivityAnnouncement.class);
                startActivity(intent);

            }
        });
        lout_notifications = root.findViewById(R.id.lout_notifications);
        lout_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ActivityNotification.class);
                startActivity(intent);

            }
        });
        lout_logout = root.findViewById(R.id.lout_logout);
        lout_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();

            }
        });
        lout_contactUs = root.findViewById(R.id.lout_contactUs);
        lout_contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactUs();
            }
        });
        lout_account = root.findViewById(R.id.lout_account);
        lout_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depositAccount();
            }
        });


        //final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    private void depositAccount() {


        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View vie = getLayoutInflater().inflate(R.layout.bottom_sheet_account, null);


        TextView txt_adm_tittle_acc1 = vie.findViewById(R.id.adm_tittle_acc1);
        TextView txt_adm_bank_acc1 = vie.findViewById(R.id.adm_bank_acc1);
        TextView txt_adm_number_acc1 = vie.findViewById(R.id.adm_number_acc1);
        txt_adm_tittle_acc1.setText(adm_tittle_acc1);
        txt_adm_bank_acc1.setText(adm_bank_acc1);
        txt_adm_number_acc1.setText(adm_number_acc1);

        TextView txt_adm_tittle_acc2 = vie.findViewById(R.id.adm_tittle_acc2);
        TextView txt_adm_bank_acc2 = vie.findViewById(R.id.adm_bank_acc2);
        TextView txt_adm_number_acc2 = vie.findViewById(R.id.adm_number_acc2);
        txt_adm_tittle_acc2.setText(adm_tittle_acc2);
        txt_adm_bank_acc2.setText(adm_bank_acc2);
        txt_adm_number_acc2.setText(adm_number_acc2);

        LinearLayout lout_copy2 = vie.findViewById(R.id.lout_copy2);
        LinearLayout lout_copy1 = vie.findViewById(R.id.lout_copy1);


        dialog.setContentView(vie);
        dialog.setCancelable(true);
        dialog.show();

        lout_copy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClipboard(getActivity().getApplicationContext(),adm_number_acc1);
            }
        });

        lout_copy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClipboard(getActivity().getApplicationContext(),adm_number_acc2);
            }
        });


    }


    private void contactUs() {


        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View vie = getLayoutInflater().inflate(R.layout.bottam_sheet_contacts, null);


        TextView txt_admin_name = vie.findViewById(R.id.txt_admin_name);
        txt_admin_name.setText(adm_name);
        TextView cwNumber = vie.findViewById(R.id.cwNumber);
        cwNumber.setText(adm_whatsapp);
        TextView cpNumber = vie.findViewById(R.id.cpNumber);
        cpNumber.setText(adm_call);
        TextView cmAddress = vie.findViewById(R.id.cmAddress);
        cmAddress.setText(adm_mail);
        Button btn_cwNumber = vie.findViewById(R.id.btn_cwNumber);
        Button btn_cpNumber = vie.findViewById(R.id.btn_cpNumber);
        Button btn_cmAddress = vie.findViewById(R.id.btn_cmAddress);

        dialog.setContentView(vie);
        dialog.setCancelable(true);
        dialog.show();

        btn_cpNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = adm_whatsapp; // use country code with your phone number
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + contact));
                startActivity(i);
            }
        });

        btn_cwNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = adm_call; // use country code with your phone number
                String url = "https://api.whatsapp.com/send?phone=" + contact;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        btn_cmAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", adm_mail, null));
                i.putExtra(Intent.EXTRA_SUBJECT, SUBJECT);
                i.putExtra(Intent.EXTRA_TEXT, BODY);
                startActivity(i);
            }
        });
    }

    private void fetchContacts() {

        final lottiedialog lottie=new lottiedialog(getContext());
        lottie.show();
        firestore.collection("Admin").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            for (QueryDocumentSnapshot document : task.getResult()){
                                adm_name=document.getString("fname")+" "+document.getString("lname");
                                adm_mail=document.getString("email");
                                adm_call=document.getString("cnumber");
                                adm_whatsapp=document.getString("wnumber");


                                adm_tittle_acc1=document.getString("tittle_acc1");
                                adm_bank_acc1=document.getString("bank_acc1");
                                adm_number_acc1=document.getString("number_acc1");

                                adm_tittle_acc2=document.getString("tittle_acc2");
                                adm_bank_acc2=document.getString("bank_acc2");
                                adm_number_acc2=document.getString("number_acc2");

                            }
                            lottie.dismiss();

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


    private void fetchNotifi(String token){

        final lottiedialog lottie=new lottiedialog(getContext());
        lottie.show();


        firestore.collection("Users").document(token)// whereEqualTo("studentID",token)
                .collection("Notifications").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {


                            nCounter=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(document.getString("status").equals("unread")){
                                    nCounter++;
                                }
                            }
                            if(nCounter>9) txt_notifiCounter.setText("9+");
                            else txt_notifiCounter.setText(String.valueOf(nCounter));

                            firestore.collectionGroup("Announcement").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {

                                                aCounter=0;
                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    if(document.getString("status").equals("unread")){
                                                        aCounter++;
                                                    }
                                                }
                                                if(aCounter>9) txt_anouncCounter.setText("9+");
                                                else txt_anouncCounter.setText(String.valueOf(aCounter));

                                                lottie.dismiss();
                                            }
                                            else {
                                                lottie.dismiss();
                                                Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else {
                            lottie.dismiss();
                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }



    ////Image slider functions/////////////////

//    public void newsSlider(){
//
//
//        final lottiedialog lottie=new lottiedialog(getContext());
//        lottie.show();
//        adapter = new SliderAdapterExample(this.getContext());
//        sliderView.setSliderAdapter(adapter);
//        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
//        sliderView.setIndicatorSelectedColor(Color.WHITE);
//        sliderView.setIndicatorUnselectedColor(Color.GRAY);
//        sliderView.setScrollTimeInSec(3);
//        sliderView.setAutoCycle(true);
//        sliderView.startAutoCycle();
//
//        List<SliderItem> sliderItemList = new ArrayList<>();
//
//        firestore.collectionGroup("Banners").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                SliderItem sliderItem = new SliderItem();
//                                sliderItem.setImageUrl(document.getString("photo"));
//                                sliderItemList.add(sliderItem);
//                            }
//                            adapter.renewItems(sliderItemList);
//
//                            lottie.dismiss();
//                        }
//                        else {
//                            lottie.dismiss();
//                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull @NotNull Exception e) {
//                        lottie.dismiss();
//                        Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//    }
//
//    public void renewItems(View view) {
//        List<SliderItem> sliderItemList = new ArrayList<>();
//        //dummy data
//        for (int i = 0; i < 5; i++) {
//            SliderItem sliderItem = new SliderItem();
//            sliderItem.setDescription("Slider Item " + i);
//            if (i % 2 == 0) {
//                sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
//            } else {
//                sliderItem.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
//            }
//            sliderItemList.add(sliderItem);
//        }
//        adapter.renewItems(sliderItemList);
//    }
//
//    public void removeLastItem(View view) {
//        if (adapter.getCount() - 1 >= 0)
//            adapter.deleteItem(adapter.getCount() - 1);
//    }
//
//    public void addNewItem(View view) {
//        SliderItem sliderItem = new SliderItem();
//        sliderItem.setDescription("Slider Item Added Manually");
//        sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
//        adapter.addItem(sliderItem);
//    }

    //////////////////////////////////////////////////////
    public void logout(){
        MaterialDialog mDialog = new MaterialDialog.Builder(this.getActivity())
                .setTitle("Logout")
                .setMessage("Are you sure want to logout!")
                .setCancelable(false)
                .setPositiveButton("Logout", R.drawable.ic_baseline_exit_to_app_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        utils.logout();
                        startActivity(new Intent(getContext(), ActivitySignIn.class));
                        getActivity().finish();
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

    private void setClipboard(Context context, String text) {
        Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show();
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }
}
