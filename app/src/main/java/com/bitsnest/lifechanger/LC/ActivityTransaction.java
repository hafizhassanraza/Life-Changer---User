package com.bitsnest.lifechanger.LC;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bitsnest.lifechanger.Adapter.Adapter_Investment;
import com.bitsnest.lifechanger.Adapter.Adapter_Withdraw;
import com.bitsnest.lifechanger.Model.Model_Investment;
import com.bitsnest.lifechanger.Model.Model_Withdraw;
import com.bitsnest.lifechanger.R;
import com.bitsnest.lifechanger.Utils;
import com.bitsnest.lifechanger.lottiedialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActivityTransaction extends AppCompatActivity {


    private FirebaseFirestore firestore;
    private Utils utils;

    private List<Model_Withdraw> list_Withdraw ;
    RecyclerView recy_Withdraw ;

    private List<Model_Investment> list_Investment ;
    RecyclerView recy_Investment ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this);



        list_Withdraw= new ArrayList<Model_Withdraw>();
        recy_Withdraw = findViewById(R.id.list_tWithdraw);
        recy_Withdraw.setHasFixedSize(true);
        recy_Withdraw.setLayoutManager(new LinearLayoutManager(this));

        list_Investment= new ArrayList<Model_Investment>();
        recy_Investment = findViewById(R.id.list_tInvest);
        recy_Investment.setHasFixedSize(true);
        recy_Investment.setLayoutManager(new LinearLayoutManager(this));

        fetchData(utils.getToken());
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData(utils.getToken());
                pullToRefresh.setRefreshing(false);
            }
        });
    }


    private void fetchData(String token){


        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();
        list_Withdraw.clear();
        list_Investment.clear();

        firestore.collection("Users").document(token)// whereEqualTo("studentID",token)
                .collection("Transactions").document(token)
                .collection("Withdraw").orderBy("date", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(document.getString("status").equals("proceed")){
                                    Model_Withdraw model_Withdraw =  new Model_Withdraw(
                                            document.getId(),
                                            document.getString("amount"),
                                            document.getString("date"),
                                            document.getString("withdraw_account"));
                                    list_Withdraw.add(model_Withdraw);
                                }

                            }


                            Adapter_Withdraw adapter_Withdraw = new Adapter_Withdraw(ActivityTransaction.this,list_Withdraw);
                            recy_Withdraw.setAdapter(adapter_Withdraw);




                            firestore.collection("Users").document(token)// whereEqualTo("studentID",token)
                                    .collection("Transactions").document(token)
                                    .collection("Invest").orderBy("date", Query.Direction.ASCENDING).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    if(document.getString("status").equals("proceed")){
                                                        Model_Investment model_Investment =  new Model_Investment(
                                                                document.getId(),
                                                                document.getString("amount"),
                                                                document.getString("date"),
                                                                document.getString("deposit_account"),
                                                                document.getString("receipt"));
                                                        list_Investment.add(model_Investment);
                                                    }

                                                }


                                                Adapter_Investment adapter_Investment = new Adapter_Investment(ActivityTransaction.this,list_Investment);
                                                recy_Investment.setAdapter(adapter_Investment);

                                                lottie.dismiss();
                                            }
                                            else {
                                                lottie.dismiss();
                                                Toast.makeText(ActivityTransaction.this, "failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        }
                        else {
                            Toast.makeText(ActivityTransaction.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




    }



}