package com.example.ourproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }
        super.onStart();
    }

    MaterialEditText emailEdit,passwordEdit;
    Button button;
    TextView textView,forgotPass;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEdit=findViewById(R.id.signEmail_ID);
        passwordEdit=findViewById(R.id.signPassword_ID);
        button=findViewById(R.id.signSubmit_ID);
        forgotPass=findViewById(R.id.forgotPass_ID);
        progressBar=findViewById(R.id.signProgressbar_ID);

        textView=findViewById(R.id.goSignUP_ID);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegActivity.class);
                startActivity(intent);
            }
        });

        mAuth=FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailEdit.getText().toString().trim();
                String password=passwordEdit.getText().toString();

                if (email.isEmpty()){
                    emailEdit.setError("Enter email !!!");
                    emailEdit.requestFocus();
                    return;
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailEdit.setError("Enter valid email !!!");
                    emailEdit.requestFocus();
                    return;
                }else if (password.isEmpty()){
                    passwordEdit.setError("Enter password !!!");
                    passwordEdit.requestFocus();
                    return;
                }else if (password.length()<6){
                    passwordEdit.setError("Enter 6 digit password!!!");
                    passwordEdit.requestFocus();
                    return;
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    signInUser(email,password);
                    //emailEdit.setText("");
                    //passwordEdit.setText("");
                }
            }
        });


        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText=new EditText(v.getContext());
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                builder.setTitle("Reset Password");
                builder.setMessage("আপনার মেইল দিন।মেইল এর মাধ্যমে পাসওয়ার্ড রিসেট করার জন্য লিঙ্ক দেওয়া হবে।");
                builder.setView(editText);

                builder.setPositiveButton("হ্যা", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email=editText.getText().toString();
                        if (email.isEmpty()){
                            editText.setError("Enter gmail");
                            editText.requestFocus();
                            return;
                        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                            editText.setError("Enter a valid gmail");
                            editText.requestFocus();
                            return;
                        }else {
//                            mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    Toast.makeText(MainActivity.this,"আপনার মেইল চেক করুন।",Toast.LENGTH_LONG).show();
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(MainActivity.this,"সঠিক মেইল দিন।",Toast.LENGTH_LONG).show();
//                                }
//                            });

                            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(MainActivity.this,"আপনার মেইল চেক করুন।",Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });

                builder.setNegativeButton("না", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                builder.create().show();

            }
        });
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}