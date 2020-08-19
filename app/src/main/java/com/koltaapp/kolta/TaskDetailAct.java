package com.koltaapp.kolta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class TaskDetailAct extends AppCompatActivity {

    TextView nama_tugas,deskripsi,xdate,xdate_pertemuan,file_tugas;
    DatabaseReference reference,reference2;
    Button btn_back;
    ImageView photo_profile;

    RecyclerView list_draft_student;
    ArrayList<ListDrafBimbinganItem> listDrafBimbinganItems;
    ListDraftBimbinganAdapter listDraftBimbinganAdapter;


    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        getUsernameLocal();

        final String nama_tugas_baru = getIntent().getStringExtra("nama_tugas");
        final String nama_file_baru = getIntent().getStringExtra("nama_file");
        final String url_document_baru = getIntent().getStringExtra("url_document");
        String deskripsi_baru = getIntent().getStringExtra("deskripsi");
        String tanggal_baru = getIntent().getStringExtra("tanggal");
        String tanggal_pertemuan = getIntent().getStringExtra("tanggal_pertemuan");
        final String username_baru = getIntent().getStringExtra("username");

        nama_tugas = findViewById(R.id.nama_tugas);
        deskripsi = findViewById(R.id.deskripsi);
        file_tugas = findViewById(R.id.file_tugas);
        xdate = findViewById(R.id.xdate);
        xdate_pertemuan = findViewById(R.id.xdate_pertemuan);
        btn_back = findViewById(R.id.btn_back);
        photo_profile = findViewById(R.id.photo_profile);

        SpannableString content = new SpannableString(nama_file_baru);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        nama_tugas.setText(nama_tugas_baru);
        xdate.setText(tanggal_baru);
        xdate_pertemuan.setText(tanggal_pertemuan);
        deskripsi.setText(deskripsi_baru);
        file_tugas.setText(content);

        file_tugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference =FirebaseDatabase.getInstance().getReference().child("Bimbingan").child(username_key_new)
                        .child(username_baru).child("tugas");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        downloadFile(this,nama_file_baru,
                                Environment.getExternalStorageState(new File(Environment.DIRECTORY_DOWNLOADS)),url_document_baru);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        list_draft_student = findViewById(R.id.list_draft_student);
        list_draft_student.setLayoutManager(new LinearLayoutManager(this));
        listDrafBimbinganItems = new ArrayList<ListDrafBimbinganItem>();

        reference2 = FirebaseDatabase.getInstance().getReference().child("Draf").child(username_baru).child(nama_tugas_baru);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    ListDrafBimbinganItem p = dataSnapshot1.getValue(ListDrafBimbinganItem.class);
                    listDrafBimbinganItems.add(p);
                }
                listDraftBimbinganAdapter = new ListDraftBimbinganAdapter(TaskDetailAct.this, listDrafBimbinganItems);
                list_draft_student.setAdapter(listDraftBimbinganAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotobimbingan = new Intent(TaskDetailAct.this,BimbinganStudentAct.class);
                gotobimbingan.putExtra("username",username_baru);
                startActivity(gotobimbingan);
            }
        });

    }

    public long downloadFile(ValueEventListener context, String fileName, String destinationDirectory, String url) {
        final TaskDetailAct c =this;
        DownloadManager downloadManager = (DownloadManager) c.getSystemService(c.DOWNLOAD_SERVICE);
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