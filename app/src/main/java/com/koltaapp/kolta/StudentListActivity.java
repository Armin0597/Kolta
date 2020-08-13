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

public class StudentListActivity extends AppCompatActivity {

    Button btn_back;


    DatabaseReference reference;
    RecyclerView list_student_place;
    ArrayList<ListStudent> listStudents;
    ListStudentAdapter listStudentAdapter;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        getUsernameLocal();

        btn_back =findViewById(R.id.btn_back);

        list_student_place = findViewById(R.id.list_student_place);
        list_student_place.setLayoutManager(new LinearLayoutManager(this));
        listStudents = new ArrayList<ListStudent>();

        reference = FirebaseDatabase.getInstance().getReference().child("Bimbingan").child(username_key_new).child("mahasiswa");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    ListStudent p = dataSnapshot1.getValue(ListStudent.class);
                    listStudents.add(p);
                }
                listStudentAdapter = new ListStudentAdapter(StudentListActivity.this, listStudents);
                list_student_place.setAdapter(listStudentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome = new Intent(StudentListActivity.this, HomePageTeacherActivity.class);
                startActivity(gotohome);
            }
        });

    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
