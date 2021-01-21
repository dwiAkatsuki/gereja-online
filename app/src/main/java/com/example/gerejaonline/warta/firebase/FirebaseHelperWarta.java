package com.example.gerejaonline.warta.firebase;

import android.content.Context;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gerejaonline.riwayat.adapter.AdapterRiwayat;
import com.example.gerejaonline.riwayat.model.ModelRiwayat;
import com.example.gerejaonline.warta.Adapter.AdapterWarta;
import com.example.gerejaonline.warta.model.ModelWarta;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class FirebaseHelperWarta {
    DatabaseReference db;
    String DB_URL;
    Context c;
    ListView listView;
    Boolean saved;
    ArrayList<ModelWarta> users = new ArrayList<>();
    AdapterWarta adapter;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    String mUserId;
    Date date1;
    Date date2;
    public FirebaseHelperWarta(Context c, String DB_URL, ListView listView){
        this.c=c;
        this.DB_URL=DB_URL;
        this.listView=listView;

        db = FirebaseDatabase.getInstance().getReferenceFromUrl(DB_URL);
    }

    public void refreshData(){
        db.child("warta").addChildEventListener(new ChildEventListener() {
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

        ModelWarta d = new ModelWarta();
        d.setId(dataSnapshot.getValue(ModelWarta.class).getId());
        d.setTanggal(dataSnapshot.getValue(ModelWarta.class).getTanggal());

        users.add(d);

        if (users.size()>0){
            Collections.sort(users, new Comparator<ModelWarta>() {
                @Override
                public int compare(ModelWarta o1, ModelWarta o2) {
                    if (o1.getTanggal() == null || o2.getTanggal() == null){
                        return 0;
                    }
                    try {
                        date1=toDate(o1.getTanggal());
                        date2=toDate(o2.getTanggal());
                    } catch (ParseException e){
                        e.printStackTrace();
                    }
                    return date1.compareTo(date2);
                }
            });
            Collections.reverse(users);
            adapter=new AdapterWarta(c, users);
            listView.setAdapter(adapter);
        }
    }
    public static Date toDate(String value) throws ParseException{
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        return format.parse(value);
    }
}
