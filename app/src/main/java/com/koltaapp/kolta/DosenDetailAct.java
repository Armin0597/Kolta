package com.koltaapp.kolta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DosenDetailAct extends AppCompatActivity {

    Button btn_back;
    TextView nama_dsn,xnip,xprodi,xemail_address;
    ImageView photo_user_profile;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen_detail);

        final String username_baru = getIntent().getStringExtra("username");

        btn_back = findViewById(R.id.btn_back);
        nama_dsn = findViewById(R.id.nama_dsn);
        xnip = findViewById(R.id.xnip);
        xprodi = findViewById(R.id.xprodi);
        xemail_address = findViewById(R.id.xemail_address);
        photo_user_profile = findViewById(R.id.photo_user_profile);

        reference = FirebaseDatabase.getInstance().getReference().child("Dosen").child(username_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_dsn.setText(dataSnapshot.child("nama").getValue().toString());
                xnip.setText(dataSnapshot.child("nip").getValue().toString());
                xprodi.setText(dataSnapshot.child("prodi").getValue().toString());
                xemail_address.setText(dataSnapshot.child("email_address").getValue().toString());
                Picasso.with(DosenDetailAct.this).load(dataSnapshot.child("url_photo_profile")
                        .getValue().toString()).fit().into(photo_user_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotodosendata = new Intent(DosenDetailAct.this, DosenListAct.class);
                startActivity(gotodosendata);
            }
        });

    }
}