package com.example.ourproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ourproject.Adapter.PaymnetAdapter;
import com.example.ourproject.Adapter.PersonalAdapter;
import com.example.ourproject.Model.Notice;
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

public class PersonalActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    PersonalAdapter personalAdapter;
    List<Notice> noticeList;

    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String myID=firebaseUser.getUid();

        recyclerView=findViewById(R.id.personalRecycler_ID);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        noticeList=new ArrayList<>();

        redData(myID);
    }

    private void redData(String myId) {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("personal_data");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Notice notice=snapshot.getValue(Notice.class);


                    if (notice.getId().equals(myId)){
                        noticeList.add(notice);
                    }


                    personalAdapter=new PersonalAdapter(PersonalActivity.this,noticeList);
                    recyclerView.setAdapter(personalAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}