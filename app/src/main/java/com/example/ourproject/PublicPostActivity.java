package com.example.ourproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ourproject.Adapter.PostAdapter;
import com.example.ourproject.Adapter.PostPublicAdapter;
import com.example.ourproject.Model.Post;
import com.example.ourproject.Model.ProfileModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PublicPostActivity extends AppCompatActivity {


    DatabaseReference reference;
    FirebaseUser firebaseUser;
    CircleImageView postcirculerImage;
    TextView textView;

    RecyclerView recyclerView;
    PostPublicAdapter postPublicAdapter;
    List<Post> postList;

    StorageReference storageReference;
    StorageTask storageTask;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_post);

        textView=findViewById(R.id.post_myName_ID);
        postcirculerImage=findViewById(R.id.post_my_Profile_ID);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("member").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfileModel profileModel=snapshot.getValue(ProfileModel.class);

                textView.setText(profileModel.getUsername());

                if (profileModel.getImageUrl().equals("default")){
                    postcirculerImage.setImageResource(R.drawable.ic_baseline_perm_identity_24);

                }else {
                    Glide.with(PublicPostActivity.this).load(profileModel.getImageUrl()).into(postcirculerImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView=findViewById(R.id.post_public_recycler_ID);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        redPost();
    }

    private void redPost() {
        postList=new ArrayList<>();




        firebaseStorage= FirebaseStorage.getInstance();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Dream Post");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Post post=dataSnapshot.getValue(Post.class);
                    post.setKey(dataSnapshot.getKey());
                    postList.add(post);
                }

                postPublicAdapter=new PostPublicAdapter(PublicPostActivity.this,postList);
                recyclerView.setAdapter(postPublicAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}