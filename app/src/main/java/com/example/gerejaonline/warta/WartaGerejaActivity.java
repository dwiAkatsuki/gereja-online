package com.example.gerejaonline.warta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.gerejaonline.R;
import com.example.gerejaonline.jadwalIbadah.AddJadwalActivity;
import com.example.gerejaonline.jadwalIbadah.JadwalIbadahActivity;
import com.example.gerejaonline.riwayat.RiwayatActivity;
import com.example.gerejaonline.riwayat.firebase.FirebaseHelperRiwayat;
import com.example.gerejaonline.warta.firebase.FirebaseHelperWarta;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WartaGerejaActivity extends AppCompatActivity {
    FloatingActionButton addWarta;
    ListView ListWarta;
    final static String DB_URL="https://gereja-online.firebaseio.com/";
    FirebaseHelperWarta helper;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabase;
    String mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warta_gereja);
        ListWarta=(ListView)findViewById(R.id.LsViWarta);
        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mUserId=mFirebaseUser.getUid();
        addWarta=(FloatingActionButton)findViewById(R.id.add_warta);
        helper=new FirebaseHelperWarta(WartaGerejaActivity.this,DB_URL,ListWarta);
        helper.refreshData();
        addWarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WartaGerejaActivity.this, AddWartaActivity.class);
                startActivity(i);
            }
        });
    }
}