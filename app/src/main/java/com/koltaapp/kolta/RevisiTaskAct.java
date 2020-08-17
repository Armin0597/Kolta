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

public class RevisiTaskAct extends AppCompatActivity {

    Button btn_back,add_revisi;
    TextView nama_mhs;
    DatabaseReference reference,reference2,reference3;
    RecyclerView revisi_task_place;
    ArrayList<RevisiTaskItem> revisiTaskItems;
    RevisiTaskAdapter revisiTaskAdapter;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revisi_task);

        getUsernameLocal();

        btn_back = findViewById(R.id.btn_back);
        add_revisi = findViewById(R.id.add_revisi);
        nama_mhs = findViewById(R.id.nama_mhs);

        final String username_baru = getIntent().getStringExtra("username");

        revisi_task_place = findViewById(R.id.revisi_task_place);
        revisi_task_place.setLayoutManager(new LinearLayoutManager(this));
        revisiTaskItems = new ArrayList<RevisiTaskItem>();

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

        reference = FirebaseDatabase.getInstance().getReference().child("Revisi")
                .child(username_key_new).child("bimbingan").child(username_baru).child("tugas");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    RevisiTaskItem p = dataSnapshot1.getValue(RevisiTaskItem.class);
                    revisiTaskItems.add(p);
                }
                revisiTaskAdapter = new RevisiTaskAdapter(RevisiTaskAct.this, revisiTaskItems);
                revisi_task_place.setAdapter(revisiTaskAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        add_revisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoaddrevisi = new Intent(RevisiTaskAct.this, AddRevisiAct.class);
                gotoaddrevisi.putExtra("username",username_baru);
                startActivity(gotoaddrevisi);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototask = new Intent(RevisiTaskAct.this, RevisiMainAct.class);
                startActivity(gototask);
            }
        });
    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}