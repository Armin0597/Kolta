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

public class StudentPresensiMainAct extends AppCompatActivity {

    Button btn_back;
    TextView xdate;

    DatabaseReference reference,reference2;
    RecyclerView list_student_presensi;
    ArrayList<StudentPresensiItem> studentPresensiItems;
    StudentPresensiAdapter studentPresensiAdapter;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    String Dsn_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_presensi_main);

        getUsernameLocal();

        btn_back = findViewById(R.id.btn_back);
        xdate = findViewById(R.id.xdate);

        list_student_presensi = findViewById(R.id.list_student_presensi);
        list_student_presensi.setLayoutManager(new LinearLayoutManager(this));
        studentPresensiItems = new ArrayList<StudentPresensiItem>();

        reference = FirebaseDatabase.getInstance().getReference().child("Dibimbing")
                .child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Dsn_id = dataSnapshot.child("dsn_id").getValue().toString();

                reference2 = FirebaseDatabase.getInstance().getReference().child("Kehadiran")
                        .child(Dsn_id).child("mahasiswa").child(username_key_new).child("presensi");
                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            StudentPresensiItem p = dataSnapshot1.getValue(StudentPresensiItem.class);
                            studentPresensiItems.add(p);
                        }
                        studentPresensiAdapter = new StudentPresensiAdapter(StudentPresensiMainAct.this, studentPresensiItems);
                        list_student_presensi.setAdapter(studentPresensiAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome = new Intent(StudentPresensiMainAct.this, HomePageActivity.class);
                startActivity(gotohome);
            }
        });

    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}