package com.example.gerejaonline.warta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gerejaonline.R;
import com.example.gerejaonline.jadwalIbadah.AddJadwalActivity;
import com.example.gerejaonline.warta.Util.PathUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import io.blackbox_vision.datetimepickeredittext.view.DatePickerEditText;

public class AddWartaActivity extends AppCompatActivity {
    DatePickerEditText tanggal;
    MaterialButton add,file;
    EditText textFile;
    String mUserId,mTanggal,mUrl;
    String userTask;
    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference;
    FirebaseUser mFirebaseUser;
    FirebaseAuth mFirebaseAuth;
    final static int PICK_PDF_CODE = 2342;
    public static final String STORAGE_PATH_UPLOADS = "warta/";
    Uri files;
    String path;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_warta);
        progressDialog=new ProgressDialog(AddWartaActivity.this);
        progressDialog.setMessage("Submit......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        file=(MaterialButton)findViewById(R.id.button_add_file_warta);
        add=(MaterialButton)findViewById(R.id.button_add_warta);
        textFile=(EditText)findViewById(R.id.file_warta);
        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
        mUserId=mFirebaseUser.getUid();
        mDatabaseReference= FirebaseDatabase.getInstance().getReference();
        mStorageReference= FirebaseStorage.getInstance().getReference();
        tanggal=(DatePickerEditText) findViewById(R.id.add_tanggal_warta);
        tanggal.setManager(AddWartaActivity.this.getSupportFragmentManager());
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
        userTask="wartaGereja"+dateTime;
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPDF();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTanggal=tanggal.getText().toString();
                mUrl=textFile.getText().toString();
                if (!mTanggal.isEmpty()){
                    if (!mUrl.isEmpty()){
                       uploadFile();
                    }else {
                        Toast.makeText(AddWartaActivity.this, "Pilih Tanggal Ibadah", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(AddWartaActivity.this, "Pilih Waktu Ibadah", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                files=data.getData();
                String uriString = files.toString();
                File myFile = new File(uriString);
                String path = myFile.getAbsolutePath();
                String displayName = null;

                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = AddWartaActivity.this.getContentResolver().query(files, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            textFile.setText(displayName);
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    displayName = myFile.getName();
                    textFile.setText(displayName);
                }
                File item=new File(files.getPath());
                    path=files.getEncodedPath();

            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void uploadFile() {
        progressDialog.show();
        String tanggals=tanggal.getText().toString();

        StorageReference sRef = mStorageReference.child(STORAGE_PATH_UPLOADS + userTask + ".pdf");
        sRef.putFile(files)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       mDatabaseReference.child("warta").child(userTask).child("tanggal").setValue(tanggals);
                       mDatabaseReference.child("warta").child(userTask).child("id").setValue(userTask);
                       onBackPressed();
                       progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    }
                });

    }
//    public String getPath(Uri uri) {
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        String document_id = cursor.getString(0);
//        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
//        cursor.close();
//
//        cursor = getContentResolver().query(
//                Uri.parse(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME),
//                null, MediaStore.Files.FileColumns._ID + " = ? ", new String[]{document_id}, null);
//        cursor.moveToFirst();
//        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
//        cursor.close();
//
//        return path;
//    }
}