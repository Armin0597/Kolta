package com.koltaapp.kolta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomePageTeacherActivity extends AppCompatActivity {

    ImageView photo_home_user,page_bimbingan,page_revisi,page_mahasiswa,page_jadwal,page_kehadiran;
    TextView nama,nip;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_teacher_activity);

        //mendapatkan data user dari lokal storage
        getUsernameLocal();

        //mendaftarkan variabel
        nama = findViewById(R.id.nama);
        nip = findViewById(R.id.nip);
        photo_home_user = findViewById(R.id.photo_home_user);
        page_bimbingan = findViewById(R.id.page_bimbingan);
        page_kehadiran = findViewById(R.id.page_kehadiran);
        page_mahasiswa = findViewById(R.id.page_mahasiswa);
        page_jadwal = findViewById(R.id.page_jadwal);
        page_revisi = findViewById(R.id.page_revisi);


        //mengambil data dari firebase
        reference = FirebaseDatabase.getInstance().getReference()
                .child("Dosen").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama.setText(dataSnapshot.child("nama").getValue().toString());
                nip.setText(dataSnapshot.child("nip").getValue().toString());
                Picasso.with(HomePageTeacherActivity.this).load(dataSnapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(photo_home_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //intent menuju profile
        photo_home_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(HomePageTeacherActivity.this, ProfilTeacherActivity.class);
                startActivity(gotoprofile);
            }
        });

        //intent menuju bimbingan
        page_bimbingan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototask = new Intent(HomePageTeacherActivity.this,TaskMainAct.class);
                startActivity(gototask);
            }
        });

        //intent menuju presensi
        page_kehadiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotokehadiran = new Intent(HomePageTeacherActivity.this,KehadiranMainAct.class);
                startActivity(gotokehadiran);
            }
        });

        //intent menuju daftar mahasiswa
        page_mahasiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotolistmhs = new Intent(HomePageTeacherActivity.this,StudentListActivity.class);
                startActivity(gotolistmhs);
            }
        });

        //intent menuju jadwal
        page_jadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotojadwal = new Intent(HomePageTeacherActivity.this,JadwalTeacherAct.class);
                startActivity(gotojadwal);
            }
        });

        //intent menuju revisi
        page_revisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotojadwal = new Intent(HomePageTeacherActivity.this,RevisiMainAct.class);
                startActivity(gotojadwal);
            }
        });
    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}

