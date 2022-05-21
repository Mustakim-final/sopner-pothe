package com.example.ourproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Objects;

public class RegActivity extends AppCompatActivity {
    MaterialEditText userNameEdit,emailEdit,passwordEdit,secretCodeEdit;
    Button button;
    TextView textView;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        userNameEdit=findViewById(R.id.regUsername_ID);
        emailEdit=findViewById(R.id.regEmail_ID);
        passwordEdit=findViewById(R.id.regPassword_ID);
        button=findViewById(R.id.regSubmit_ID);
        textView=findViewById(R.id.goSignIn_ID);
        progressBar=findViewById(R.id.regProgressbar_ID);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mAuth=FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=userNameEdit.getText().toString().trim();
                String email=emailEdit.getText().toString();
                String password=passwordEdit.getText().toString();

                if (username.isEmpty()){
                    userNameEdit.setError("Enter username!!!");
                    userNameEdit.requestFocus();
                    return;
                }else if (email.isEmpty()){
                    emailEdit.setError("Enter gmail");
                    emailEdit.requestFocus();
                    return;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailEdit.setError("Enter a valid gmail");
                    emailEdit.requestFocus();
                    return;
                }else if (password.isEmpty()){
                    passwordEdit.setError("Enter password!!!");
                    passwordEdit.requestFocus();
                    return;
                }else if (password.length()<6){
                    passwordEdit.setError("Enter 6 digit password!!!");
                    passwordEdit.requestFocus();
                    return;
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(username,email,password);
                }
            }
        });



    }

    private void registerUser(String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser=mAuth.getCurrentUser();
                    String userID=firebaseUser.getUid();

                    reference= FirebaseDatabase.getInstance().getReference("member").child(userID);

                    HashMap<String, Object> hashMap=new HashMap<>();
                    hashMap.put("id",userID);
                    hashMap.put("username",username);
                    hashMap.put("password",password);
                    hashMap.put("email",email);
                    hashMap.put("imageUrl","default");
                    hashMap.put("status","offline");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RegActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(RegActivity.this,HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                }else {
                    Toast.makeText(RegActivity.this, "Try Another email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}