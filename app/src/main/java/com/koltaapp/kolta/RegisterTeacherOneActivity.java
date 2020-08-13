package com.koltaapp.kolta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterTeacherOneActivity extends AppCompatActivity {

    Button btn_continue;
    LinearLayout btn_back;
    EditText username,email_address,password;

    DatabaseReference reference, reference_username;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher_one);

        //mendaftarkan variabel
        //edit teks
        username = findViewById(R.id.username);
        email_address = findViewById(R.id.email_address);
        password = findViewById(R.id.password);

        //button
        btn_continue = findViewById(R.id.btn_continue);
        btn_back = findViewById(R.id.btn_back);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ubah state menjadi loading
                btn_continue.setEnabled(false);
                btn_continue.setText("Loading ...");

                if(username.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Username Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();

                    //ubah state menjadi loading
                    btn_continue.setEnabled(true);
                    btn_continue.setText("CONTINUE");
                }else if(email_address.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Email Address Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();

                    //ubah state menjadi loading
                    btn_continue.setEnabled(true);
                    btn_continue.setText("CONTINUE");
                }else if(password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();

                    //ubah state menjadi loading
                    btn_continue.setEnabled(true);
                    btn_continue.setText("CONTINUE");
                }else{
                    // mengambil username dari firebase
                    reference_username = FirebaseDatabase.getInstance().getReference()
                            .child("Dosen").child(username.getText().toString());
                    reference_username.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Toast.makeText(getApplicationContext(), "Username Sudah Tersedia!", Toast.LENGTH_SHORT).show();

                                //ubah state menjadi loading
                                btn_continue.setEnabled(true);
                                btn_continue.setText("CONTINUE");
                            }else{
                                //menyimpan data pada local storage
                                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(username_key, username.getText().toString());
                                editor.apply();

                                //menyimpan kedalam database
                                reference = FirebaseDatabase.getInstance().getReference()
                                        .child("Dosen").child(username.getText().toString());
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(username.getText().toString().isEmpty()){
                                            Toast.makeText(getApplicationContext(), "Username Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();

                                            //ubah state menjadi loading
                                            btn_continue.setEnabled(true);
                                            btn_continue.setText("CONTINUE");
                                        }else if(email_address.getText().toString().isEmpty()){
                                            Toast.makeText(getApplicationContext(), "Email Address Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();

                                            //ubah state menjadi loading
                                            btn_continue.setEnabled(true);
                                            btn_continue.setText("CONTINUE");
                                        }else if(password.getText().toString().isEmpty()){
                                            Toast.makeText(getApplicationContext(), "Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();

                                            //ubah state menjadi loading
                                            btn_continue.setEnabled(true);
                                            btn_continue.setText("CONTINUE");
                                        }else{
                                            dataSnapshot.getRef().child("username").setValue(username.getText().toString());
                                            dataSnapshot.getRef().child("password").setValue(password.getText().toString());
                                            dataSnapshot.getRef().child("email_address").setValue(email_address.getText().toString());

                                            //berpindah activity
                                            Intent gotoregistertwo = new Intent(RegisterTeacherOneActivity.this, RegisterTeacherTwoActivity.class);
                                            startActivity(gotoregistertwo);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotochooserole = new Intent(RegisterTeacherOneActivity.this, ChooseRoleActivity.class);
                startActivity(gotochooserole);
            }
        });
    }
}
