package com.bitsnest.lifechanger.LC;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bitsnest.lifechanger.Adapter.Adapter_Withdraw;
import com.bitsnest.lifechanger.Model.Model_Withdraw;
import com.bitsnest.lifechanger.R;
import com.bitsnest.lifechanger.Utils;
import com.bitsnest.lifechanger.lottiedialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ActivityWithdrawReq extends AppCompatActivity {


    private FirebaseFirestore firestore;
    private Utils utils;

    private List<Model_Withdraw> list_Withdraw ;
    RecyclerView recy_Withdraw ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_req);
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this);



        list_Withdraw= new ArrayList<Model_Withdraw>();
        recy_Withdraw = findViewById(R.id.list_Withdraw);
        recy_Withdraw.setHasFixedSize(true);
        recy_Withdraw.setLayoutManager(new LinearLayoutManager(this));

        fetchWithdraw(utils.getToken());
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchWithdraw(utils.getToken());
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    private void fetchWithdraw(String token){


        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();
        list_Withdraw.clear();

        firestore.collection("Users").document(token)// whereEqualTo("studentID",token)
                .collection("Transactions").document(token)
                .collection("Withdraw").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(document.getString("status").equals("pending")){
                                    Model_Withdraw model_Withdraw =  new Model_Withdraw(
                                            document.getId(),
                                            document.getString("amount"),
                                            document.getString("date"),
                                            document.getString("withdraw_account"));
                                    list_Withdraw.add(model_Withdraw);
                                }

                            }


                            Adapter_Withdraw adapter_Withdraw = new Adapter_Withdraw(ActivityWithdrawReq.this,list_Withdraw);
                            recy_Withdraw.setAdapter(adapter_Withdraw);

                            lottie.dismiss();
                        }
                        else {
                            lottie.dismiss();
                            Toast.makeText(ActivityWithdrawReq.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}