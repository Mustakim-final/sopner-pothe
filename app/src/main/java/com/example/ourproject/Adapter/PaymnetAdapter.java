package com.example.ourproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ourproject.Model.Payment;
import com.example.ourproject.R;

import java.util.List;

public class PaymnetAdapter extends RecyclerView.Adapter<PaymnetAdapter.MyHolder>{
    Context context;
    List<Payment> paymentList;

    public PaymnetAdapter(Context context, List<Payment> paymentList) {
        this.context = context;
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.profile_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        if (paymentList!=null && paymentList.size()>0){
            Payment payment=paymentList.get(position);
            holder.nameText.setText(payment.getName());
            holder.amountText.setText(payment.getAmount());
            holder.dateText.setText(payment.getDate());
            holder.paidDateText.setText(payment.getPaidDate());
            holder.statusText.setText(payment.getStatus());

        }else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView nameText,amountText,dateText,paidDateText,statusText;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            nameText=itemView.findViewById(R.id.tbName_ID);
            amountText=itemView.findViewById(R.id.tbAmount_ID);
            dateText=itemView.findViewById(R.id.tbDate_ID);
            paidDateText=itemView.findViewById(R.id.tbPayedDate_ID);
            statusText=itemView.findViewById(R.id.tbStatus_ID);
        }
    }
}
