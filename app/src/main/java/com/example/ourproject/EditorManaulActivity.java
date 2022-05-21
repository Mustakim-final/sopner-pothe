package com.example.ourproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditorManaulActivity extends AppCompatActivity {
    EditText billerNameEdit,amountEdit,dateEdit,tranjectionEdit,statusEdit,personalTotal,personalDue;
    Button submitBtn,personalBtn;

    FirebaseUser firebaseUser;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_manaul);

        billerNameEdit=findViewById(R.id.billManualName_ID);
        amountEdit=findViewById(R.id.bilManualAomunt_ID);
        dateEdit=findViewById(R.id.billManualDate_ID);
        tranjectionEdit=findViewById(R.id.billTManualTrans_ID);
        statusEdit=findViewById(R.id.billManualStatus_ID);
        personalTotal=findViewById(R.id.personalAmount_ID);
        personalDue=findViewById(R.id.personalDue_ID);

        submitBtn=findViewById(R.id.billManualSubmit_ID);
        personalBtn=findViewById(R.id.personalSubmit_ID);

        intent=getIntent();
        String id=intent.getStringExtra("editor_manualID");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=billerNameEdit.getText().toString().trim();
                String amount=amountEdit.getText().toString().trim();
                String date=dateEdit.getText().toString().trim();
                String transaction=tranjectionEdit.getText().toString().trim();
                String status=statusEdit.getText().toString();

                if (name.isEmpty()){
                    billerNameEdit.setError("আপনার নাম দিন");
                    billerNameEdit.requestFocus();
                    return;
                }else if (amount.isEmpty()){
                    amountEdit.setError("টাকার পরিমাণ লিখুন");
                    amountEdit.requestFocus();
                    return;
                }else if (date.isEmpty()){
                    dateEdit.setError("তারিখ লিখুন");
                    dateEdit.requestFocus();
                    return;
                } else {

                    sentData(name,amount,date,transaction,status,id);
                /*
                billerNameEdit.setText("");
                amountEdit.setText("");
                dateEdit.setText("");
                tranjectionEdit.setText("");

                 */
                }

            }
        });

        personalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String total=personalTotal.getText().toString();
                String due=personalDue.getText().toString();
                sentPersonalData(total,due,id);
            }
        });
    }



    private void sentData(String name, String amount, String date, String transaction, String status, String id) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Bill_folder");
        String new_id=reference.push().getKey();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("name",name);
        hashMap.put("amount",amount);
        hashMap.put("date",date);
        hashMap.put("transaction",transaction);
        hashMap.put("status",status);
        hashMap.put("id",id);
        hashMap.put("new_id",new_id);

        reference.child(new_id).setValue(hashMap);
        Toast.makeText(EditorManaulActivity.this, "আপনার বিল প্রেরন করা হয়েছে", Toast.LENGTH_SHORT).show();
    }

    private void sentPersonalData(String total, String due, String id) {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("personal_data");
        String new_Id=reference.push().getKey();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("total",total);
        hashMap.put("due",due);
        hashMap.put("new_id",new_Id);
        hashMap.put("id",id);

        reference.child(new_Id).setValue(hashMap);
        Toast.makeText(EditorManaulActivity.this, "আপনার তথ্য প্রেরন করা হয়েছে", Toast.LENGTH_SHORT).show();
    }
}