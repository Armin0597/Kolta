package com.koltaapp.kolta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class RevisiDetailAct extends AppCompatActivity {

    TextView nama_tugas,deskripsi,xdate,file_revisi,xdate_pertemuan;
    DatabaseReference reference,reference2;
    StorageReference storage;
    Button btn_back;

    DownloadManager downloadManager;

    Uri doc_location;
    Integer doc_max = 1;

    RecyclerView list_draft_student;
    ArrayList<ListRevisiDrafTeacherItem> listRevisiDrafTeacherItems;
    ListRevisiDrafTeacherAdapter listRevisiDrafTeacherAdapter;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revisi_detail);

        getUsernameLocal();

        final String nama_tugas_baru = getIntent().getStringExtra("nama_revisi");
        String nama_file_baru = getIntent().getStringExtra("nama_file");
        final String url_file_baru = getIntent().getStringExtra("url_document");
        String deskripsi_baru = getIntent().getStringExtra("deskripsi");
        String tanggal_baru = getIntent().getStringExtra("tanggal");
        String tanggal_pertemuan_baru = getIntent().getStringExtra("tanggal_pertemuan");
        final String username_baru = getIntent().getStringExtra("username");

        nama_tugas = findViewById(R.id.nama_tugas);
        deskripsi = findViewById(R.id.deskripsi);
        xdate = findViewById(R.id.xdate);
        xdate_pertemuan = findViewById(R.id.xdate_pertemuan);
        file_revisi = findViewById(R.id.file_revisi);
        btn_back = findViewById(R.id.btn_back);

        list_draft_student = findViewById(R.id.list_draft_student);
        list_draft_student.setLayoutManager(new LinearLayoutManager(this));
        listRevisiDrafTeacherItems = new ArrayList<ListRevisiDrafTeacherItem>();

        reference = FirebaseDatabase.getInstance().getReference().child("RevisiDraf").child(username_baru).child(nama_tugas_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    ListRevisiDrafTeacherItem p = dataSnapshot1.getValue(ListRevisiDrafTeacherItem.class);
                    listRevisiDrafTeacherItems.add(p);
                }
                listRevisiDrafTeacherAdapter = new ListRevisiDrafTeacherAdapter(RevisiDetailAct.this, listRevisiDrafTeacherItems);
                list_draft_student.setAdapter(listRevisiDrafTeacherAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        SpannableString content = new SpannableString(nama_file_baru);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        nama_tugas.setText(nama_tugas_baru);
        xdate.setText(tanggal_baru);
        xdate_pertemuan.setText(tanggal_pertemuan_baru);
        deskripsi.setText(deskripsi_baru);
        file_revisi.setText(content);

        file_revisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference2 =FirebaseDatabase.getInstance().getReference().child("Revisi").child(username_key_new)
                        .child(username_baru).child("tugas");
                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        downloadFile(this,nama_tugas_baru,
                                Environment.getExternalStorageState(new File(Environment.DIRECTORY_DOWNLOADS)),url_file_baru);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotorevisi = new Intent(RevisiDetailAct.this,RevisiTaskAct.class);
                gotorevisi.putExtra("username",username_baru);
                startActivity(gotorevisi);
            }
        });


    }

    public long downloadFile(ValueEventListener context, String fileName, String destinationDirectory, String url) {
        final RevisiDetailAct c =this;
        downloadManager = (DownloadManager) c.getSystemService(c.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setMimeType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(c, destinationDirectory, fileName);

        return downloadManager.enqueue(request);
    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}