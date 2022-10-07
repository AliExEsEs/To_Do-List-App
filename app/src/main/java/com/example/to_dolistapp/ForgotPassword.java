package com.example.to_dolistapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.to_dolistapp.firebaseAdapter.FirebaseAdapter;
import com.example.to_dolistapp.supportNotificationManager.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import soup.neumorphism.NeumorphButton;

public class ForgotPassword extends AppCompatActivity {

    FirebaseAuth ResetAuth;

    EditText ResetEmail;
    NeumorphButton ResetButton;
    TextView BackToLogin;
    ImageView GoBack;

    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Utils.blackStatusBar(this, R.color.m_white);

        ResetAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);


        ResetEmail = findViewById(R.id.Reset_Email);
        ResetButton = findViewById(R.id.ResetButton);
        BackToLogin = findViewById(R.id.back_to_login);
        GoBack = findViewById(R.id.back_image);


        GoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        ResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String e = ResetEmail.getText().toString().trim();

                if(TextUtils.isEmpty(e))
                {
                    Toast.makeText(ForgotPassword.this, "Enter Email", Toast.LENGTH_SHORT).show();

                }
                mProgress.setMessage("We love you, please wait");
                mProgress.show();

                ResetPassword();
            }
        });

        BackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


    }


    private void ResetPassword() {

        String reset_email = ResetEmail.getText().toString().toString();



        ResetAuth.sendPasswordResetEmail(reset_email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    mProgress.dismiss();

                    Toast.makeText(getApplicationContext(), "Check Your EMail, also the spam folder " + ("\ud83d\ude01"), Toast.LENGTH_LONG).show();

                    Intent m = new Intent(ForgotPassword.this, LoginActivity.class);
                    startActivity(m);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {

                    Toast.makeText(getApplicationContext(), "Error Occured " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    mProgress.dismiss();
                }
            }
        });

    }


}