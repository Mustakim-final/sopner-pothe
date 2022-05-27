package com.example.ourproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ourproject.Model.Post;
import com.example.ourproject.Model.ProfileModel;
import com.firebase.ui.database.FirebaseArray;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseUser firebaseUser;
    DatabaseReference reference;


    FirebaseAuth mAuth;

    TextView profileName;
    CircleImageView profileImage;


    Button choiceBtn,saveBtn;
    EditText postEdit;
    ImageView imageView;

    private Uri imageUri=null;

    private static final int GALLERY_CODE1=300;
    StorageReference storageReference;
    StorageTask storageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        profileName=findViewById(R.id.myName_ID);
        profileImage=findViewById(R.id.myProfile_ID);


        mAuth=FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("member").child(firebaseUser.getUid());


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ProfileModel profileModel=dataSnapshot.getValue(ProfileModel.class);
                profileName.setText(profileModel.getUsername());

                if (profileModel.getImageUrl().equals("default")){
                    profileImage.setImageResource(R.drawable.ic_baseline_perm_identity_24);
                }else {
                    Glide.with(getApplicationContext()).load(profileModel.getImageUrl()).into(profileImage);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        
        
        choiceBtn=findViewById(R.id.choceImage_ID);
        choiceBtn.setOnClickListener(this);
        saveBtn=findViewById(R.id.savePostBtn_ID);
        saveBtn.setOnClickListener(this);
        postEdit=findViewById(R.id.postText_ID);
        imageView=findViewById(R.id.imageView_ID);


    }



    @Override
    public void onClick(View view) {

        if (view.getId()==R.id.choceImage_ID){
            openGallary();
        }

        if (view.getId()==R.id.savePostBtn_ID){

            if (storageTask!=null && storageTask.isInProgress()){
                Toast.makeText(this, "ছবি আপলোড হচ্ছে", Toast.LENGTH_SHORT).show();
            }else {
                savePost();
            }
        }


    }







    private void openGallary() {
        Intent intent=new Intent();
        intent.setType(("image/*"));
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,GALLERY_CODE1);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE1 && resultCode == RESULT_OK &&data!=null && data.getData()!=null ) {
            imageUri = data.getData();
            Glide.with(getApplicationContext()).load(imageUri).into(imageView);

        }

    }

    public String getFileExtension(Uri imageUri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }


    private void savePost() {

        String post = postEdit.getText().toString();






        if (imageUri==null){


            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            String userID = firebaseUser.getUid();

            reference=FirebaseDatabase.getInstance().getReference("member").child(userID);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    ProfileModel profileModel=snapshot.getValue(ProfileModel.class);
                    String name=profileModel.getUsername();
                    String imageUrl=profileModel.getImageUrl();


                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();


                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("username",name);
                    hashMap.put("imageUrl",imageUrl);
                    hashMap.put("post", post);
                    hashMap.put("id", userID);
                    hashMap.put("imagePost","zero");
                    reference1.child("Dream Post").push().setValue(hashMap);

                    postEdit.setText("");


                    Toast.makeText(PostActivity.this, "Post done",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }else {

            storageReference = FirebaseStorage.getInstance().getReference("Post");

            StorageReference sreference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            sreference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();



                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            String userID = firebaseUser.getUid();

                            String imageUri = uri.toString();

                            reference=FirebaseDatabase.getInstance().getReference("member").child(userID);
                            reference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ProfileModel profileModel=snapshot.getValue(ProfileModel.class);
                                    String name=profileModel.getUsername();
                                    String imageUrl=profileModel.getImageUrl();


                                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();


                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("username",name);
                                    hashMap.put("imageUrl",imageUrl);
                                    hashMap.put("post", post);
                                    hashMap.put("id", userID);
                                    hashMap.put("imagePost", imageUri);

                                    reference1.child("Dream Post").push().setValue(hashMap);

                                    postEdit.setText("");
                                    imageView.setVisibility(View.INVISIBLE);

                                    Toast.makeText(PostActivity.this, "Post done", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });







                        }
                    });

                }
            });

        }




        /*
        String post = postEdit.getText().toString();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = firebaseUser.getUid();


        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("post", post);
        hashMap.put("id", userID);
        reference1.child("Dream Post").push().setValue(hashMap);


        Toast.makeText(PostActivity.this, "Post done",   Toast.LENGTH_SHORT).show();

         */


    }

}