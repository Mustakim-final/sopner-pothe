package com.example.ourproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ourproject.Model.EditorPayment;
import com.example.ourproject.Model.Notice;
import com.example.ourproject.R;

import java.util.List;

public class EditorPersonalEditAdapter extends ArrayAdapter<Notice> {

    List<Notice> noticeList;
    Context context;

    public EditorPersonalEditAdapter(Context context,List<Notice> noticeList) {
        super(context, R.layout.editor_personal_item,noticeList);
        this.context = context;
        this.noticeList = noticeList;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView= LayoutInflater.from(context).inflate(R.layout.editor_personal_item,parent,false);
        TextView noticeText,total,due;

        total=convertView.findViewById(R.id.editorPersonalTotal_ID);
        due=convertView.findViewById(R.id.editorPersonalDue_ID);

        Notice notice=noticeList.get(position);

        total.setText(notice.getTotal());
        due.setText(notice.getDue());

        return convertView;
    }
}
