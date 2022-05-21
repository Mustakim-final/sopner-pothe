package com.example.ourproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ourproject.MessageActivity;
import com.example.ourproject.Model.Chats;
import com.example.ourproject.Model.ProfileModel;
import com.example.ourproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    Context context;
    List<ProfileModel> profileModelList;
    boolean isChat;

    String lastMessage;
    String userID;


    FirebaseUser firebaseUser;
    public UserAdapter(Context context, List<ProfileModel> profileModelList,boolean isChat) {
        this.context = context;
        this.profileModelList = profileModelList;
        this.isChat=isChat;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.user_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProfileModel profileModel=profileModelList.get(position);
        holder.profileName.setText(profileModel.getUsername());

        if (profileModel.getImageUrl().equals("default")){
            holder.circleImageView.setImageResource(R.drawable.ic_baseline_perm_identity_24);
        }else {
            Glide.with(context).load(profileModel.getImageUrl()).into(holder.circleImageView);
        }

        userID=profileModel.getId();

        if (isChat){
            if (profileModel.getStatus().equals("online")){
                holder.img_online.setVisibility(View.VISIBLE);
                holder.img_offline.setVisibility(View.GONE);
            }else {
                holder.img_online.setVisibility(View.GONE);
                holder.img_offline.setVisibility(View.VISIBLE);
            }
        }else {
            holder.img_online.setVisibility(View.GONE);
            holder.img_offline.setVisibility(View.GONE);
        }

        if (isChat){
            LastMessage(profileModel.getId(),holder.lastMessage);
        }else {
            holder.lastMessage.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return profileModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CircleImageView circleImageView,img_online,img_offline;
        TextView profileName,lastMessage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView=itemView.findViewById(R.id.userProfileListImg_ID);
            img_online=itemView.findViewById(R.id.mesonline_ID);
            img_offline=itemView.findViewById(R.id.mesoffline_ID);

            profileName=itemView.findViewById(R.id.userProfileList_ID);
            lastMessage=itemView.findViewById(R.id.lastMessage_ID);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ProfileModel profileModel=profileModelList.get(getAdapterPosition());

            userID=profileModel.getId();

            Intent intent=new Intent(context, MessageActivity.class);
            intent.putExtra("userID",userID);
            context.startActivity(intent);
        }
    }

    public void LastMessage(String userID,TextView lastMessageText){
        lastMessage="default";
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("message");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chats chats=snapshot.getValue(Chats.class);

                    if (firebaseUser!=null && chats!=null){
                        if (chats.getSender().equals(userID) && chats.getReceiver().equals(firebaseUser.getUid()) || chats.getSender().equals(firebaseUser.getUid()) && chats.getReceiver().equals(userID)){
                            lastMessage=chats.getMessage();
                        }
                    }
                }

                switch (lastMessage){
                    case "default":
                        lastMessageText.setText("No message");
                        break;
                    default:
                        lastMessageText.setText(lastMessage);
                }

                lastMessage="default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
