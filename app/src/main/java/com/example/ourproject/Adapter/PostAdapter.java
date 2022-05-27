package com.example.ourproject.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ourproject.Model.Post;
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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.myHolder>{
    private Context context;
    List<Post> postList;
    List<ProfileModel> profileModelList;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    private onItemClickListener listener;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.post_item,parent,false);
        return new myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        Post post=postList.get(position);

        Glide.with(context).load(post.getImageUrl()).into(holder.circleImageView);

        holder.profileName.setText(post.getUsername());

        if (post.getPost().equals("")){
            holder.textView.setVisibility(View.GONE);
        }else {
            holder.textView.setText(post.getPost());
        }

        if (post.getImagePost().equals("zero")){
            holder.imageView.setVisibility(View.GONE);
        }else {
            Glide.with(context).load(post.getImagePost()).into(holder.imageView);
        }



    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class myHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener, ViewTreeObserver.OnTouchModeChangeListener {
        ImageView imageView;
        CircleImageView circleImageView;
        TextView textView,profileName;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.postProfileImg_ID);
            profileName=itemView.findViewById(R.id.postProfileName_ID);

            imageView=itemView.findViewById(R.id.post_Photo);
            textView=itemView.findViewById(R.id.post_text_ID);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {

            if (listener!=null){
                int position=getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    listener.onItemClick(position);
                }
            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Options");
            MenuItem delete=contextMenu.add(Menu.NONE,1,1,"Delete");
            MenuItem download=contextMenu.add(Menu.NONE,2,2,"Download");

            delete.setOnMenuItemClickListener(this);
            download.setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            if (listener!=null){
                int position=getAdapterPosition();
                if (position!=RecyclerView.NO_POSITION){
                    switch (menuItem.getItemId()){
                        case 1:
                            listener.onDelete(position);
                            return true;
                        case 2:
                            listener.onDownload(position);
                            return true;
                    }
                }
            }
            return false;
        }


        @Override
        public void onTouchModeChanged(boolean b) {

        }
    }

    public interface onItemClickListener{
        void onItemClick(int position);
        void onDelete(int position);
        void onDownload(int position);
    }

    public void setOnClickListener(onItemClickListener listener){
        this.listener=listener;
    }
}
