package com.example.gerejaonline.riwayat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gerejaonline.BookingKursiActivity;
import com.example.gerejaonline.R;
import com.example.gerejaonline.jadwalIbadah.JadwalIbadahActivity;
import com.example.gerejaonline.jadwalIbadah.firebase.FirebaseHelperJadwalIbadah;
import com.example.gerejaonline.riwayat.firebase.FirebaseHelperRiwayat;
import com.example.gerejaonline.riwayat.firebase.FirebaseHelperRiwayat2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RiwayatActivity extends AppCompatActivity {
    ListView ListRiwayat;
    final static String DB_URL="https://gereja-online.firebaseio.com/";
    FirebaseHelperRiwayat helper;
    FirebaseHelperRiwayat2 helper2;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabase;
    String mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        ListRiwayat=(ListView)findViewById(R.id.LsViRiwayat);
        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mUserId=mFirebaseUser.getUid();
        mDatabase.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(mUserId).exists()){
                    helper=new FirebaseHelperRiwayat(RiwayatActivity.this,DB_URL,ListRiwayat);
                    helper.refreshData();
                }else {
                    helper2=new FirebaseHelperRiwayat2(RiwayatActivity.this,DB_URL,ListRiwayat);
                    helper2.refreshData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}