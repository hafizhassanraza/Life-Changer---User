package com.bitsnest.lifechanger.LC;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bitsnest.lifechanger.Adapter.Adapter_Notify;
import com.bitsnest.lifechanger.Adapter.Adapter_unNotify;
import com.bitsnest.lifechanger.Model.Model_Notify;
import com.bitsnest.lifechanger.Model.Model_unNotify;
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

public class ActivityNotification extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private Utils utils;

    List<Model_unNotify> list_unNotify = new ArrayList<>();
    List<Model_Notify> list_Notify = new ArrayList<>();
    RecyclerView recy_unNotify ;
    RecyclerView recy_Notify ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this);

        recy_unNotify = findViewById(R.id.list_UnNotify);
        recy_unNotify.setHasFixedSize(true);
        recy_unNotify.setLayoutManager(new LinearLayoutManager(this));

        recy_Notify = findViewById(R.id.list_Notify);
        recy_Notify.setHasFixedSize(true);
        recy_Notify.setLayoutManager(new LinearLayoutManager(this));

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
        list_Notify.clear();
        list_unNotify.clear();

        firestore.collection("Users").document(token)// whereEqualTo("studentID",token)
                .collection("Notifications").orderBy("date", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(document.getString("status").equals("unread")){
                                    Model_unNotify model_unNotify =  new Model_unNotify(
                                            document.getId(),
                                            document.getString("data"),
                                            document.getString("date"),
                                            document.getString("heading"));
                                    list_unNotify.add(model_unNotify);
                                }
                                if(document.getString("status").equals("read")){
                                    Model_Notify model_Notify =  new Model_Notify(
                                            document.getId(),
                                            document.getString("data"),
                                            document.getString("date"),
                                            document.getString("heading"));
                                    list_Notify.add(model_Notify);
                                }

                            }
                            Adapter_unNotify adapter_unNotify = new Adapter_unNotify(ActivityNotification.this,list_unNotify);
                            recy_unNotify.setAdapter(adapter_unNotify);

                            Adapter_Notify adapter_Notify = new Adapter_Notify(list_Notify);
                            recy_Notify.setAdapter(adapter_Notify);

                            lottie.dismiss();
                        }
                        else {
                            lottie.dismiss();
                            Toast.makeText(ActivityNotification.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

}