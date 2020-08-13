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

import java.util.ArrayList;

public class JadwalStudentAct extends AppCompatActivity {

    Button btn_back;


    DatabaseReference reference;
    RecyclerView jadwal_student;
    ArrayList<ListJadwalStudentItem> listJadwalStudentItems;
    ListJadwalStudentAdapter listJadwalStudentAdapter;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_student);

        getUsernameLocal();

        btn_back =findViewById(R.id.btn_back);

        jadwal_student = findViewById(R.id.jadwal_student);
        jadwal_student.setLayoutManager(new LinearLayoutManager(this));
        listJadwalStudentItems = new ArrayList<ListJadwalStudentItem>();

        reference = FirebaseDatabase.getInstance().getReference().child("Jadwal");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ListJadwalStudentItem p = ds.getValue(ListJadwalStudentItem.class);
                    listJadwalStudentItems.add(p);
                    listJadwalStudentAdapter = new ListJadwalStudentAdapter(JadwalStudentAct.this, listJadwalStudentItems);
                    jadwal_student.setAdapter(listJadwalStudentAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome = new Intent(JadwalStudentAct.this, HomePageActivity.class);
                startActivity(gotohome);
            }
        });

    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}