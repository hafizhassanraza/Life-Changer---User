package com.bitsnest.lifechanger.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitsnest.lifechanger.Model.Model_Claimed;
import com.bitsnest.lifechanger.R;

import java.util.List;

public class Adapter_Claimed extends RecyclerView.Adapter<Adapter_Claimed.ViewHolder> {
    private List<Model_Claimed> model_Claimed;

    // RecyclerView recyclerView;
    public Adapter_Claimed(List<Model_Claimed> model_Claimed) {
        this.model_Claimed = model_Claimed;

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
        final Model_Claimed current_Claimed = model_Claimed.get(position);
        holder.txt_date.setText(model_Claimed.get(position).getDate());
        holder.txt_head.setText(model_Claimed.get(position).getHead());
        holder.txt_body.setText(model_Claimed.get(position).getBody());

        holder.btn_claimed.setVisibility(View.GONE);
        holder.layout_Claimed.setBackgroundColor(Color.TRANSPARENT);// setBackgroundColor(Color.parseColor("#9F000000"));

//        holder.layout_Withdraw_req.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               /* Intent intent = new Intent(view.getContext(), ActivityNotificationDetails.class);
//                intent.putExtra("id", current_Claimed.getNotificatioID().toString().trim());
//                view.getContext().startActivity(intent);*/
//                //Toast.makeText(view.getContext(),"click on item: "+current_Claimed.getAmount().toString().trim(), Toast.LENGTH_LONG).show();
//                //Toast.makeText(view.getContext(),"click on item: "+current_investor.get_name_Investor(), Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return model_Claimed.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public TextView txt_date,txt_body,txt_head;
        public LinearLayout layout_Claimed;
        public Button btn_claimed;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_date = (TextView) itemView.findViewById(R.id.txt_date_profit);
            this.txt_head = (TextView) itemView.findViewById(R.id.txt_head);
            this.txt_body = (TextView) itemView.findViewById(R.id.txt_body);
            layout_Claimed = (LinearLayout)itemView.findViewById(R.id.layout_Claimed);
            btn_claimed = (Button) itemView.findViewById(R.id.btn_claim);
        }

    }
}

