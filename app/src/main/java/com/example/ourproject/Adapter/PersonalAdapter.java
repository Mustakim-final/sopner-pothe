package com.example.ourproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ourproject.Model.Notice;
import com.example.ourproject.R;

import java.util.List;

public class PersonalAdapter extends RecyclerView.Adapter<PersonalAdapter.MyHolder> {
    Context context;
    List<Notice> noticeList;

    public PersonalAdapter(Context context, List<Notice> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.personal_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Notice notice=noticeList.get(position);
        holder.totalText.setText(notice.getTotal());
        holder.dueText.setText(notice.getDue());
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView totalText,dueText;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            totalText=itemView.findViewById(R.id.totalAmount_ID);
            dueText=itemView.findViewById(R.id.totalDue_ID);
        }
    }
}
