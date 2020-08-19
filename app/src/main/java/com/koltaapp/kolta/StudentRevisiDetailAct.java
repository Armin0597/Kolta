package com.koltaapp.kolta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class StudentRevisiDetailAct extends AppCompatActivity {

    TextView nama_tugas,deskripsi,xdate,file_revisi,xdate_pertemuan;
    Button btn_back,add_draf_revisi;
    ImageView photo_profile;

    DownloadManager downloadManager;

    RecyclerView list_draft_student;
    ArrayList<ListRevisiDraftStudentItem> listRevisiDraftStudentItems;
    ListRevisiDraftStudentAdapter listRevisiDraftStudentAdapter;

    StorageReference storage;
    DatabaseReference reference,reference2,reference3;

    Uri doc_location;
    Integer doc_max = 1;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    String Dsn_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_revisi_detail);

        getUsernameLocal();

        final String nama_tugas_baru = getIntent().getStringExtra("nama_revisi");
        final String nama_file_baru = getIntent().getStringExtra("nama_file");
        final String url_file_baru = getIntent().getStringExtra("url_document");
        String deskripsi_baru = getIntent().getStringExtra("deskripsi");
        String tanggal_baru = getIntent().getStringExtra("tanggal");
        String tanggal_pertemuan_baru = getIntent().getStringExtra("tanggal_pertemuan");
        final String username_baru = getIntent().getStringExtra("username");

        nama_tugas = findViewById(R.id.nama_tugas);
        deskripsi = findViewById(R.id.deskripsi);
        xdate = findViewById(R.id.xdate);
        xdate_pertemuan = findViewById(R.id.xdate_pertemuan);
        file_revisi = findViewById(R.id.file_revisi);
        btn_back = findViewById(R.id.btn_back);
        btn_back = findViewById(R.id.btn_back);
        add_draf_revisi = findViewById(R.id.add_draf_revisi);

        list_draft_student = findViewById(R.id.list_draft_student);
        list_draft_student.setLayoutManager(new LinearLayoutManager(this));
        listRevisiDraftStudentItems = new ArrayList<ListRevisiDraftStudentItem>();


        final SpannableString content = new SpannableString(nama_file_baru);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        nama_tugas.setText(nama_tugas_baru);
        xdate.setText(tanggal_baru);
        xdate_pertemuan.setText(tanggal_pertemuan_baru);
        deskripsi.setText(deskripsi_baru);
        file_revisi.setText(content);

        file_revisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseDatabase.getInstance().getReference().child("Dibimbing").child(username_key_new);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Dsn_id = dataSnapshot.child("dsn_id").getValue().toString();
                        reference2 = FirebaseDatabase.getInstance().getReference().child(Dsn_id).child("bimbingan")
                                .child(username_key_new).child("tugas");
                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                downloadFile(this,nama_file_baru,
                                        Environment.getExternalStorageState(new File(Environment.DIRECTORY_DOWNLOADS)),url_file_baru);

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
            }
        });

        reference3 = FirebaseDatabase.getInstance().getReference().child("RevisiDraf").child(username_key_new).child(nama_tugas_baru);
        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    ListRevisiDraftStudentItem p = dataSnapshot1.getValue(ListRevisiDraftStudentItem.class);
                    listRevisiDraftStudentItems.add(p);
                }
                listRevisiDraftStudentAdapter = new ListRevisiDraftStudentAdapter(StudentRevisiDetailAct.this,listRevisiDraftStudentItems);
                list_draft_student.setAdapter(listRevisiDraftStudentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        add_draf_revisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotorevisi = new Intent(StudentRevisiDetailAct.this,StudentRevisiMainAct.class);
                gotorevisi.putExtra("username",username_baru);
                startActivity(gotorevisi);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == doc_max && resultCode == RESULT_OK && data != null && data.getData() != null) {
            doc_location = data.getData();
            uploadDoc();
        }
    }

    public void uploadDoc(){

        getUsernameLocal();
        final ProgressDialog pd = new ProgressDialog(this);
        final String nama_tugas_baru = getIntent().getStringExtra("nama_revisi");

        final String fileName;
        Cursor cursor = getContentResolver().query(doc_location,null,null,null,null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        fileName = cursor.getString(idx);
        cursor.close();

        reference2 = FirebaseDatabase.getInstance().getReference()
                .child("RevisiDraf").child(username_key_new).child(nama_tugas_baru).push();
        storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("Revisi").child(nama_tugas_baru);

        //validasi file (apakah ada?)
        if(doc_location != null) {
            final StorageReference storageReference =
                    storage.child(fileName);

            storageReference.putFile(doc_location)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    pd.dismiss();
                                    String uri_doc = uri.toString();
                                    reference2.getRef().child("nama_file").setValue(fileName);
                                    reference2.getRef().child("url_document").setValue(uri_doc);
                                    reference2.getRef().child("mhs_id").setValue(username_key_new);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            pd.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    pd.setMessage("Percent : " + (int) progressPercent + "%");
                    pd.show();
                }
            });
        }
    }


    public long downloadFile(ValueEventListener context, String fileName, String destinationDirectory, String url) {
        final StudentRevisiDetailAct c =this;
        downloadManager = (DownloadManager) c.getSystemService(c.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setMimeType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(c, destinationDirectory, fileName);

        return downloadManager.enqueue(request);
    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}