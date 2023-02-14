package com.bitsnest.lifechanger.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bitsnest.lifechanger.Model.DashboardViewModel;
import com.bitsnest.lifechanger.R;
import com.bitsnest.lifechanger.LC.ActivityInvest;
import com.bitsnest.lifechanger.LC.ActivityInvestReq;
import com.bitsnest.lifechanger.LC.ActivityProfit;
import com.bitsnest.lifechanger.LC.ActivityTransaction;
import com.bitsnest.lifechanger.LC.ActivityWithdraw;
import com.bitsnest.lifechanger.LC.ActivityWithdrawReq;
import com.bitsnest.lifechanger.Utils;
import com.bitsnest.lifechanger.lottiedialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;


    private Utils utils;
    private FirebaseFirestore firestore;

    private int nCounter=0;
    private String balance,fname,lname,ac_number;
    private Button btn_profit;
    private TextView txt_balance,txt_sumcovered,txt_takaful;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        txt_balance=root.findViewById(R.id.txt_balance);
        txt_sumcovered=root.findViewById(R.id.txt_sumcovered);
        txt_takaful=root.findViewById(R.id.txt_takaful);
        Button btn_invest=root.findViewById(R.id.btn_niReq);
        Button btn_withdraw=root.findViewById(R.id.btn_nwReq);
        Button btn_iReq=root.findViewById(R.id.btn_iReq);
        Button btn_wReq=root.findViewById(R.id.btn_wReq);
        Button btn_transactions=root.findViewById(R.id.btn_transactions);
        btn_profit=root.findViewById(R.id.btn_profit);



        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this.getContext());

        fetchData(utils.getToken());
        //fetchProfit(utils.getToken());

        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData(utils.getToken());
                //fetchProfit(utils.getToken());
                pullToRefresh.setRefreshing(false);
            }
        });



        btn_profit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityProfit.class);
                intent.putExtra("balance", balance);
                startActivity(intent);
            }
        });

        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ActivityWithdraw.class);
                intent.putExtra("name", fname+" "+lname);
                intent.putExtra("balance", balance);
                intent.putExtra("ac_number", ac_number);


                startActivity(intent);
            }
        });
        btn_invest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ActivityInvest.class);
                intent.putExtra("name", fname+" "+lname);

                startActivity(intent);
            }
        });
        btn_iReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ActivityInvestReq.class);
                intent.putExtra("name", fname+" "+lname);
                startActivity(intent);
            }
        });
        btn_wReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ActivityWithdrawReq.class);
                intent.putExtra("name", fname+" "+lname);
                startActivity(intent);
            }
        });
        btn_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ActivityTransaction.class);
                intent.putExtra("name", fname+" "+lname);
                startActivity(intent);
            }
        });






        return root;
    }

    private void fetchData(String userId) {

        final lottiedialog lottie=new lottiedialog(getContext());
        lottie.show();

        firestore.collection("Users").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                balance=document.getString("balance");
                                ac_number=document.getString("ac_number");
                                fname=document.getString("fname");
                                lname=document.getString("lname");
                                txt_balance.setText(document.getString("balance"));
                                txt_sumcovered.setText(document.getString("sumcovered"));
                                txt_takaful.setText(document.getString("takaful"));
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

    private void fetchProfit(String token){

        final lottiedialog lottie=new lottiedialog(getContext());
        lottie.show();


        firestore.collection("Users").document(token)// whereEqualTo("studentID",token)
                .collection("Profit").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            nCounter=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(document.getString("status").equals("unclaimed")){
                                    nCounter++;
                                }
                            }
                            if(nCounter>9) btn_profit.setText("Profit (9+)");
                            else btn_profit.setText("Profit ("+String.valueOf(nCounter)+")");

                            lottie.dismiss();

                        }
                        else {
                            lottie.dismiss();
                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }



}