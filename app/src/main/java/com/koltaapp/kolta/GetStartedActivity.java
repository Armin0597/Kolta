package com.koltaapp.kolta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class GetStartedActivity extends AppCompatActivity {

    Button btn_signin,btn_newaccount;
    ImageView get_started_img;
    Animation bottom_to_up, top_to_bottom,app_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        //load animation
        top_to_bottom = AnimationUtils.loadAnimation(this,R.anim.top_to_bottom);
        bottom_to_up = AnimationUtils.loadAnimation(this,R.anim.bottom_to_top);
        app_splash = AnimationUtils.loadAnimation(this,R.anim.app_splash);

        //load element
        btn_signin = findViewById(R.id.btn_signin);
        btn_newaccount = findViewById(R.id.btn_newaccount);
        get_started_img = findViewById(R.id.get_started_img);

        //run animation

        get_started_img.startAnimation(app_splash);
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
