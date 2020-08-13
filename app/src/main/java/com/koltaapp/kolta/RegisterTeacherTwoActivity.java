package com.koltaapp.kolta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterTeacherTwoActivity extends AppCompatActivity {

    Button btn_continue, btn_add_photo;
    LinearLayout btn_back;
    ImageView pic_photo_register;
    EditText nama,nip,prodi;

    Uri photo_location;
    Integer photo_max = 1;

    DatabaseReference reference;
    StorageReference storage;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher_two);

        getUsernameLocal();

        //mendaftarkan variabel
        //edit teks
        nama = findViewById(R.id.nama);
        nip = findViewById(R.id.nip);
        prodi = findViewById(R.id.prodi);

        //button
        btn_add_photo = findViewById(R.id.btn_add_photo);
        btn_continue = findViewById(R.id.btn_continue);
        btn_back = findViewById(R.id.btn_back);

        //imageview
        pic_photo_register = findViewById(R.id.pic_photo_register_user);

        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ubah state button
                btn_continue.setEnabled(false);
                btn_continue.setText("Loading ...");


                if(nama.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Username Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();

                    //ubah state menjadi loading
                    btn_continue.setEnabled(true);
                    btn_continue.setText("CONTINUE");
                }else if(nip.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Email Address Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();

                    //ubah state menjadi loading
                    btn_continue.setEnabled(true);
                    btn_continue.setText("CONTINUE");
                }else if(prodi.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();

                    //ubah state menjadi loading
                    btn_continue.setEnabled(true);
                    btn_continue.setText("CONTINUE");
                }else{
                    //menyimpan kedalam firebase
                    reference = FirebaseDatabase.getInstance().getReference()
                            .child("Dosen").child(username_key_new);
                    storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);

                    //validasi file (apakah ada?)
                    if(photo_location != null){
                        final StorageReference storageReference =
                                storage.child(System.currentTimeMillis() + "." + getFileExtension(photo_location));

                        storageReference.putFile(photo_location)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String uri_photo = uri.toString();
                                                reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                                reference.getRef().child("nama").setValue(nama.getText().toString());
                                                reference.getRef().child("nip").setValue(nip.getText().toString());
                                                reference.getRef().child("prodi").setValue(prodi.getText().toString());
                                            }
                                        });
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                //berpindah activity
                                Intent gotousersignup = new Intent(RegisterTeacherTwoActivity.this, UserSignupActivity.class);
                                startActivity(gotousersignup);
                            }
                        });
                    }
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregisterone = new Intent(RegisterTeacherTwoActivity.this, RegisterTeacherOneActivity.class);
                startActivity(gotoregisterone);
            }
        });

    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //menemukan foto
    public void findPhoto(){
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null) {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(pic_photo_register);
        }
    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

}
