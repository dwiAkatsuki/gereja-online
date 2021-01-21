package com.example.gerejaonline.warta.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.gerejaonline.R;
import com.example.gerejaonline.jadwalIbadah.model.ModelJadwalIbadah;
import com.example.gerejaonline.riwayat.adapter.AdapterRiwayat;
import com.example.gerejaonline.riwayat.model.ModelRiwayat;
import com.example.gerejaonline.warta.WartaGerejaActivity;
import com.example.gerejaonline.warta.model.ModelWarta;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AdapterWarta extends BaseAdapter {
    Context c;
    ArrayList<ModelWarta> wartaGereja;
    LayoutInflater inflater;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabase;
    StorageReference mStorage;
    ProgressDialog progressDialog;
    FirebaseStorage firebaseStorage;

    public AdapterWarta(Context c, ArrayList<ModelWarta> wartaGereja){
        this.c=c;
        this.wartaGereja=wartaGereja;
        mDatabase= FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(c);

    }

    @Override
    public int getCount() {
        return wartaGereja.size();
    }

    @Override
    public Object getItem(int position) {
        return wartaGereja.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = LayoutInflater.from(c).inflate(R.layout.model_list_warta, parent, false);
        }
        String id=wartaGereja.get(position).getId();
        Log.d("id",id);
        String tanggal=wartaGereja.get(position).getTanggal();
        Holders holder=new Holders(convertView);
        holder.tanggal.setText(wartaGereja.get(position).getTanggal());
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(id);
            }
        });
        return convertView;
    }
    public void downloadFile(String id){
        String fileName = id+".pdf";
        Log.d("fileName",fileName);
        mStorage=FirebaseStorage.getInstance().getReference();
        StorageReference filepath = mStorage.child("warta").child(fileName);
        File rootPath = new File(Environment.getExternalStorageDirectory(), "Gereja");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath,fileName);

        filepath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase ",";local tem file created  created " +localFile.toString());
                Toast.makeText(c,"downloaded to"+localFile.toString(),Toast.LENGTH_LONG).show();
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ",";local tem file not created  created " +exception.toString());
            }
        });
    }
    public class Holders{
        TextView tanggal;
        MaterialButton download;

        public Holders(View v){
            tanggal=(TextView)v.findViewById(R.id.tanggal_warta);
            download=(MaterialButton)v.findViewById(R.id.download_warta);

        }
    }
}
