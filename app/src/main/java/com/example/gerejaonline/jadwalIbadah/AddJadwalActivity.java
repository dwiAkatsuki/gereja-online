package com.example.gerejaonline.jadwalIbadah;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gerejaonline.LoginActivity;
import com.example.gerejaonline.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.blackbox_vision.datetimepickeredittext.view.DatePickerEditText;

public class AddJadwalActivity extends AppCompatActivity {
    MaterialSpinner jamIbadah;
    MaterialButton tambahJadwal;
    DatePickerEditText tanggal;
    EditText pengkotbah;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabase;
    String mUserId,mTanggal,mPengkotbah;
    String jam=" ";
    String userTask;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jadwal);
        tanggal=(DatePickerEditText) findViewById(R.id.add_tanggal);
        pengkotbah=(EditText)findViewById(R.id.add_pengkotbah);
        tambahJadwal=(MaterialButton)findViewById(R.id.button_add_jadwal);
        jamIbadah=(MaterialSpinner)findViewById(R.id.waktu_kebaktian);
        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
        mUserId=mFirebaseUser.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        tanggal.setManager(AddJadwalActivity.this.getSupportFragmentManager());
        jamIbadah.setItems("Pilih Waktu","Pagi 07.00","Pagi 09.00");
        jamIbadah.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (item.equals("Pilih Waktu")){
                    jam=" ";
                }
                else if(item.equals("Pagi 07.00")){
                    jam="Pagi 07.00";
                }
                else if(item.equals("Pagi 09.00")){
                    jam="Pagi 09.00";
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
        tambahJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog=new ProgressDialog(AddJadwalActivity.this);
                progressDialog.setMessage("Submit......");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mTanggal=tanggal.getText().toString();
                mPengkotbah=pengkotbah.getText().toString();
                if (!jam.equals(" ")){
                    if (!mTanggal.isEmpty()){
                        if (!mPengkotbah.isEmpty()){
                            mDatabase.child("jadwal").child(userTask).child("id").setValue(userTask);
                            mDatabase.child("jadwal").child(userTask).child("time").setValue(jam);
                            mDatabase.child("jadwal").child(userTask).child("date").setValue(mTanggal);
                            mDatabase.child("jadwal").child(userTask).child("pastor").setValue(mPengkotbah);
                            mDatabase.child("jadwal").child(userTask).child("quote").setValue("80");
                            onBackPressed();

                        }else {
                            Toast.makeText(AddJadwalActivity.this, "Tolong isi Pengkotbah", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(AddJadwalActivity.this, "Pilih Tanggal Ibadah", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(AddJadwalActivity.this, "Pilih Waktu Ibadah", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}