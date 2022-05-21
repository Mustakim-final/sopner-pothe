package com.example.ourproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ourproject.Model.Chats;
import com.example.ourproject.Model.ProfileModel;
import com.example.ourproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyHolder>{
    Context context;
    List<Chats> chatsList;
    String imageUrl;

    public static final int MEG_RIGHT=0;
    public static final int MEG_LEFT=1;

    public MessageAdapter(Context context, List<Chats> chatsList, String imageUrl) {
        this.context = context;
        this.chatsList = chatsList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MEG_RIGHT){
            View view= LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            return new MyHolder(view);
        }else {
            View view=LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
            return new MyHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Chats chats=chatsList.get(position);
        
        holder.textView.setText(chats.getMessage());


        if (imageUrl.equals("default")){
            holder.circleImageView.setImageResource(R.drawable.ic_baseline_perm_identity_24);
        }else {
            Glide.with(context).load(imageUrl).into(holder.circleImageView);
        }


    }

    @Override
    public int getItemCount() {
        return chatsList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        CircleImageView circleImageView;
        TextView textView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView=itemView.findViewById(R.id.chat_image_ID);
            textView=itemView.findViewById(R.id.chat_text_ID);
        }
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if (chatsList.get(position).getSender().equals(firebaseUser.getUid())){
            return MEG_RIGHT;
        }else {
            return MEG_LEFT;
        }
    }
}
