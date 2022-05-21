package com.example.ourproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ourproject.Adapter.PaymnetAdapter;
import com.example.ourproject.Model.Payment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    PaymnetAdapter paymnetAdapter;
    List<Payment> paymentList;

    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String myID=firebaseUser.getUid();

        recyclerView=findViewById(R.id.profileRecycler_ID);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        paymentList=new ArrayList<>();

        redData(myID);
    }

    private void redData(String myId) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Bill_folder");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Payment payment=snapshot.getValue(Payment.class);
                    if (payment.getId().equals(myId)){
                        paymentList.add(payment);
                    }

                    paymnetAdapter=new PaymnetAdapter(ProfileActivity.this,paymentList);
                    recyclerView.setAdapter(paymnetAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}