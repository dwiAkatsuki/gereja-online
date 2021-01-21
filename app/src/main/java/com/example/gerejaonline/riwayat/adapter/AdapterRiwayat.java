package com.example.gerejaonline.riwayat.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.example.gerejaonline.R;
import com.example.gerejaonline.riwayat.model.ModelRiwayat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterRiwayat extends BaseAdapter {
    Context c;
    ArrayList<ModelRiwayat> riwayatbooking;
    LayoutInflater inflater;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabase;

    public AdapterRiwayat(Context c, ArrayList<ModelRiwayat> riwayatbooking){
        this.c=c;
        this.riwayatbooking=riwayatbooking;
    }

    @Override
    public int getCount() {
        return riwayatbooking.size();
    }

    @Override
    public Object getItem(int position) {
        return riwayatbooking.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater==null){
            inflater=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView==null){
            convertView = LayoutInflater.from(c).inflate(R.layout.model_list_riwayat,parent,false);
        }
        mDatabase= FirebaseDatabase.getInstance().getReference();
        String id=riwayatbooking.get(position).getId();
        String idPemesan=riwayatbooking.get(position).getIdPemesan();
        String idJadwal=riwayatbooking.get(position).getIdJadwal();
        Log.e("idjadwal",idJadwal);
        final Holders holder=new Holders(convertView);
        holder.tanggal.setText(riwayatbooking.get(position).getDate());
        holder.jumlah.setText(riwayatbooking.get(position).getKursi());
        mDatabase.child("jadwal").child(idJadwal).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.waktu.setText(""+dataSnapshot.child("time").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(idPemesan).exists()){
                    holder.nama.setText(""+dataSnapshot.child(idPemesan).child("name").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(idPemesan).exists()){
                    holder.nama.setText(""+dataSnapshot.child(idPemesan).child("name").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return convertView;
    }

    public class Holders{
        TextView waktu,tanggal,nama,jumlah;

        public Holders(View v){
            waktu=(TextView)v.findViewById(R.id.jam_Riwayat);
            tanggal=(TextView)v.findViewById(R.id.tanggal_riwayat);
            nama=(TextView)v.findViewById(R.id.nama_riwayat);
            jumlah=(TextView)v.findViewById(R.id.riwayat_jumah_kursi);

        }
    }
}
