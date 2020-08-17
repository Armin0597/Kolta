package com.koltaapp.kolta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class RevisiDetailAct extends AppCompatActivity {

    TextView nama_tugas,deskripsi,xdate,file_revisi,xdate_pertemuan;
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
        setContentView(R.layout.activity_revisi_detail);

        getUsernameLocal();

        String nama_tugas_baru = getIntent().getStringExtra("nama_revisi");
        String nama_file_baru = getIntent().getStringExtra("nama_file");
        String url_file_baru = getIntent().getStringExtra("url_document");
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
        btn_back = findViewById(R.id.btn_back);

        SpannableString content = new SpannableString(nama_file_baru);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        nama_tugas.setText(nama_tugas_baru);
        xdate.setText(tanggal_baru);
        xdate_pertemuan.setText(tanggal_pertemuan_baru);
        deskripsi.setText(deskripsi_baru);
        file_revisi.setText(content);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotorevisi = new Intent(RevisiDetailAct.this,RevisiTaskAct.class);
                gotorevisi.putExtra("username",username_baru);
                startActivity(gotorevisi);
            }
        });


    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}