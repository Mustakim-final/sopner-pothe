package com.example.ourproject.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ourproject.Model.EditorPayment;
import com.example.ourproject.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditorProfileAdapter extends ArrayAdapter<EditorPayment> {

    List<EditorPayment> editorPaymentList;
    Context context;

    public EditorProfileAdapter(Context context,List<EditorPayment> editorPaymentList) {
        super(context, R.layout.editor_profile_item,editorPaymentList);
        this.context=context;
        this.editorPaymentList = editorPaymentList;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        convertView=LayoutInflater.from(context).inflate(R.layout.editor_profile_item,parent,false);
        TextView name,amount,date,paidDate,status;
        name=convertView.findViewById(R.id.editortbName_ID);
        amount=convertView.findViewById(R.id.editortbAmount_ID);
        date=convertView.findViewById(R.id.editortbDate_ID);
        paidDate=convertView.findViewById(R.id.editortbPayedDate_ID);
        status=convertView.findViewById(R.id.editortbStatus_ID);

        EditorPayment editorPayment=editorPaymentList.get(position);
        name.setText(editorPayment.getName());
        amount.setText(editorPayment.getAmount());
        date.setText(editorPayment.getDate());
        paidDate.setText(editorPayment.getPaidDate());
        status.setText(editorPayment.getStatus());

        return convertView;
    }
}
