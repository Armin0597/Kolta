package com.koltaapp.kolta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseRoleActivity extends AppCompatActivity {

    Button btn_student;
    Button btn_teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);

        btn_student = findViewById(R.id.btn_student);
        btn_teacher = findViewById(R.id.btn_teacher);

        btn_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregisterstud = new Intent(ChooseRoleActivity.this, RegisterStudentOneActivity.class);
                startActivity(gotoregisterstud);
            }
        });

        btn_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregisterteach = new Intent(ChooseRoleActivity.this, RegisterTeacherOneActivity.class);
                startActivity(gotoregisterteach);
            }
        });
    }
}
