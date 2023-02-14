package com.bitsnest.lifechanger.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitsnest.lifechanger.Model.Model_Announ;
import com.bitsnest.lifechanger.R;

import java.util.List;

public class Adapter_Announ extends RecyclerView.Adapter<Adapter_Announ.ViewHolder> {
    private List<Model_Announ> model_Announ;

    // RecyclerView recyclerView;
    public Adapter_Announ(List<Model_Announ> model_Announ) {
        this.model_Announ = model_Announ;
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
        final Model_Announ current_Announ = model_Announ.get(position);
        holder.txt_date.setText(model_Announ.get(position).getDate());
        holder.txt_name.setText(model_Announ.get(position).getHeading());
        holder.txt_notifi.setText(model_Announ.get(position).getData());

        holder.layout_Announ.setBackgroundColor(Color.TRANSPARENT);// setBackgroundColor(Color.parseColor("#9F000000"));

//        holder.layout_Withdraw_req.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               /* Intent intent = new Intent(view.getContext(), ActivityNotificationDetails.class);
//                intent.putExtra("id", current_Announ.getNotificatioID().toString().trim());
//                view.getContext().startActivity(intent);*/
//                //Toast.makeText(view.getContext(),"click on item: "+current_Announ.getAmount().toString().trim(), Toast.LENGTH_LONG).show();
//                //Toast.makeText(view.getContext(),"click on item: "+current_investor.get_name_Investor(), Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return model_Announ.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public TextView txt_date,txt_name,txt_notifi;
        public LinearLayout layout_Announ;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            this.txt_name = (TextView) itemView.findViewById(R.id.txt_header_notify);
            this.txt_notifi = (TextView) itemView.findViewById(R.id.txt_notifi);
            layout_Announ = (LinearLayout)itemView.findViewById(R.id.layout_Notify);
        }

    }
}


