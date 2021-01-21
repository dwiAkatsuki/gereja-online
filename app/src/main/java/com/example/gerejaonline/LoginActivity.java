package com.example.gerejaonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    MaterialButton login;
    Button daftar;
    FirebaseAuth mFirebaseAuth;
    String loginEmail, loginPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        login=(MaterialButton)findViewById(R.id.masuk);
        daftar=(Button) findViewById(R.id.ke_daftar);
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Login......");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loginEmail=email.getText().toString();
                loginPass=password.getText().toString();
                if (!loginEmail.isEmpty()){
                    if (!loginPass.isEmpty()){
                        progressDialog.show();
                        mFirebaseAuth=FirebaseAuth.getInstance();
                        mFirebaseAuth.signInWithEmailAndPassword(loginEmail,loginPass)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            progressDialog.dismiss();
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            LoginActivity.this.finish();
                                        }else {
                                            progressDialog.dismiss();
                                            Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }else {
                        Toast.makeText(LoginActivity.this, "Tolong isi Password", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Tolong isi Email", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}