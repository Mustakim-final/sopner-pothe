package com.example.ourproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ourproject.Model.EditorNotice;
import com.example.ourproject.Model.Notice;
import com.example.ourproject.R;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyViewHolder> {
    Context context;
    List<EditorNotice> editorNoticeList;

    public NoticeAdapter(Context context, List<EditorNotice> editorNoticeList) {
        this.context = context;
        this.editorNoticeList = editorNoticeList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notice_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EditorNotice editorNotice=editorNoticeList.get(position);
        holder.textView.setText(editorNotice.getNotice());
    }

    @Override
    public int getItemCount() {
        return editorNoticeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView=itemView.findViewById(R.id.noticeText_ID);
        }
    }
}
