package com.example.to_dolistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.to_dolistapp.supportNotificationManager.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import soup.neumorphism.NeumorphButton;

public class LoginActivity extends AppCompatActivity {

    NeumorphButton mButton;
    TextView Sign_up_text, ForgotShift;
    EditText loginEmail, loginPass;

    FirebaseAuth LoginAuth;
    FirebaseUser CurrentUser;
    FirebaseAuth.AuthStateListener mListner;

    ProgressDialog mProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Utils.blackStatusBar(this, R.color.m_white);
        mProcess = new ProgressDialog(this);

        Sign_up_text = findViewById(R.id.sign_up_shift);
        mButton = findViewById(R.id.Login_Btn);
        loginEmail = findViewById(R.id.Login_email);
        loginPass = findViewById(R.id.Login_Pass);

        ForgotShift = findViewById(R.id.forgot_password_shift);

        ForgotShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(i);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        LoginAuth = FirebaseAuth.getInstance();

        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (CurrentUser != null) {
            // User is signed in
            Intent i = new Intent(LoginActivity.this, HomePageActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }

        Sign_up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                //finish();
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StartLogin();
                mProcess.setMessage("Loging In");


            }
        });

    }

    private void StartLogin() {

        mProcess.show();

        String Email = loginEmail.getText().toString().trim();
        String Password = loginPass.getText().toString().trim();


        if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password)) {
            mProcess.dismiss();
            Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();

        }
        else
        {
            LoginAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, HomePageActivity.class);
                        startActivity(i);
                        mProcess.dismiss();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();

                    } else if (!task.isSuccessful()) {
                        mProcess.dismiss();
                        Toast.makeText(LoginActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        loginPass.setText("");

                    }

                }
            });
        }

    }
}