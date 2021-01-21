package com.example.gerejaonline.jadwalIbadah.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.gerejaonline.BookingKursiActivity;
import com.example.gerejaonline.R;
import com.example.gerejaonline.jadwalIbadah.model.ModelJadwalIbadah;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterJadwalIbadah extends BaseAdapter {
    Context c;
    ArrayList<ModelJadwalIbadah> jadwalIbadah;
    LayoutInflater inflater;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabase;

    public AdapterJadwalIbadah(Context c, ArrayList<ModelJadwalIbadah> jadwalIbadah){
        this.c=c;
        this.jadwalIbadah=jadwalIbadah;
    }

    @Override
    public int getCount() {
        return jadwalIbadah.size();
    }

    @Override
    public Object getItem(int position) {
        return jadwalIbadah.get(position);
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
            convertView = LayoutInflater.from(c).inflate(R.layout.model_list_jadwal_ibadah,parent,false);
        }
        mDatabase= FirebaseDatabase.getInstance().getReference();
        String id=jadwalIbadah.get(position).getId();
        final Holders holder=new Holders(convertView);
        holder.waktu.setText(jadwalIbadah.get(position).getTime());
        holder.tanggal.setText(jadwalIbadah.get(position).getDate());
        holder.pastor.setText(jadwalIbadah.get(position).getPastor());
        holder.background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(c, BookingKursiActivity.class);
                intent.putExtra("id",id);
                c.startActivity(intent);
            }
        });

        return convertView;
    }

    public class Holders{
        RelativeLayout background;
        TextView waktu,tanggal,pastor;


        public Holders(View v){
            background=(RelativeLayout) v.findViewById(R.id.go_booking);
            waktu=(TextView)v.findViewById(R.id.jam_ibadah);
            tanggal=(TextView)v.findViewById(R.id.tanggal_ibadah);
            pastor=(TextView)v.findViewById(R.id.pendeta);

        }
    }
}
