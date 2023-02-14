package com.bitsnest.lifechanger.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitsnest.lifechanger.LC.ActivityMain;
import com.bitsnest.lifechanger.Model.Model_unClaimed;
import com.bitsnest.lifechanger.R;
import com.bitsnest.lifechanger.Utils;
import com.bitsnest.lifechanger.lottiedialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter_unClaimed extends RecyclerView.Adapter<Adapter_unClaimed.ViewHolder> {
    private List<Model_unClaimed> model_unClaimed;
    Context context;
    private FirebaseFirestore firestore;
    private Utils utils;


    LayoutInflater inflater;
    private OnItemClickListener mListener;

    /////////////////////click listner for outside adopter
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



    // RecyclerView recyclerView;
    public Adapter_unClaimed(Context context,List<Model_unClaimed> model_unClaimed) {
        this.model_unClaimed = model_unClaimed;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_un_claimed, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model_unClaimed current_unClaimed = model_unClaimed.get(position);
        holder.txt_date.setText(model_unClaimed.get(position).getDate());
        holder.txt_balance.setText(model_unClaimed.get(position).getAmount()+" Rs");

        holder.btn_claimed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final lottiedialog lottie=new lottiedialog(context);
                lottie.show();
                Map<String, Object> m = new HashMap<>();
                m.put("status", "claimed");
                firestore.collection("Users").document(utils.getToken())// whereEqualTo("studentID",token)
                        .collection("Profit")
                        .document(model_unClaimed.get(position).getId()).update(m)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {


                                    lottie.dismiss();
                                    Intent intent = new Intent(context, ActivityMain.class);
                                    ((Activity)context).startActivity(intent);
                                    ((Activity)context).finish();

//
//                                    Map<String, Object> m = new HashMap<>();
//                                    m.put("balance", model_unClaimed.get(position).getTotalbalance());
//                                    firestore.collection("Users").document(utils.getToken())// whereEqualTo("studentID",token)
//                                             .update(m)
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if (task.isSuccessful()) {
//
//
//                                                        lottie.dismiss();
//                                                        Toast.makeText(context, "Balance Updated!", Toast.LENGTH_SHORT).show();
//                                                        holder.layout_Claimed.setBackgroundColor(Color.TRANSPARENT);// setBackgroundColor(Color.parseColor("#9F000000"));
//                                                        Intent intent = new Intent(context, ActivityMain.class);
//                                                        ((Activity)context).startActivity(intent);
//                                                        ((Activity)context).finish();
//
//                                                    }
//
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull @NotNull Exception e) {
//                                                    lottie.dismiss();
//                                                }
//                                            });
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull  Exception e) {
                                lottie.dismiss();
                            }
                        });
               /* Intent intent = new Intent(view.getContext(), ActivityNotificationDetails.class);
                intent.putExtra("id", current_unClaimed.getNotificatioID().toString().trim());
                view.getContext().startActivity(intent);*/
                //Toast.makeText(view.getContext(),"click on item: "+current_unClaimed.getAmount().toString().trim(), Toast.LENGTH_LONG).show();
                //Toast.makeText(view.getContext(),"click on item: "+current_investor.get_name_Investor(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return model_unClaimed.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public TextView txt_date,txt_name,txt_balance;
        public LinearLayout layout_Claimed;
        public Button btn_claimed;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_date = (TextView) itemView.findViewById(R.id.txt_date_profit);
//            this.txt_balance = (TextView) itemView.findViewById(R.id.txt_balance_profit);
            layout_Claimed = (LinearLayout)itemView.findViewById(R.id.layout_Claimed);
            btn_claimed = (Button) itemView.findViewById(R.id.btn_claim);
        }
    }
}

