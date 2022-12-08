package com.example.ourproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ourproject.Adapter.MessageAdapter;
import com.example.ourproject.Adapter.UserAdapter;
import com.example.ourproject.Model.Chats;
import com.example.ourproject.Model.ProfileModel;
import com.example.ourproject.NotificationCreate.ApiService;
import com.example.ourproject.NotificationCreate.Client;
import com.example.ourproject.NotificationCreate.Data;
import com.example.ourproject.NotificationCreate.MyResponse;
import com.example.ourproject.NotificationCreate.Notification;
import com.example.ourproject.NotificationCreate.Sender;
import com.example.ourproject.NotificationCreate.Token;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    TextView userPrfileName;
    CircleImageView profileImage;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;

    EditText mesEditText;
    ImageButton sentImgBtn;

    List<Chats> chatsList;
    MessageAdapter messageAdapter;
    RecyclerView recyclerView;

    ApiService apiService;
    boolean notify=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        apiService= Client.getCLient("https://fcm.googleapis.com/").create(ApiService.class);

        recyclerView=findViewById(R.id.recyclerMessage_ID);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);


        mesEditText=findViewById(R.id.sentMesEditText_ID);
        sentImgBtn=findViewById(R.id.mesSentBtn_ID);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String myID=firebaseUser.getUid();

        userPrfileName=findViewById(R.id.messsageProfileName_ID);
        profileImage=findViewById(R.id.profileImage_ID);

        intent=getIntent();
        String userID=intent.getStringExtra("userID");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("member").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfileModel profileModel=dataSnapshot.getValue(ProfileModel.class);
                userPrfileName.setText(profileModel.getUsername());

                if (profileModel.getImageUrl().equals("default")){
                    profileImage.setImageResource(R.drawable.ic_baseline_perm_identity_24);
                }else {
                    Glide.with(MessageActivity.this).load(profileModel.getImageUrl()).into(profileImage);
                }

                redMessage(myID,userID,profileModel.getImageUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sentImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify=true;
                String message=mesEditText.getText().toString().trim();
                if (!message.equals("")){
                    sentMessage(myID,userID,message);

                }else {
                    Toast.makeText(MessageActivity.this, "বার্তা লিখুন !!!", Toast.LENGTH_SHORT).show();
                }

                mesEditText.setText("");

            }
        });
    }


    private void sentMessage(String myID, String userID, String message) {

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap=new HashMap<>();
        hashMap.put("sender",myID);
        hashMap.put("receiver",userID);
        hashMap.put("message",message);

        reference.child("message").push().setValue(hashMap);

        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("message_list").child(myID).child(userID);
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    reference1.child("id").setValue(userID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isComplete()){
                            FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                            String refreshToken=task.getResult().getToken();
                            if (firebaseUser!=null){
                                updateToken(refreshToken);
                            }


                        }
                    }
                });

        final String msg=message;
        reference=FirebaseDatabase.getInstance().getReference("member").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfileModel users=snapshot.getValue(ProfileModel.class);

                if (notify){
                    sendNotitfication(userID,users.getUsername(), msg);

                }
                notify=false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void updateToken(String refreshToken) {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Dream Token");
        Token token=new Token(refreshToken);
        databaseReference.child(firebaseUser.getUid()).setValue(token);
    }

    private void sendNotitfication(String userID, String username, String msg) {

        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Dream Token");

        Query query=tokens.orderByKey().equalTo(userID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Token token=dataSnapshot.getValue(Token.class);
                    Data data=new Data(firebaseUser.getUid(),R.mipmap.ic_launcher,username+":"+msg,"New Message",userID);
                    Notification notification=new Notification(username,msg,R.mipmap.ic_launcher);

                    Sender sender=new Sender(token.getToken(),data,notification);
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code()==200){
                                        if (response.body().success!=1){
                                            Toast.makeText(MessageActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void redMessage(String myID, String userID, String imageUrl) {
        chatsList=new ArrayList<>();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("message");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatsList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chats chats=snapshot.getValue(Chats.class);
                    if (chats.getSender().equals(myID) && chats.getReceiver().equals(userID) || chats.getReceiver().equals(myID) && chats.getSender().equals(userID)){
                        chatsList.add(chats);
                    }

                    messageAdapter=new MessageAdapter(MessageActivity.this,chatsList,imageUrl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Status(String status){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("member").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Status("offline");
    }
}