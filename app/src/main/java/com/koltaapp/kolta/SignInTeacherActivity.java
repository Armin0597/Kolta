package com.koltaapp.kolta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInTeacherActivity extends AppCompatActivity {

    Button btn_signin;
    TextView btn_create_new_acc;
    EditText xusername, xpassword;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_teacher);

        //mendaftarkan variabel
        //edit text
        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);

        //button
        btn_signin = findViewById(R.id.btn_signin);
        btn_create_new_acc = findViewById(R.id.btn_create_new_acc);

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = xusername.getText().toString();
                final String password = xpassword.getText().toString();

                btn_signin.setEnabled(false);
                btn_signin.setText("Loading ...");

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("Dosen").child(username);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            //ambil data password
                            String passwordFromFirebase = dataSnapshot.child("password").getValue().toString();

                            //validasi password
                            if (password.equals(passwordFromFirebase)) {
                                //simpan username pada local storage
                                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(username_key, xusername.getText().toString());
                                editor.apply();

                                //berpindah activity
                                Intent gotohome = new Intent(SignInTeacherActivity.this, HomePageTeacherActivity.class);
                                startActivity(gotohome);
                            } else {
                                Toast.makeText(getApplicationContext(), "Password Salah", Toast.LENGTH_SHORT).show();

                                //ubah state menjadi enable lagi
                                btn_signin.setEnabled(true);
                                btn_signin.setText("SIGN IN");
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "username tidak ada", Toast.LENGTH_SHORT).show();

                            //ubah state menjadi enable lagi
                            btn_signin.setEnabled(true);
                            btn_signin.setText("SIGN IN");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                btn_create_new_acc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent gotochooserole = new Intent(SignInTeacherActivity.this, RegisterTeacherOneActivity.class);
                        startActivity(gotochooserole);
                    }
                });
            }
        });

    }
}
