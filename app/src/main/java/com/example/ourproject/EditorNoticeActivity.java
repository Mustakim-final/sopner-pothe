package com.example.ourproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ourproject.ALL_Notification.A_Data;
import com.example.ourproject.ALL_Notification.A_Notification;
import com.example.ourproject.ALL_Notification.A_Sender;
import com.example.ourproject.Model.ProfileModel;
import com.example.ourproject.NotificationCreate.ApiService;
import com.example.ourproject.NotificationCreate.Client;
import com.example.ourproject.NotificationCreate.Data;
import com.example.ourproject.NotificationCreate.MyResponse;
import com.example.ourproject.NotificationCreate.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorNoticeActivity extends AppCompatActivity {
    private EditText editText;
    private Button button;

    FirebaseUser firebaseUser;
    ApiService apiService;
    boolean notify=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_notice);

        apiService= Client.getCLient("https://fcm.googleapis.com/").create(ApiService.class);

        editText=findViewById(R.id.editorNotice_ID);
        button=findViewById(R.id.editorNoticeSubBtn_ID);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String my_ID=firebaseUser.getUid();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify=true;
                String notice=editText.getText().toString().trim();
                if (notice.isEmpty()){
                    editText.setError("Enter notice !!");
                    editText.requestFocus();
                    return;
                }else {
                    sendData(notice,my_ID);
                    editText.setText("");
                }

            }
        });


    }

    private void sendData(String notice, String my_id) {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Notice");
        String new_ID=reference.push().getKey();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("notice",notice);
        //hashMap.put("new_id",new_ID);
        hashMap.put("id",my_id);

        reference.child(my_id).setValue(hashMap);

        Toast.makeText(EditorNoticeActivity.this,"আপনার নোটিস প্রেরন করা হয়েছে",Toast.LENGTH_SHORT).show();


        final String msg=notice;
        sendNotitfication(msg);
        notify=false;
    }

    private void sendNotitfication(String msg) {
        A_Data data=new A_Data("All Member",R.mipmap.ic_launcher,msg,"Notice","Admin");
        A_Notification notification=new A_Notification("Notice",msg,R.mipmap.ic_launcher);
        A_Sender sender=new A_Sender(data,notification);

    }
}