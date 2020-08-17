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

public class HomePageActivity extends AppCompatActivity {

    ImageView photo_home_user,dosen_page,bimbingan_page,revisi_page,kehadiran_page,jadwal_page,finaldoc_page;
    TextView nama,nim;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        getUsernameLocal();

        //mendaftarkan variabel
        nama = findViewById(R.id.nama);
        nim = findViewById(R.id.nim);
        photo_home_user = findViewById(R.id.photo_home_user);
        bimbingan_page = findViewById(R.id.bimbingan_page);
        revisi_page = findViewById(R.id.revisi_page);
        kehadiran_page = findViewById(R.id.kehadiran_page);
        dosen_page = findViewById(R.id.dosen_page);
        jadwal_page = findViewById(R.id.jadwal_page);
        finaldoc_page = findViewById(R.id.finaldoc_page);

        bimbingan_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotobimbinganpage = new Intent(HomePageActivity.this, StudentBimbinganMainAct.class);
                startActivity(gotobimbinganpage);
            }
        });

        revisi_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotorevisi = new Intent(HomePageActivity.this, StudentBimbinganMainAct.class);
                startActivity(gotorevisi);
            }
        });

        kehadiran_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotokehadiranpage = new Intent(HomePageActivity.this, StudentPresensiMainAct.class);
                startActivity(gotokehadiranpage);
            }
        });

        dosen_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotodosenpage = new Intent(HomePageActivity.this, DosenListAct.class);
                startActivity(gotodosenpage);
            }
        });

        jadwal_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotojadwalstudent = new Intent(HomePageActivity.this, JadwalStudentAct.class);
                startActivity(gotojadwalstudent);
            }
        });

        finaldoc_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotofinaldoc = new Intent(HomePageActivity.this, FinalDocAct.class);
                startActivity(gotofinaldoc);
            }
        });

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Mahasiswa").child(username_key_new);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama.setText(dataSnapshot.child("nama").getValue().toString());
                nim.setText(dataSnapshot.child("nim").getValue().toString());
                Picasso.with(HomePageActivity.this).load(dataSnapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(photo_home_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        photo_home_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(HomePageActivity.this, ProfilActivity.class);
                startActivity(gotoprofile);
            }
        });
    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
