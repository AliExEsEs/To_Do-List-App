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

public class SignUpActivity extends AppCompatActivity {

    EditText Name, Email, pass1, pass2;

    NeumorphButton SignUp;
    TextView LoginText;

    ProgressDialog mProgress;

    FirebaseAuth SignUpAuth;
    FirebaseUser CurrentUser;
    FirebaseAuth.AuthStateListener mListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Utils.blackStatusBar(this, R.color.m_white);
        mProgress = new ProgressDialog(this);


        LoginText = findViewById(R.id.signup_to_login_shift);
        SignUp = findViewById(R.id.Sign_up_btn);

        Name = findViewById(R.id.Signup_Name);
        Email = findViewById(R.id.Sigmup_Email);
        pass1 = findViewById(R.id.Signup_Password1);
        pass2 = findViewById(R.id.Signup_Password2);

        SignUpAuth = FirebaseAuth.getInstance();
        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();


        LoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                // finish();
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignUpProcess();
                mProgress.setMessage("We love you, please wait");
                mProgress.show();
            }
        });
    }

    private void SignUpProcess() {

        String UserName = Name.getText().toString().toString();
        String UserEMail = Email.getText().toString().toString();
        String P1 = pass1.getText().toString().trim();
        String P2 = pass2.getText().toString().trim();

        if (TextUtils.isEmpty(UserName) || TextUtils.isEmpty(UserEMail) || TextUtils.isEmpty(P1) || TextUtils.isEmpty(P2))
        {
            Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
            mProgress.dismiss();
        }
        else if (!P1.equals(P2))
        {
            Toast.makeText(this, "Passwords are not same", Toast.LENGTH_SHORT).show();
            mProgress.dismiss();
        }

        else if (!TextUtils.isEmpty(UserName) || !TextUtils.isEmpty(UserEMail) || !TextUtils.isEmpty(P1) || !TextUtils.isEmpty(P2))
        {

            SignUpAuth.createUserWithEmailAndPassword(UserEMail, P1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful())
                    {
                        Toast.makeText(SignUpActivity.this, "Account Succesfully created", Toast.LENGTH_SHORT).show();
                        Intent i =  new Intent(SignUpActivity.this, HomePageActivity.class);
                        startActivity(i);
                        mProgress.dismiss();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }

                    else if (!task.isSuccessful())
                    {
                        Toast.makeText(SignUpActivity.this, "Error "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        mProgress.dismiss();
                        pass1.setText("");
                        pass2.setText("");
                    }

                }
            });

        }

    }
}