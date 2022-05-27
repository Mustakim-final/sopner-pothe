package com.example.ourproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ourproject.Adapter.NoticeAdapter;
import com.example.ourproject.Adapter.SliderAdapter;
import com.example.ourproject.Model.EditorNotice;
import com.example.ourproject.Model.Notice;
import com.example.ourproject.Model.ProfileModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    SliderView sliderView;
    int[] images={R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4,R.drawable.img5};

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;

    CardView memberCard,postCard,payCard;

    RecyclerView recyclerView;
    NoticeAdapter noticeAdapter;
    List<EditorNotice> editorNoticeList;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    TextView profileName,noticeText;
    CircleImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        toolbar=findViewById(R.id.toolBar_ID);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileName=findViewById(R.id.profile_ID);
        profileImage=findViewById(R.id.profileImage_ID);


        mAuth=FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("member").child(firebaseUser.getUid());
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


        drawerLayout=findViewById(R.id.drawer_ID);

        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView=findViewById(R.id.navigation_ID);
        navigationView.setNavigationItemSelectedListener(this);


        sliderView=findViewById(R.id.sliderView_ID);

        SliderAdapter sliderAdapter=new SliderAdapter(images);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

        memberCard=findViewById(R.id.memberCard_ID);
        postCard=findViewById(R.id.postCard_ID);
        payCard=findViewById(R.id.billCard_ID);
        profileName=findViewById(R.id.profile_ID);



        recyclerView=findViewById(R.id.noticeRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        editorNoticeList=new ArrayList<>();
        redNotice();

        memberCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,MemberActivity.class);
                startActivity(intent);
            }
        });

        postCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,PublicPostActivity.class);
                startActivity(intent);
            }
        });

        payCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,BillPayActivity.class);
                startActivity(intent);
            }
        });





    }

    private void redNotice() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Notice");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    EditorNotice editorNotice=snapshot.getValue(EditorNotice.class);
                    editorNoticeList.add(editorNotice);

                }

                noticeAdapter=new NoticeAdapter(HomeActivity.this,editorNoticeList);
                recyclerView.setAdapter(noticeAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logout_ID){
            mAuth=FirebaseAuth.getInstance();
            mAuth.signOut();
            finish();
        }else if (item.getItemId()==R.id.profile_ID){
            Intent intent=new Intent(HomeActivity.this,ProfileActivity.class);
            startActivity(intent);
        }else if (item.getItemId()==R.id.editor_ID){
            Intent intent=new Intent(HomeActivity.this,EditorLoginActivity.class);
            startActivity(intent);
        }else if (item.getItemId()==R.id.dueAndAmount_ID){
            Intent intent=new Intent(HomeActivity.this,PersonalActivity.class);
            startActivity(intent);
        }else if (item.getItemId()==R.id.notice_ID){
            Intent intent=new Intent(HomeActivity.this,EditorLoginActivity.class);
            intent.putExtra("notice_ID",1);
            startActivity(intent);
        }else if (item.getItemId()==R.id.editProfile_ID){
            Intent intent=new Intent(HomeActivity.this,MainProfileActivity.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}