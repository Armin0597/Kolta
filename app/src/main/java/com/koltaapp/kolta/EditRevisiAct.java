package com.koltaapp.kolta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditRevisiAct extends AppCompatActivity {

    Button btn_save;
    LinearLayout btn_back;
    EditText nama_mhs,edit_nama_revisi,edit_deskripsi_revisi,edit_file_revisi,edit_tanggal,edit_tanggal_pertemuan;
    DatePickerDialog.OnDateSetListener mDatesetListener,mDatesetListener_pertemuan;

    DatabaseReference reference,reference2,ref_username_dosen,ref_username_mhs;
    StorageReference storage;

    Uri doc_location;
    Integer doc_max = 1;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    String nama = "";
    String nim = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_revisi);

        getUsernameLocal();

        final String nama_tugas_baru = getIntent().getStringExtra("nama_revisi");
        final String nama_file_baru = getIntent().getStringExtra("nama_file");
        final String url_file_baru = getIntent().getStringExtra("url_document");
        final String deskripsi_baru = getIntent().getStringExtra("deskripsi");
        final String tanggal_baru = getIntent().getStringExtra("tanggal");
        final String tanggal_pertemuan_baru = getIntent().getStringExtra("tanggal_pertemuan");
        final String username_baru = getIntent().getStringExtra("username");

        nama_mhs = findViewById(R.id.nama_mhs);
        edit_nama_revisi = findViewById(R.id.edit_nama_revisi);
        edit_deskripsi_revisi = findViewById(R.id.edit_deskripsi_revisi);
        edit_file_revisi = findViewById(R.id.edit_file_revisi);
        edit_tanggal = findViewById(R.id.edit_tanggal);
        edit_tanggal_pertemuan = findViewById(R.id.edit_tanggal_pertemuan);
        btn_save = findViewById(R.id.btn_save);
        btn_back = findViewById(R.id.btn_back);

        nama_mhs.setEnabled(false);
        edit_nama_revisi.requestFocus();
        edit_nama_revisi.setText(nama_tugas_baru);
        edit_deskripsi_revisi.setText(deskripsi_baru);
        edit_file_revisi.setText(nama_file_baru);
        edit_tanggal.setText(tanggal_baru);
        edit_tanggal_pertemuan.setText(tanggal_pertemuan_baru);

        edit_file_revisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc();
            }
        });

        edit_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int  day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EditRevisiAct.this,
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

                DatePickerDialog dialog = new DatePickerDialog(EditRevisiAct.this,
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

        ref_username_mhs = FirebaseDatabase.getInstance().getReference().child("Mahasiswa").child(username_baru);
        ref_username_mhs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama = dataSnapshot.child("nama").getValue().toString();
                reference = FirebaseDatabase.getInstance().getReference().child("Revisi")
                        .child(username_key_new).child("bimbingan").child(username_baru).child("tugas").child(nama_tugas_baru);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        nama_mhs.setText(nama);
                        edit_nama_revisi.setText(dataSnapshot.child("nama_revisi").getValue().toString());
                        edit_deskripsi_revisi.setText(dataSnapshot.child("deskripsi").getValue().toString());
                        edit_file_revisi.setText(dataSnapshot.child("nama_file").getValue().toString());
                        edit_tanggal.setText(dataSnapshot.child("tanggal_pengumpulan").getValue().toString());
                        edit_tanggal_pertemuan.setText(dataSnapshot.child("tanggal_pertemuan").getValue().toString());
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



        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new)
                        .child(edit_nama_revisi.getText().toString());

                final String fileName;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                fileName = "rev_"+"bimb_"+nim+"_"+time+"."+getFileExtension(doc_location);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("nama_revisi").setValue(edit_nama_revisi.getText().toString());
                        dataSnapshot.getRef().child("nama_file").setValue(edit_file_revisi.getText().toString());
                        dataSnapshot.getRef().child("deskripsi").setValue(edit_deskripsi_revisi.getText().toString());
                        dataSnapshot.getRef().child("tanggal_pengumpulan").setValue(edit_tanggal.getText().toString());
                        dataSnapshot.getRef().child("tanggal_pertemuan").setValue(edit_tanggal_pertemuan.getText().toString());
                        dataSnapshot.getRef().child("username").setValue(username_baru);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                if(doc_location != null){
                    final StorageReference storageReference =
                            storage.child(fileName);
                    storageReference.putFile(doc_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String uri_doc = uri.toString();
                                    reference.getRef().child("url_document").setValue(uri_doc);
                                    //ubah state menjadi loading
                                    btn_save.setEnabled(false);
                                    btn_save.setText("Loading ...");

                                    Intent gotobimbingan = new Intent(EditRevisiAct.this, RevisiDetailAct.class);
                                    gotobimbingan.putExtra("username",username_baru);
                                    startActivity(gotobimbingan);
                                }
                            });
                        }
                    });
                }
                ref_username_dosen = FirebaseDatabase.getInstance().getReference().child("Revisi").child(username_key_new).child("username");
                ref_username_dosen.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ref_username_dosen.getRef().setValue(username_key_new);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                ref_username_mhs = FirebaseDatabase.getInstance().getReference().child("Revisi")
                        .child(username_key_new).child("bimbingan").child(username_baru).child("username");
                ref_username_mhs.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ref_username_mhs.getRef().setValue(username_baru);
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
                Intent gotorevisitask = new Intent(EditRevisiAct.this, RevisiDetailAct.class);
                gotorevisitask.putExtra("username",username_baru);
                startActivity(gotorevisitask);
            }
        });
    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //menemukan dokumen
    public void findDoc(){
        Intent doc = new Intent();
        doc.setType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        doc.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(doc, doc_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == doc_max && resultCode == RESULT_OK && data != null && data.getData() != null) {
            doc_location = data.getData();

            getUsernameLocal();
            final String new_username = getIntent().getStringExtra("username");

            ref_username_mhs = FirebaseDatabase.getInstance().getReference().child("Bimbingan")
                    .child(username_key_new).child("mahasiswa").child(new_username);
            ref_username_mhs.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    nim = dataSnapshot.child("nim").getValue().toString();

                    final String fileName;
                    long yourmilliseconds = System.currentTimeMillis();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                    Date resultdate = new Date(yourmilliseconds);
                    String time = simpleDateFormat.format(resultdate);
                    fileName = "rev_"+"bimb_"+nim+"_"+time+"."+getFileExtension(doc_location);
                    edit_file_revisi.setText(fileName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}