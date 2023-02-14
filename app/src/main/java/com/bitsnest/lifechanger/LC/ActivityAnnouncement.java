package com.bitsnest.lifechanger.LC;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bitsnest.lifechanger.Adapter.Adapter_Announ;
import com.bitsnest.lifechanger.Adapter.Adapter_unAnnoun;
import com.bitsnest.lifechanger.Model.Model_Announ;
import com.bitsnest.lifechanger.Model.Model_unAnnoun;
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

public class ActivityAnnouncement extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private Utils utils;

    List<Model_unAnnoun> list_unAnnoun = new ArrayList<>();
    List<Model_Announ> list_Announ = new ArrayList<>();
    RecyclerView recy_unAnnoun ;
    RecyclerView recy_Announ ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(this);

        recy_unAnnoun = findViewById(R.id.list_UnAnnoun);
        recy_unAnnoun.setHasFixedSize(true);
        recy_unAnnoun.setLayoutManager(new LinearLayoutManager(this));

        recy_Announ = findViewById(R.id.list_Announ);
        recy_Announ.setHasFixedSize(true);
        recy_Announ.setLayoutManager(new LinearLayoutManager(this));

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

        list_Announ.clear();
        list_unAnnoun.clear();
        firestore.collection("Announcement").orderBy("date", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(document.getString("status").equals("unread")){
                                    Model_unAnnoun model_unAnnoun =  new Model_unAnnoun(
                                            document.getId(),
                                            document.getString("data"),
                                            document.getString("date"),
                                            document.getString("heading"));
                                    list_unAnnoun.add(model_unAnnoun);
                                }
                                if(document.getString("status").equals("read")){
                                    Model_Announ model_Announ =  new Model_Announ(
                                            document.getId(),
                                            document.getString("data"),
                                            document.getString("date"),
                                            document.getString("heading"));
                                    list_Announ.add(model_Announ);
                                }

                            }
                            Adapter_unAnnoun adapter_unAnnoun = new Adapter_unAnnoun(ActivityAnnouncement.this,list_unAnnoun);
                            recy_unAnnoun.setAdapter(adapter_unAnnoun);

                            Adapter_Announ adapter_Announ = new Adapter_Announ(list_Announ);
                            recy_Announ.setAdapter(adapter_Announ);

                            lottie.dismiss();
                        }
                        else {
                            lottie.dismiss();
                            Toast.makeText(ActivityAnnouncement.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

}