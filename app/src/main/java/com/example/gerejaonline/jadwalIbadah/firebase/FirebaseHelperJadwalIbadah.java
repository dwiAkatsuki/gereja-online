package com.example.gerejaonline.jadwalIbadah.firebase;

import android.content.Context;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.versionedparcelable.VersionedParcel;

import com.example.gerejaonline.jadwalIbadah.adapter.AdapterJadwalIbadah;
import com.example.gerejaonline.jadwalIbadah.model.ModelJadwalIbadah;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class FirebaseHelperJadwalIbadah {
    DatabaseReference db;
    String DB_URL;
    Context c;
    ListView listView;
    Boolean saved;
    ArrayList<ModelJadwalIbadah> users = new ArrayList<>();
    AdapterJadwalIbadah adapter;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    String mUserId;
    Date date1;
    Date date2;

    public FirebaseHelperJadwalIbadah(Context c, String DB_URL, ListView listView){
        this.c=c;
        this.DB_URL=DB_URL;
        this.listView=listView;

        db = FirebaseDatabase.getInstance().getReferenceFromUrl(DB_URL);
    }

    public void refreshData(){
        db.child("jadwal").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                getUpdate(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                users.clear();
                getUpdate(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getUpdate(DataSnapshot dataSnapshot){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUserId = mFirebaseUser.getUid();

        ModelJadwalIbadah d = new ModelJadwalIbadah();
        d.setId(dataSnapshot.getValue(ModelJadwalIbadah.class).getId());
        d.setTime(dataSnapshot.getValue(ModelJadwalIbadah.class).getTime());
        d.setDate(dataSnapshot.getValue(ModelJadwalIbadah.class).getDate());
        d.setPastor(dataSnapshot.getValue(ModelJadwalIbadah.class).getPastor());
        d.setQuote(dataSnapshot.getValue(ModelJadwalIbadah.class).getQuote());
        users.add(d);

        if (users.size()>0){
            Collections.sort(users, new Comparator<ModelJadwalIbadah>() {
                @Override
                public int compare(ModelJadwalIbadah o1, ModelJadwalIbadah o2) {
                    if (o1.getDate() == null || o2.getDate() == null){
                        return 0;
                    }
                    try {
                        date1=toDate(o1.getDate());
                        date2=toDate(o2.getDate());
                    } catch (ParseException e){
                        e.printStackTrace();
                    }
                    return date1.compareTo(date2);
                }
            });
            Collections.reverse(users);
            adapter=new AdapterJadwalIbadah(c, users);
            listView.setAdapter(adapter);
        }
    }
    public static Date toDate(String value) throws ParseException{
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        return format.parse(value);
    }

}
