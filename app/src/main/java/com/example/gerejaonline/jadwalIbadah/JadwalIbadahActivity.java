package com.example.gerejaonline.jadwalIbadah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.gerejaonline.BookingKursiActivity;
import com.example.gerejaonline.MainActivity;
import com.example.gerejaonline.R;
import com.example.gerejaonline.jadwalIbadah.firebase.FirebaseHelperJadwalIbadah;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JadwalIbadahActivity extends AppCompatActivity {
    final static String DB_URL="https://gereja-online.firebaseio.com/";
    ListView ListJadwal;
    FirebaseHelperJadwalIbadah helper;
    FloatingActionButton tambah;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    String mUserId;
    DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_ibadah);
        ListJadwal=(ListView)findViewById(R.id.LsViJadwal);
        helper=new FirebaseHelperJadwalIbadah(JadwalIbadahActivity.this,DB_URL,ListJadwal);
        helper.refreshData();
        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
        mUserId=mFirebaseUser.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        tambah=(FloatingActionButton)findViewById(R.id.add_jadwal);
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(JadwalIbadahActivity.this, AddJadwalActivity.class);
                startActivity(i);
            }
        });
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(mUserId).exists()){
                    tambah.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(mUserId).exists()){
                    tambah.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}