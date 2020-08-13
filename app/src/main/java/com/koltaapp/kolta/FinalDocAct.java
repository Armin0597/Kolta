package com.koltaapp.kolta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class FinalDocAct extends AppCompatActivity {

    Button add_draf_final,btn_back;
    RecyclerView list_finaldoc;

    ArrayList<ListBerkasFinItem> listBerkasFinItems;
    ListBerkasFinAdapter listBerkasFinAdapter;

    Uri doc_location;
    Integer doc_max = 1;

    DatabaseReference reference,reference2;
    StorageReference storage;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_doc);

        getUsernameLocal();

        btn_back = findViewById(R.id.btn_back);
        add_draf_final = findViewById(R.id.add_draf_final);

        list_finaldoc = findViewById(R.id.list_finaldoc);
        list_finaldoc.setLayoutManager(new LinearLayoutManager(this));
        listBerkasFinItems = new ArrayList<ListBerkasFinItem>();

        reference2 = FirebaseDatabase.getInstance().getReference().child("BerkasFinal").child(username_key_new).child("berkas");
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    ListBerkasFinItem p = dataSnapshot1.getValue(ListBerkasFinItem.class);
                    listBerkasFinItems.add(p);
                }
                listBerkasFinAdapter = new ListBerkasFinAdapter(FinalDocAct.this, listBerkasFinItems);
                list_finaldoc.setAdapter(listBerkasFinAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        add_draf_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohomepage = new Intent(FinalDocAct.this, HomePageActivity.class);
                startActivity(gotohomepage);
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
        doc.setType("application/pdf");
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

        final String fileName;
        Cursor cursor = getContentResolver().query(doc_location,null,null,null,null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        fileName = cursor.getString(idx);
        cursor.close();

        reference = FirebaseDatabase.getInstance().getReference()
                .child("BerkasFinal").child(username_key_new).child("berkas").push();
        storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

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