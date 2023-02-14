package com.bitsnest.lifechanger.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitsnest.lifechanger.Model.Model_Investment;
import com.bitsnest.lifechanger.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class Adapter_Investment extends RecyclerView.Adapter<Adapter_Investment.ViewHolder> {
    private List<Model_Investment> model_Investment;

    Context context;

    // RecyclerView recyclerView;
    public Adapter_Investment(Context context,List<Model_Investment> model_Investment) {
        this.context = context;

        this.model_Investment = model_Investment;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_investment, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model_Investment current_Investment = model_Investment.get(position);



        holder.txt_date.setText(model_Investment.get(position).getDate());
        holder.txt_amount.setText(model_Investment.get(position).getAmount()+" Rs");
        holder.txt_AcDeposit.setText(model_Investment.get(position).getAc_deposit());
        Glide.with(holder.itemView)
                .load(model_Investment.get(position).getReceipt())
                .fitCenter()
                .into(holder.img_receipt);


        holder.img_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_photo);
                ImageView temp_photo = dialog.findViewById(R.id.img_photo);
                Glide.with(context)
                        .load(model_Investment.get(position).getReceipt())
                        .into(temp_photo);

                dialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return model_Investment.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public TextView txt_date,txt_amount,txt_AcDeposit;
        public ImageView img_receipt;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_date = (TextView) itemView.findViewById(R.id.txt_date_invest);
            this.txt_amount = (TextView) itemView.findViewById(R.id.txt_amount_invest);
            this.txt_AcDeposit = (TextView) itemView.findViewById(R.id.txt_depositAccount_investment);
            this.img_receipt = (ImageView) itemView.findViewById(R.id.img_receipt);

        }

    }
}


