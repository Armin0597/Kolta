package com.koltaapp.kolta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PresensiStudentAct extends AppCompatActivity {

    Button btn_back,add_presensi;
    TextView nama_mhs;

    DatabaseReference reference,reference2;
    RecyclerView presensi;
    ArrayList<ListPresensiItem> listPresensiItems;
    ListPresensiAdapter listPresensiAdapter;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presensi_student);

        getUsernameLocal();

        btn_back = findViewById(R.id.btn_back);
        nama_mhs = findViewById(R.id.nama_mhs);
        add_presensi = findViewById(R.id.add_presensi);

        final String username_baru = getIntent().getStringExtra("username");

        presensi = findViewById(R.id.presensi);
        presensi.setLayoutManager(new LinearLayoutManager(this));
        listPresensiItems = new ArrayList<ListPresensiItem>();

        reference2 = FirebaseDatabase.getInstance().getReference().child("Mahasiswa").child(username_baru);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_mhs.setText(dataSnapshot.child("nama").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Kehadiran")
                .child(username_key_new).child("mahasiswa").child(username_baru).child("presensi");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    ListPresensiItem p = dataSnapshot1.getValue(ListPresensiItem.class);
                    listPresensiItems.add(p);
                }
                listPresensiAdapter = new ListPresensiAdapter(PresensiStudentAct.this, listPresensiItems);
                presensi.setAdapter(listPresensiAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        add_presensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoaddtask = new Intent(PresensiStudentAct.this, AddPresensiAct.class);
                gotoaddtask.putExtra("username",username_baru);
                startActivity(gotoaddtask);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotokehadiran = new Intent(PresensiStudentAct.this, KehadiranMainAct.class);
                startActivity(gotokehadiran);
            }
        });


    }
    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}