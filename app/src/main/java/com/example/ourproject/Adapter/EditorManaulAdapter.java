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
import com.example.ourproject.EditorManaulActivity;
import com.example.ourproject.EditorPageActivity;
import com.example.ourproject.Model.ProfileModel;
import com.example.ourproject.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditorManaulAdapter extends RecyclerView.Adapter<EditorManaulAdapter.MyViewHolder>{
    Context context;
    List<ProfileModel> profileModelList;
    String editorUserID;

    public EditorManaulAdapter(Context context, List<ProfileModel> profileModelList) {
        this.context = context;
        this.profileModelList = profileModelList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.editor_user_item,parent,false);
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

        editorUserID=profileModel.getId();
    }

    @Override
    public int getItemCount() {
        return profileModelList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView circleImageView;
        TextView profileName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.editorUserProfileListImg_ID);

            profileName = itemView.findViewById(R.id.editorUserProfileName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            ProfileModel profileModel=profileModelList.get(getAdapterPosition());

            editorUserID=profileModel.getId();

            Intent intent=new Intent(context, EditorManaulActivity.class);
            intent.putExtra("editor_manualID",editorUserID);
            context.startActivity(intent);
        }
    }
}
