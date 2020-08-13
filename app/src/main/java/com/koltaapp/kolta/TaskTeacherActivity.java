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

public class TaskTeacherActivity extends AppCompatActivity {
    Button add_task, btn_back;

    DatabaseReference reference;
    RecyclerView task_place;
    ArrayList<ListStudentTask> list;
    ListStudentTaskAdapter listStudentTaskAdapter;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_teacher);

        getUsernameLocal();

        add_task = findViewById(R.id.add_task);
        btn_back = findViewById(R.id.btn_back);

        task_place = findViewById(R.id.task_place);
        task_place.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<ListStudentTask>();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoHomepage = new Intent(TaskTeacherActivity.this, HomePageTeacherActivity.class);
                startActivity(gotoHomepage);
            }
        });


        reference = FirebaseDatabase.getInstance().getReference().child("Tugas").child(username_key_new).child("task");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    ListStudentTask p = dataSnapshot1.getValue(ListStudentTask.class);
                    list.add(p);
                }
                listStudentTaskAdapter = new ListStudentTaskAdapter(TaskTeacherActivity.this, list);
                task_place.setAdapter(listStudentTaskAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                  openDialog();
                Intent gotoaddtask = new Intent(TaskTeacherActivity.this, AddTaskAct.class);
                startActivity(gotoaddtask);
            }
        });
    }
    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
