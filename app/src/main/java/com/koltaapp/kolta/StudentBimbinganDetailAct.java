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
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class StudentBimbinganDetailAct extends AppCompatActivity {

    TextView nama_tugas,deskripsi,xdate,xdate_pertemuan,nama_mhs,file_bimbingan;
    Button btn_back,add_bimbingan;
    RecyclerView list_draft;

    ArrayList<ListDraftStudentItem> listDraftStudentItems;
    ListDraftStudentAdapter listDraftStudentAdapter;

    DatabaseReference reference,reference2;
    StorageReference storage;

    Uri doc_location;
    Integer doc_max = 1;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_bimbingan_detail);

        getUsernameLocal();

        String nama_tugas_baru = getIntent().getStringExtra("nama_tugas");
        final String nama_file_baru = getIntent().getStringExtra("nama_file");
        final String url_file_baru = getIntent().getStringExtra("url_document");
        String deskripsi_baru = getIntent().getStringExtra("deskripsi");
        String tanggal_baru = getIntent().getStringExtra("tanggal");
        String tanggal_pertemuan = getIntent().getStringExtra("tanggal_pertemuan");
        final String extras_username = getIntent().getStringExtra("username");

        nama_tugas = findViewById(R.id.nama_tugas);
        deskripsi = findViewById(R.id.deskripsi);
        xdate = findViewById(R.id.xdate);
        xdate_pertemuan = findViewById(R.id.xdate_pertemuan);
        nama_mhs = findViewById(R.id.nama_mhs);
        file_bimbingan = findViewById(R.id.file_bimbingan);
        btn_back = findViewById(R.id.btn_back);
        add_bimbingan = findViewById(R.id.add_bimbingan);

        SpannableString content = new SpannableString(nama_file_baru);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        nama_tugas.setText(nama_tugas_baru);
        deskripsi.setText(deskripsi_baru);
        xdate.setText(tanggal_baru);
        xdate_pertemuan.setText(tanggal_pertemuan);
        file_bimbingan.setText(content);

        list_draft = findViewById(R.id.list_draft);
        list_draft.setLayoutManager(new LinearLayoutManager(this));
        listDraftStudentItems = new ArrayList<ListDraftStudentItem>();

        file_bimbingan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference =FirebaseDatabase.getInstance().getReference().child("Tugas").child(username_key_new)
                        .child(extras_username).child("tugas");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
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
        });

        reference2 = FirebaseDatabase.getInstance().getReference().child("Draf").child(username_key_new).child(nama_tugas_baru);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    ListDraftStudentItem p = dataSnapshot1.getValue(ListDraftStudentItem.class);
                    listDraftStudentItems.add(p);
                }
                listDraftStudentAdapter = new ListDraftStudentAdapter(StudentBimbinganDetailAct.this, listDraftStudentItems);
                list_draft.setAdapter(listDraftStudentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        add_bimbingan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc();
                }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotobimbingan = new Intent(StudentBimbinganDetailAct.this, StudentBimbinganMainAct.class);
                startActivity(gotobimbingan);
            }
        });
    }

    public long downloadFile(ValueEventListener context, String fileName, String destinationDirectory, String url) {
        final StudentBimbinganDetailAct c =this;
        DownloadManager downloadManager = (DownloadManager) c.getSystemService(c.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setMimeType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(c, destinationDirectory, fileName);

        return downloadManager.enqueue(request);
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
        String nama_tugas_baru = getIntent().getStringExtra("nama_tugas");

        final String fileName;
        Cursor cursor = getContentResolver().query(doc_location,null,null,null,null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        fileName = cursor.getString(idx);
        cursor.close();

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Draf").child(username_key_new).child(nama_tugas_baru).push();
        storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child(nama_tugas_baru);

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
                                    reference.getRef().child("nama_file").setValue(fileName);
                                    reference.getRef().child("url_document").setValue(uri_doc);
                                    reference.getRef().child("mhs_id").setValue(username_key_new);
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


    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}