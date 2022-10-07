package com.example.to_dolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.to_dolistapp.supportNotificationManager.Utils;
import com.google.firebase.auth.FirebaseAuth;

import soup.neumorphism.NeumorphButton;

public class ProfileActivity extends AppCompatActivity {

    ImageView HomeImage, ProfileImage;
    Button Home, Profile;
    TextView user_email;

    NeumorphButton Signout;

    FirebaseAuth ProfileAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Utils.blackStatusBar(this, R.color.m_white);

        HomeImage = findViewById(R.id.home_vector_white);
        ProfileImage = findViewById(R.id.profile_vector_white);
        user_email = findViewById(R.id.profile_email);

        ProfileAuth = FirebaseAuth.getInstance();

        String Current_email = ProfileAuth.getCurrentUser().getEmail();

        user_email.setText(Current_email);

        Home = findViewById(R.id.home);
        Profile = findViewById(R.id.profile);

        Signout = findViewById(R.id.Sign_out);

        Signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();

                Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeImage.setAlpha(1f);
                ProfileImage.setAlpha(.6f);


                Intent i = new Intent(ProfileActivity.this, HomePageActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileImage.setAlpha(1f);
                HomeImage.setAlpha(.6f);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent  i = new Intent(ProfileActivity.this,HomePageActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

}