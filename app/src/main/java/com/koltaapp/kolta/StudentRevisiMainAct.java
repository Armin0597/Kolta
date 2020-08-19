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

public class StudentRevisiMainAct extends AppCompatActivity {

    Button btn_back;
    TextView nama_dsn;
    DatabaseReference reference,reference2,reference3;
    RecyclerView list_revisi;
    ArrayList<ListRevisiStudentItem> listRevisiStudentItems;
    ListRevisiStudentAdapter listRevisiStudentAdapter;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    String DsnId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_revisi_main);

        getUsernameLocal();

        btn_back = findViewById(R.id.btn_back);

        list_revisi = findViewById(R.id.list_revisi);
        list_revisi.setLayoutManager(new LinearLayoutManager(this));
        listRevisiStudentItems = new ArrayList<ListRevisiStudentItem>();

        reference = FirebaseDatabase.getInstance().getReference().child("Dibimbing")
                .child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DsnId = dataSnapshot.child("dsn_id").getValue().toString();

                reference2 = FirebaseDatabase.getInstance().getReference().child("Revisi")
                        .child(DsnId).child("bimbingan").child(username_key_new).child("tugas");
                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            ListRevisiStudentItem p = dataSnapshot1.getValue(ListRevisiStudentItem.class);
                            listRevisiStudentItems.add(p);
                        }
                        listRevisiStudentAdapter = new ListRevisiStudentAdapter(StudentRevisiMainAct.this, listRevisiStudentItems);
                        list_revisi.setAdapter(listRevisiStudentAdapter);
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
                Intent gotohomepage = new Intent(StudentRevisiMainAct.this, HomePageActivity.class);
                startActivity(gotohomepage);
            }
        });
    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}