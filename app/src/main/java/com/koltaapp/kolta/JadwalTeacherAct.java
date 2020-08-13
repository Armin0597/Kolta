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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.ArrayList;

public class JadwalTeacherAct extends AppCompatActivity {

    Button btn_back;

    RecyclerView list_jadwal_teacher;
    DatabaseReference reference;
    ArrayList<ListJadwalTeacherItem> listJadwalTeacherItems;
    ListJadwalTeacherAdapter listJadwalTeacherAdapter;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_teacher);
        AndroidThreeTen.init(this);

        btn_back = findViewById(R.id.btn_back);

        list_jadwal_teacher = findViewById(R.id.list_jadwal_teacher);
        list_jadwal_teacher.setLayoutManager(new LinearLayoutManager(this));
        listJadwalTeacherItems = new ArrayList<ListJadwalTeacherItem>();

        reference = FirebaseDatabase.getInstance().getReference().child("Jadwal");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ListJadwalTeacherItem p = ds.getValue(ListJadwalTeacherItem.class);
                    listJadwalTeacherItems.add(p);
                    listJadwalTeacherAdapter = new ListJadwalTeacherAdapter(JadwalTeacherAct.this, listJadwalTeacherItems);
                    list_jadwal_teacher.setAdapter(listJadwalTeacherAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohomepage = new Intent(JadwalTeacherAct.this, HomePageTeacherActivity.class);
                startActivity(gotohomepage);
            }
        });

    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}