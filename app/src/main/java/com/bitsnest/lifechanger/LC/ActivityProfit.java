package com.bitsnest.lifechanger.LC;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bitsnest.lifechanger.Adapter.Adapter_Claimed;
import com.bitsnest.lifechanger.Adapter.Adapter_unClaimed;
import com.bitsnest.lifechanger.Model.Model_Claimed;
import com.bitsnest.lifechanger.Model.Model_unClaimed;
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

public class ActivityProfit extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private Utils utils;

    List<Model_unClaimed> list_unClaimed = new ArrayList<>();
    List<Model_Claimed> list_Claimed = new ArrayList<>();
    RecyclerView recy_unClaimed ;
    RecyclerView recy_Claimed ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this);

        recy_unClaimed = findViewById(R.id.list_UnClaimed);
        recy_unClaimed.setHasFixedSize(true);
        recy_unClaimed.setLayoutManager(new LinearLayoutManager(this));

        recy_Claimed = findViewById(R.id.list_Claimed);
        recy_Claimed.setHasFixedSize(true);
        recy_Claimed.setLayoutManager(new LinearLayoutManager(this));

        fetchNotifi(utils.getToken());
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swiperefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNotifi(utils.getToken());
                pullToRefresh.setRefreshing(false);
            }
        });
    }

    private void fetchNotifi(String token){

        final lottiedialog lottie=new lottiedialog(this);
        lottie.show();
        //list_unClaimed.clear();
        list_Claimed.clear();

        firestore.collection("Users").document(token)// whereEqualTo("studentID",token)
                .collection("Profit").orderBy("date", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

//                                if(document.getString("status").equals("unclaimed")){
//
//                                    int tempBalance=Integer.parseInt(getIntent().getStringExtra("balance"))
//                                            +Integer.parseInt(document.getString("amount"));
//
//                                    Model_unClaimed model_unClaimed =  new Model_unClaimed(
//                                            document.getId(),
//                                            document.getString("amount"),
//                                            document.getString("date"),
//                                            String.valueOf(tempBalance));
//                                    list_unClaimed.add(model_unClaimed);
//                                }
                                if(document.getString("status").equals("claimed")){
                                    Model_Claimed model_Claimed =  new Model_Claimed(
                                            document.getId(),
                                            document.getString("heading"),
                                            document.getString("data"),
                                            document.getString("date"));
                                    list_Claimed.add(model_Claimed);
                                }

                            }
//                            Adapter_unClaimed adapter_unClaimed = new Adapter_unClaimed(ActivityProfit.this,list_unClaimed);
//                            recy_unClaimed.setAdapter(adapter_unClaimed);

                            Adapter_Claimed adapter_Claimed = new Adapter_Claimed(list_Claimed);
                            recy_Claimed.setAdapter(adapter_Claimed);

                            lottie.dismiss();
                        }
                        else {
                            lottie.dismiss();
                            Toast.makeText(ActivityProfit.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}