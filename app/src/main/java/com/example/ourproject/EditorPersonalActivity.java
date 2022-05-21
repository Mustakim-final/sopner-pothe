package com.example.ourproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.ourproject.Adapter.EditorPersonalAdapter;
import com.example.ourproject.Adapter.EditorPersonalEditAdapter;
import com.example.ourproject.Adapter.EditorProfileAdapter;
import com.example.ourproject.Model.EditorPayment;
import com.example.ourproject.Model.Notice;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditorPersonalActivity extends AppCompatActivity {

    SwipeMenuListView listView;
    EditorPersonalEditAdapter editorPersonalEditAdapter;
    List<Notice> noticeList;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_personal);

        intent=getIntent();
        String editorUser_ID=intent.getStringExtra("editor_personalID");

        noticeList=new ArrayList<>();

        editorPersonalEditAdapter=new EditorPersonalEditAdapter(this,noticeList);
        reference= FirebaseDatabase.getInstance().getReference("personal_data");

        listView=findViewById(R.id.editorPersonallistview_ID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noticeList.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Notice notice=snapshot.getValue(Notice.class);
                    if (notice.getId().equals(editorUser_ID)){
                        noticeList.add(notice);
                    }
                }

                listView.setAdapter(editorPersonalEditAdapter);
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
                Notice notice=noticeList.get(position);
                switch (index) {
                    case 0:
                        showUpdateDialuge(notice.getNew_id(),position);
                        break;
                    case 1:
                        reference=FirebaseDatabase.getInstance().getReference("personal_data").child(notice.getNew_id());
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
        View view= LayoutInflater.from(this).inflate(R.layout.editor_dialouge_item,null,false);
        builder.setView(view);

        intent=getIntent();
        String id=intent.getStringExtra("editor_personalID");

        AlertDialog alertDialog=builder.show();
        EditText total=view.findViewById(R.id.editorPersonalEditAmount_ID);
        EditText due=view.findViewById(R.id.editorPersonalEditDue_ID);



        Button button=view.findViewById(R.id.editorPersonalSubmitBtn_ID);
        Notice notice=noticeList.get(position);

        total.setText(notice.getTotal());
        due.setText(notice.getDue());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference=FirebaseDatabase.getInstance().getReference("personal_data").child(new_id);
                Notice notice1=new Notice(total.getText().toString(),due.getText().toString(),new_id,id);
                reference.setValue(notice1);

                Toast.makeText(EditorPersonalActivity.this, "data updated", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }
}