package com.example.ourproject.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ourproject.Adapter.EditorManaulAdapter;
import com.example.ourproject.Adapter.EditorUserAdapter;
import com.example.ourproject.Model.ProfileModel;
import com.example.ourproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class EditorExtraFragment extends Fragment {

    RecyclerView recyclerView;
    EditorManaulAdapter editorManaulAdapter;
    List<ProfileModel> profileModelList;
    FirebaseUser firebaseUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_editor_extra, container, false);

        recyclerView=view.findViewById(R.id.editorManualRecyclerView_ID);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        profileModelList=new ArrayList<>();

        redUser();

        return view;
    }

    private void redUser() {
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("member");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profileModelList.clear();

                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ProfileModel profileModel=snapshot.getValue(ProfileModel.class);

                    if (!profileModel.getId().equals(firebaseUser.getUid())){
                        profileModelList.add(profileModel);
                    }
                }

                editorManaulAdapter=new EditorManaulAdapter(getContext(),profileModelList);
                recyclerView.setAdapter(editorManaulAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}