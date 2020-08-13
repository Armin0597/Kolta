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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DosenListAct extends AppCompatActivity {

    Button btn_back;


    DatabaseReference reference,reference_dsn;
    RecyclerView list_dosen_place;
    ArrayList<ListDosenItem> listDosenItems;
    ListDosenAdapter listDosenAdapter;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    String Dsn_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen_list);

        getUsernameLocal();

        btn_back =findViewById(R.id.btn_back);

        list_dosen_place = findViewById(R.id.list_dosen_place);
        list_dosen_place.setLayoutManager(new LinearLayoutManager(this));
        listDosenItems = new ArrayList<ListDosenItem>();

        reference = FirebaseDatabase.getInstance().getReference().child("Dibimbing").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Dsn_id = dataSnapshot.child("dsn_id").getValue().toString();

                reference_dsn = FirebaseDatabase.getInstance().getReference().child("Dosen").child(Dsn_id);
                reference_dsn.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ListDosenItem p = dataSnapshot.getValue(ListDosenItem.class);
                        listDosenItems.add(p);
                        listDosenAdapter = new ListDosenAdapter(DosenListAct.this, listDosenItems);
                        list_dosen_place.setAdapter(listDosenAdapter);
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
                Intent gotohome = new Intent(DosenListAct.this, HomePageActivity.class);
                startActivity(gotohome);
            }
        });
    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}