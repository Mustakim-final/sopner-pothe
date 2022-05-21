package com.example.ourproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.ourproject.Adapter.EditorProfileAdapter;
import com.example.ourproject.Adapter.PaymnetAdapter;
import com.example.ourproject.Model.EditorPayment;
import com.example.ourproject.Model.Payment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditorPageActivity extends AppCompatActivity {

    SwipeMenuListView listView;
    EditorProfileAdapter editorProfileAdapter;
    List<EditorPayment> editorPaymentList;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_page);

       intent=getIntent();
       String editorUser_ID=intent.getStringExtra("editor_userID");

       editorPaymentList=new ArrayList<>();

       editorProfileAdapter=new EditorProfileAdapter(this,editorPaymentList);

       reference=FirebaseDatabase.getInstance().getReference("Bill_folder");

       listView=findViewById(R.id.listview_ID);

       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               editorPaymentList.clear();
               for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                   EditorPayment editorPayment=snapshot.getValue(EditorPayment.class);

                   if (editorUser_ID.equals(editorPayment.getId())){
                       editorPaymentList.add(editorPayment);
                   }
               }

               listView.setAdapter(editorProfileAdapter);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });


        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(200);
                // set item title
                openItem.setIcon(R.drawable.ic_baseline_edit_24);
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(200);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_baseline_delete_24);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };


// set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                EditorPayment editorPayment=editorPaymentList.get(position);
                switch (index) {
                    case 0:
                        showUpdateDialuge(editorPayment.getNew_id(),position);
                        break;
                    case 1:
                        reference=FirebaseDatabase.getInstance().getReference("Bill_folder").child(editorPayment.getNew_id());
                        reference.removeValue();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

    }

    private void showUpdateDialuge(String new_id, int position) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.dialouge_item,null,false);
        builder.setView(view);

        intent=getIntent();
        String id=intent.getStringExtra("editor_userID");

        AlertDialog alertDialog=builder.show();
        EditText name=view.findViewById(R.id.editorbillName_ID);
        EditText amount=view.findViewById(R.id.editorbilAomunt_ID);
        EditText date=view.findViewById(R.id.editorbillDate_ID);
        EditText status=view.findViewById(R.id.editorbillStatus_ID);

        Button button=view.findViewById(R.id.editorbillSubmit_ID);

        EditorPayment editorPayment=editorPaymentList.get(position);
        name.setText(editorPayment.getName());
        amount.setText(editorPayment.getAmount());
        date.setText(editorPayment.getDate());
        status.setText(editorPayment.getStatus());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference=FirebaseDatabase.getInstance().getReference("Bill_folder").child(new_id);
                EditorPayment editorPayment=new EditorPayment(new_id,name.getText().toString(),amount.getText().toString(),date.getText().toString(),status.getText().toString(),id);
                reference.setValue(editorPayment);

                Toast.makeText(EditorPageActivity.this, "data updated", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }


}