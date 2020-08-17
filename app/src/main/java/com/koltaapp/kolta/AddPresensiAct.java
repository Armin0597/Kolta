package com.koltaapp.kolta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class AddPresensiAct extends AppCompatActivity {

    EditText nama_mhs,edit_kegiatan,edit_tanggal,edit_tanggal_pertemuan;
    LinearLayout btn_back;
    Button btn_save;
    DatePickerDialog.OnDateSetListener mDatesetListener,mDatesetListener_pertemuan;

    DatabaseReference reference,reference2,reference_dsn,reference_mhs;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_presensi);

        getUsernameLocal();

        final String new_username = getIntent().getStringExtra("username");

        nama_mhs = findViewById(R.id.nama_mhs);
        edit_kegiatan = findViewById(R.id.edit_kegiatan);
        edit_tanggal = findViewById(R.id.edit_tanggal);
        edit_tanggal_pertemuan = findViewById(R.id.edit_tanggal_pertemuan);
        btn_save = findViewById(R.id.btn_save);
        btn_back = findViewById(R.id.btn_back);

        nama_mhs.setEnabled(false);
        edit_kegiatan.requestFocus();

        edit_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int  day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddPresensiAct.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDatesetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        edit_tanggal_pertemuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int  day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddPresensiAct.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDatesetListener_pertemuan, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDatesetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "-" + month + "-" + year;
                edit_tanggal.setText(date);
            }
        };

        mDatesetListener_pertemuan = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "-" + month + "-" + year;
                edit_tanggal_pertemuan.setText(date);
            }
        };

        reference = FirebaseDatabase.getInstance().getReference().child("Mahasiswa").child(new_username);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_mhs.setText(dataSnapshot.child("nama").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference2 = FirebaseDatabase.getInstance().getReference().child("Kehadiran").child(username_key_new)
                        .child("mahasiswa").child(new_username).child("presensi").child(edit_kegiatan.getText().toString());
                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        reference2.getRef().child("username").setValue(new_username);
                        reference2.getRef().child("kegiatan").setValue(edit_kegiatan.getText().toString());
                        reference2.getRef().child("tanggal_pengumpulan").setValue(edit_tanggal.getText().toString());
                        reference2.getRef().child("tanggal_pertemuan").setValue(edit_tanggal_pertemuan.getText().toString());

                        //ubah state menjadi loading
                        btn_save.setEnabled(false);
                        btn_save.setText("Loading ...");

                        Intent gotopresensi = new Intent(AddPresensiAct.this, PresensiStudentAct.class);
                        gotopresensi.putExtra("username", new_username);
                        startActivity(gotopresensi);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                reference_dsn = FirebaseDatabase.getInstance().getReference().child("Kehadiran").child(username_key_new).child("username");
                reference_dsn.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference_dsn.getRef().setValue(username_key_new);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                reference_mhs = FirebaseDatabase.getInstance().getReference().child("Kehadiran")
                        .child(username_key_new).child("mahasiswa").child(new_username).child("username");
                reference_mhs.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference_mhs.getRef().setValue(new_username);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotopresensi = new Intent(AddPresensiAct.this, PresensiStudentAct.class);
                gotopresensi.putExtra("username",new_username);
                startActivity(gotopresensi);
            }
        });

    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}