package com.example.ourproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditorNoticeActivity extends AppCompatActivity {
    private EditText editText;
    private Button button;

    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_notice);
        editText=findViewById(R.id.editorNotice_ID);
        button=findViewById(R.id.editorNoticeSubBtn_ID);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String my_ID=firebaseUser.getUid();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }
}