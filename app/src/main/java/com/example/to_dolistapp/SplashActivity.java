package com.example.to_dolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.to_dolistapp.supportNotificationManager.Utils;

public class SplashActivity extends AppCompatActivity {

    TextView Luna, Desc;
    ImageView logoImage;
    Animation logo_anim, luna_anim, desc_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.blackStatusBar(this, R.color.m_white);

        logoImage =findViewById(R.id.imageView);
        Luna = findViewById(R.id.textView);
        Desc = findViewById(R.id.textView3);

        logo_anim = AnimationUtils.loadAnimation(this, R.anim.logo_dropper);
        luna_anim = AnimationUtils.loadAnimation(this, R.anim.luna_anim);
        desc_anim = AnimationUtils.loadAnimation(this, R.anim.desc_anim);

        logoImage.startAnimation(logo_anim);
        Luna.startAnimation(luna_anim);
        Desc.startAnimation(desc_anim);


        // Here i am starting the animatyion fo the splash screen, basically the intent inside the runable
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i =  new Intent(SplashActivity.this, LoginActivity.class);
               // Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(MainActivity.this,android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, 2100);
    }
}