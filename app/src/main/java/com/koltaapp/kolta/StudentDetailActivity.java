package com.koltaapp.kolta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class StudentDetailActivity extends AppCompatActivity {

    DatabaseReference reference;
    ImageView photo_user_profile;
    TextView nama_mhs,xnim, xprodi, xemail_address;
    Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        final String username_baru = getIntent().getStringExtra("username");

        btn_back = findViewById(R.id.btn_back);
        photo_user_profile = findViewById(R.id.photo_user_profile);
        nama_mhs = findViewById(R.id.nama_mhs);
        xnim = findViewById(R.id.xnim);
        xprodi = findViewById(R.id.xprodi);
        xemail_address = findViewById(R.id.xemail_address);

        reference = FirebaseDatabase.getInstance().getReference().child("Mahasiswa").child(username_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.with(StudentDetailActivity.this).load(dataSnapshot.child("url_photo_profile")
                        .getValue().toString()).centerCrop().fit().into(photo_user_profile);
                nama_mhs.setText(dataSnapshot.child("nama").getValue().toString());
                xnim.setText(dataSnapshot.child("nim").getValue().toString());
                xprodi.setText(dataSnapshot.child("prodi").getValue().toString());
                xemail_address.setText(dataSnapshot.child("email_address").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotodatamhs = new Intent(StudentDetailActivity.this, StudentListActivity.class);
                startActivity(gotodatamhs);
            }
        });
    }
}
