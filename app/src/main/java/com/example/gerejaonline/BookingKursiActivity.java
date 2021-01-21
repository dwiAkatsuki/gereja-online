package com.example.gerejaonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gerejaonline.jadwalIbadah.AddJadwalActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Date;

public class BookingKursiActivity extends AppCompatActivity {
    MaterialSpinner waktu_kebaktian, pesan_kursi;
    MaterialButton button;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabase;
    TextView jam,kursiTersedia;
    String mUserId;
    String id,userTask;
    String time,hari,pastor,quote;
    String role=" ", kursi=" ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_kursi);
        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
        mUserId=mFirebaseUser.getUid();
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        mDatabase= FirebaseDatabase.getInstance().getReference();
        jam=(TextView)findViewById(R.id.jam_booking);
        kursiTersedia=(TextView)findViewById(R.id.kursi_tersedia);
        button=(MaterialButton)findViewById(R.id.btn_booking);
        pesan_kursi=(MaterialSpinner)findViewById(R.id.booking_kursi);
        pesan_kursi.setItems("Pilih Jumlah","1","2","3","4","5");
        pesan_kursi.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (item.equals("Pilih Waktu")){
                    kursi=" ";
                }
                else if(item.equals("1")){
                    kursi="1";
                }
                else if(item.equals("2")){
                    kursi="2";
                }
                else if(item.equals("3")){
                    kursi="3";
                }
                else if(item.equals("4")){
                    kursi="4";
                }
                else if(item.equals("5")){
                    kursi="5";
                }

            }
        });
        Date date=new Date();
        int hours=date.getHours();
        int minutes=date.getMinutes();
        int seconds=date.getSeconds();
        int years=date.getYear();
        int month=date.getMonth();
        int day=date.getDay();
        String currentTime = String.valueOf(hours+minutes+seconds);
        String currentDate = String.valueOf(day+month+years);
        String dateTime = currentDate + currentTime;
        userTask=mUserId+dateTime;
        mDatabase.child("jadwal").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                time=""+ dataSnapshot.child("time").getValue();
                hari=""+ dataSnapshot.child("date").getValue();
                pastor=""+ dataSnapshot.child("pastor").getValue();
                quote=""+ dataSnapshot.child("quote").getValue();
                jam.setText(time);
                kursiTersedia.setText(quote+" Kursi");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!kursi.equals(" ")){
                            int kursis=Integer.parseInt(kursi);
                            int quotes=Integer.parseInt(quote);
                            int hasil=quotes-kursis;
                            String hasils=String.valueOf(hasil);
                            if (hasil<=0){
                                Toast.makeText(BookingKursiActivity.this, "Maaf. Kursi yang tersedia tidak mencukupi", Toast.LENGTH_LONG).show();
                            }else {
                                mDatabase.child("booking").child(userTask).child("id").setValue(userTask);
                                mDatabase.child("booking").child(userTask).child("idJadwal").setValue(id);
                                mDatabase.child("booking").child(userTask).child("idPemesan").setValue(mUserId);
                                mDatabase.child("booking").child(userTask).child("date").setValue(hari);
                                mDatabase.child("booking").child(userTask).child("kursi").setValue(kursi);
                                mDatabase.child("jadwal").child(id).child("quote").setValue(hasils);
                                onBackPressed();
                            }
                        }else {
                            Toast.makeText(BookingKursiActivity.this, "Pilih Jam Kursi", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}