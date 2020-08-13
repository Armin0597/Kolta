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

public class KehadiranMainAct extends AppCompatActivity {

    Button btn_back;

    DatabaseReference reference;
    RecyclerView list_student_kehadiran;
    ArrayList<ListKehadiranItem> list;
    ListKehadiranAdapter listKehadiranAdapter;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kehadiran_main);

        btn_back = findViewById(R.id.btn_back);

        getUsernameLocal();

        list_student_kehadiran = findViewById(R.id.list_student_kehadiran);
        list_student_kehadiran.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<ListKehadiranItem>();

        reference = FirebaseDatabase.getInstance().getReference().child("Bimbingan").child(username_key_new).child("mahasiswa");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    ListKehadiranItem p = dataSnapshot1.getValue(ListKehadiranItem.class);
                    list.add(p);
                }
                listKehadiranAdapter = new ListKehadiranAdapter(KehadiranMainAct.this, list);
                list_student_kehadiran.setAdapter(listKehadiranAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome = new Intent(KehadiranMainAct.this, HomePageTeacherActivity.class);
                startActivity(gotohome);
            }
        });
    }
    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}