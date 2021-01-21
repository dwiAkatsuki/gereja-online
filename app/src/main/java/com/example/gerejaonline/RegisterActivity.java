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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText Nama,Username,Email,Password;
    MaterialButton daftar;
    Button login;
    String regNama,regUsername,regEmail,regPassword;
    String mUserId;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabase;
    FirebaseUser mFirebaseUser;
    ProgressDialog spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Nama=(EditText)findViewById(R.id.reg_nama);
        Username=(EditText)findViewById(R.id.reg_user_name);
        Email=(EditText)findViewById(R.id.reg_email);
        Password=(EditText)findViewById(R.id.reg_password);
        daftar=(MaterialButton)findViewById(R.id.daftar);
        login=(Button)findViewById(R.id.ke_login);
        spinner=new ProgressDialog(RegisterActivity.this);
        spinner.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regNama=Nama.getText().toString();
                regUsername=Username.getText().toString();
                regEmail=Email.getText().toString();
                regPassword=Password.getText().toString();
                if (!regNama.isEmpty()){
                    if (!regUsername.isEmpty()){
                        if (!regEmail.isEmpty()){
                            if (!regPassword.isEmpty()){
                                spinner.show();
                                mFirebaseAuth=FirebaseAuth.getInstance();
                                mFirebaseAuth.createUserWithEmailAndPassword(regEmail,regPassword)
                                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()){
                                                    mFirebaseAuth=FirebaseAuth.getInstance();
                                                    mFirebaseUser=mFirebaseAuth.getCurrentUser();
                                                    mUserId=mFirebaseUser.getUid();
                                                    mDatabase= FirebaseDatabase.getInstance().getReference();

                                                    mDatabase.child("users").child(mUserId).child("id").setValue(mUserId);
                                                    mDatabase.child("users").child(mUserId).child("name").setValue(regNama);
                                                    mDatabase.child("users").child(mUserId).child("email").setValue(regEmail);
                                                    mDatabase.child("users").child(mUserId).child("username").setValue(regUsername);
                                                    mDatabase.child("users").child(mUserId).child("password").setValue(regPassword);

                                                    mFirebaseAuth.signInWithEmailAndPassword(regEmail,regPassword)
                                                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                    if (task.isSuccessful()){
                                                                        spinner.dismiss();
                                                                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                                                        RegisterActivity.this.finish();
                                                                    }else {
                                                                        Toast.makeText(RegisterActivity.this, "Login Gagal", Toast.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            });

                                                }else {
                                                    Toast.makeText(RegisterActivity.this, "register gagal", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }else {
                                Toast.makeText(RegisterActivity.this, "Tolong isi Password", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(RegisterActivity.this, "Tolong isi Email", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(RegisterActivity.this, "Tolong isi Username", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(RegisterActivity.this, "Tolong isi Nama", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}