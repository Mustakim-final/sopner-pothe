package com.example.ourproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.example.ourproject.Adapter.PostAdapter;
import com.example.ourproject.Model.Post;
import com.example.ourproject.Model.ProfileModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainProfileActivity extends AppCompatActivity {

    DatabaseReference reference;
    FirebaseUser firebaseUser;
    CircleImageView circleImageView,postcirculerImage;
    TextView textView;
    RecyclerView recyclerView;
    PostAdapter postAdapter;
    List<Post> postList;

    EditText postEdit;
    ImageButton imageButton;
    Button postMind;

    private Uri imageUri=null;
    private static final int IMAGE_REQUEST=1;
    private static final int CAMERA_CODE=200;
    private static final int GALLERY_CODE=200;
    private static final int GALLERY_CODE1=300;
    StorageReference storageReference;
    StorageTask storageTask;
    FirebaseStorage firebaseStorage;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);

        Permission();

        circleImageView=findViewById(R.id.myProfile_ID);
        textView=findViewById(R.id.myName_ID);


        postcirculerImage=findViewById(R.id.postProfile_ID);
        postMind=findViewById(R.id.postMind_ID);

        postMind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainProfileActivity.this,PostActivity.class);
                startActivity(intent);
            }
        });



        recyclerView=findViewById(R.id.post_recycler_ID);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("member").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfileModel profileModel=snapshot.getValue(ProfileModel.class);

                textView.setText(profileModel.getUsername());

                if (profileModel.getImageUrl().equals("default")){
                    circleImageView.setImageResource(R.drawable.ic_baseline_perm_identity_24);

                }else {
                    Glide.with(MainProfileActivity.this).load(profileModel.getImageUrl()).into(circleImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfileModel profileModel=snapshot.getValue(ProfileModel.class);

                if (profileModel.getImageUrl().equals("default")){
                    circleImageView.setImageResource(R.drawable.ic_baseline_perm_identity_24);

                }else {
                    Glide.with(MainProfileActivity.this).load(profileModel.getImageUrl()).into(postcirculerImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainProfileActivity.this,"Click",Toast.LENGTH_LONG).show();
                openImage();
            }
        });





        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);



        redPost();



    }


    private void redPost() {


        postList=new ArrayList<>();

        firebaseStorage=FirebaseStorage.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Dream Post");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Post post=dataSnapshot.getValue(Post.class);
                    post.setKey(dataSnapshot.getKey());

                    if (post.getId().equals(firebaseUser.getUid())){
                        postList.add(post);
                    }
                }

                postAdapter=new PostAdapter(MainProfileActivity.this,postList);
                recyclerView.setAdapter(postAdapter);

                postAdapter.setOnClickListener(new PostAdapter.onItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        //Toast.makeText(MainProfileActivity.this,"Position: "+position,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDelete(int position) {
                        Toast.makeText(MainProfileActivity.this,"Position: "+position,Toast.LENGTH_SHORT).show();

                        Post selectItem=postList.get(position);
                        String key=selectItem.getKey();


                        reference.child(key).removeValue();



                        StorageReference storageReference=firebaseStorage.getReferenceFromUrl(selectItem.getImagePost());
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                reference.child(key).removeValue();

                            }
                        });

                    }

                    @Override
                    public void onDownload(int position) {
                        Post selectItem=postList.get(position);

                        url=selectItem.getImagePost();
                        downloadImage(url,position);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void downloadImage(String url, int position) {
        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Downloading...");
        pd.setCancelable(false);
        pd.show();

        File file= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        PRDownloader.download(url, file.getPath(), URLUtil.guessFileName(url,null,null))
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        Long per=progress.currentBytes*100/progress.totalBytes;

                        pd.setMessage("Downloading: "+per+" %");

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        pd.dismiss();
                        Toast.makeText(MainProfileActivity.this,"Done",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(MainProfileActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void openImage() {
        String[] options={"Camera","Gallery"};
        AlertDialog.Builder builder=new AlertDialog.Builder(MainProfileActivity.this);
        builder.setTitle("Choose an options");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i==0){
                    openCamera();
                }
                if (i==1){
                    openGallery();
                }
            }

        });

        builder.create().show();
    }

    private void openCamera() {
        ContentValues contentValues=new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        contentValues.put(MediaStore.Images.Media.TITLE,"Temp Desc");
        imageUri=getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,CAMERA_CODE);
    }

    private void openGallery() {
        Intent intent=new Intent();
        intent.setType(("image/*"));
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent,GALLERY_CODE);
    }





    private void Permission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECORD_AUDIO
                ).withListener(new MultiplePermissionsListener() {
                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_CODE && resultCode==RESULT_OK){
            imageUri=data.getData();
            String filepath= "Images/"+"profile"+firebaseUser.getUid();
            StorageReference reference= FirebaseStorage.getInstance().getReference(filepath);
            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task=taskSnapshot.getMetadata().getReference().getDownloadUrl();

                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUri=uri.toString();
                            DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("member").child(firebaseUser.getUid());

                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("imageUrl",imageUri);
                            reference1.updateChildren(hashMap);
                        }
                    });
                }
            });
        }


        if (requestCode==CAMERA_CODE && resultCode==RESULT_OK){
            imageUri=data.getData();
            String filepath= "Images/"+"profile"+firebaseUser.getUid();
            StorageReference reference= FirebaseStorage.getInstance().getReference(filepath);
            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task=taskSnapshot.getMetadata().getReference().getDownloadUrl();

                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUri=uri.toString();
                            DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("member").child(firebaseUser.getUid());

                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("imageUrl",imageUri);
                            reference1.updateChildren(hashMap);


                        }
                    });
                }
            });
        }



    }

}