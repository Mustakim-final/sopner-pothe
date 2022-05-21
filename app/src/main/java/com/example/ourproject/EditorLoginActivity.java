package com.example.ourproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditorLoginActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_login);

        editText=findViewById(R.id.editorPass_ID);
        button=findViewById(R.id.editorSubmit_ID);

        intent=getIntent();
        int notice_ID=intent.getIntExtra("notice_ID",0);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=editText.getText().toString();

                sentData(password,notice_ID);

                editText.setText("");
            }
        });
    }

    private void sentData(String password,int notice_ID) {
        if (password.equals("dream123")){
            if (notice_ID==1){
                Intent intent=new Intent(EditorLoginActivity.this,EditorNoticeActivity.class);
                startActivity(intent);
            }else {
                Intent intent=new Intent(EditorLoginActivity.this,EditorHomeActivity.class);
                startActivity(intent);
            }

        }else {
            Toast.makeText(EditorLoginActivity.this, "Error password", Toast.LENGTH_SHORT).show();
        }
    }
}