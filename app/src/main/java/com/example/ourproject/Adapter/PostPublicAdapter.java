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
import com.example.ourproject.MainProfileActivity;
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

public class PostPublicAdapter extends RecyclerView.Adapter<PostPublicAdapter.MyHolder> {
    Context context;
    List<Post> postList;
    List<ProfileModel> profileModelList;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    public PostPublicAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.public_post_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Post post=postList.get(position);

        Glide.with(context).load(post.getImageUrl()).into(holder.circleImageView);

        holder.nameText.setText(post.getUsername());

        if (post.getPost().equals("")){
            holder.textView.setVisibility(View.GONE);
        }else {
            holder.textView.setText(post.getPost());
        }


        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String userId=firebaseUser.getUid();



        //holder.getlikeBtnStatus(userId,imageID);

        /*
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("member").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfileModel profileModel1=snapshot.getValue(ProfileModel.class);



                if (profileModel1.getId().equals(post.getId())){
                    holder.nameText.setText(profileModel1.getUsername());
                    Glide.with(context).load(profileModel1.getImageUrl()).into(holder.circleImageView);
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */




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

    public class MyHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        ImageView imageView,likeImageBtn,likeImageBtnF;
        TextView textView,nameText,likeCountText;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.post_my_Profile_ID);
            nameText=itemView.findViewById(R.id.post_myName_ID);
            imageView=itemView.findViewById(R.id.post_public_Photo);
            textView=itemView.findViewById(R.id.post_public_text_ID);

            likeImageBtn=itemView.findViewById(R.id.likeImageBtn_ID);
            likeCountText=itemView.findViewById(R.id.likeCountText_ID);
        }

        public void getlikeBtnStatus(String userId, String imageID) {
            reference=FirebaseDatabase.getInstance().getReference("Dream Post");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(imageID).hasChild(userId)){
                        int likeCount= (int) snapshot.child(imageID).getChildrenCount();
                        likeCountText.setText(likeCount+"likes");
                        likeImageBtn.setImageResource(R.drawable.ic_baseline_favorite_24);
                    }else {
                        int likeCount= (int) snapshot.child(imageID).getChildrenCount();
                        likeCountText.setText(likeCount+"likes");
                        likeImageBtn.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
