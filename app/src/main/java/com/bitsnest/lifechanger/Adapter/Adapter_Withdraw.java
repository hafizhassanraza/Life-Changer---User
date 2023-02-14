package com.bitsnest.lifechanger.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitsnest.lifechanger.Model.Model_Withdraw;
import com.bitsnest.lifechanger.R;

import java.util.List;

public class Adapter_Withdraw extends RecyclerView.Adapter<Adapter_Withdraw.ViewHolder> {
    private List<Model_Withdraw> model_Withdraw;

    Context context;

    // RecyclerView recyclerView;
    public Adapter_Withdraw(Context context,List<Model_Withdraw> model_Withdraw) {
        this.context = context;

        this.model_Withdraw = model_Withdraw;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_withdraw, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model_Withdraw current_Withdraw = model_Withdraw.get(position);



        holder.txt_date.setText(model_Withdraw.get(position).getDate());
        holder.txt_amount.setText(model_Withdraw.get(position).getAmount()+" Rs");
        holder.txt_AcWithdraw.setText(model_Withdraw.get(position).getAc_withdraw());


    }

    @Override
    public int getItemCount() {
        return model_Withdraw.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public TextView txt_date,txt_amount,txt_AcWithdraw;
        public ImageView img_receipt;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_date = (TextView) itemView.findViewById(R.id.txt_date_invest);
            this.txt_amount = (TextView) itemView.findViewById(R.id.txt_amount_invest);
            this.txt_AcWithdraw = (TextView) itemView.findViewById(R.id.txt_Account_Withdraw);
            this.img_receipt = (ImageView) itemView.findViewById(R.id.img_receipt);

        }

    }
}


