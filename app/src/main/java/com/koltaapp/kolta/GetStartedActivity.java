package com.koltaapp.kolta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class GetStartedActivity extends AppCompatActivity {

    Button btn_signin,btn_newaccount;
    Animation bottom_to_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        //load animation
        bottom_to_up = AnimationUtils.loadAnimation(this,R.anim.bottom_to_top);

        //load element
        btn_signin = findViewById(R.id.btn_signin);
        btn_newaccount = findViewById(R.id.btn_newaccount);

        //run animation
        btn_signin.startAnimation(bottom_to_up);
        btn_newaccount.startAnimation(bottom_to_up);



        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosignin = new Intent(GetStartedActivity.this, ChooseLoginRoleActivity.class);
                startActivity(gotosignin);
            }
        });

        btn_newaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotochooserole = new Intent(GetStartedActivity.this, ChooseRoleActivity.class);
                startActivity(gotochooserole);
            }
        });
    }
}
