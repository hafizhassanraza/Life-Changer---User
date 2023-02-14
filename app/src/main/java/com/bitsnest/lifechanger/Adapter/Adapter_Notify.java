package com.bitsnest.lifechanger.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitsnest.lifechanger.Model.Model_Notify;
import com.bitsnest.lifechanger.R;

import java.util.List;

public class Adapter_Notify extends RecyclerView.Adapter<Adapter_Notify.ViewHolder> {
    private List<Model_Notify> model_Notify;


    // RecyclerView recyclerView;
    public Adapter_Notify(List<Model_Notify> model_Notify) {
        this.model_Notify = model_Notify;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_un_notify, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model_Notify current_Notify = model_Notify.get(position);
        holder.txt_date.setText(model_Notify.get(position).getDate());
        holder.txt_name.setText(model_Notify.get(position).getHeading());
        holder.txt_notifi.setText(model_Notify.get(position).getData());

        holder.layout_Notify.setBackgroundColor(Color.TRANSPARENT);// setBackgroundColor(Color.parseColor("#9F000000"));

//        holder.layout_Withdraw_req.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               /* Intent intent = new Intent(view.getContext(), ActivityNotificationDetails.class);
//                intent.putExtra("id", current_Notify.getNotificatioID().toString().trim());
//                view.getContext().startActivity(intent);*/
//                //Toast.makeText(view.getContext(),"click on item: "+current_Notify.getAmount().toString().trim(), Toast.LENGTH_LONG).show();
//                //Toast.makeText(view.getContext(),"click on item: "+current_investor.get_name_Investor(), Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return model_Notify.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public TextView txt_date,txt_name,txt_notifi;
        public LinearLayout layout_Notify;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            this.txt_name = (TextView) itemView.findViewById(R.id.txt_header_notify);
            this.txt_notifi = (TextView) itemView.findViewById(R.id.txt_notifi);
            layout_Notify = (LinearLayout)itemView.findViewById(R.id.layout_Notify);
        }

    }
}


