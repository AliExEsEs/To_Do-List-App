package com.example.to_dolistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.example.to_dolistapp.ModelClasses.FirebaseModel;
import com.example.to_dolistapp.supportNotificationManager.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class NotesDetailedActivity extends AppCompatActivity {

    TextView Title, Date, Description;
    Button EditButton, DeleteButton;

    FirebaseDatabase RealtimeDatabase;
    FirebaseAuth myAuth;
    DatabaseReference myRefrence;

    ProgressDialog popperDialog;

    String CurrentUID;

    String name, description, date, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_detailed);

        Utils.blackStatusBar(this, R.color.m_white);

        popperDialog = new ProgressDialog(this);

         name = getIntent().getStringExtra("title");
         description = getIntent().getStringExtra("desc");
         date = getIntent().getStringExtra("date");
         id = getIntent().getStringExtra("id");

        //Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();

        //Toast.makeText(this, "Congrattulations " + name + description + date, Toast.LENGTH_SHORT).show();

        CurrentUID = FirebaseAuth.getInstance().getUid();


        myRefrence = FirebaseDatabase.getInstance().getReference().child("tasks").child(CurrentUID);

        Title = findViewById(R.id.card_title);
        Date = findViewById(R.id.card_date);
        Description = findViewById(R.id.card_desc);

        EditButton = findViewById(R.id.edit_button);
        DeleteButton = findViewById(R.id.delete_button);

        Title.setText(name);
        Date.setText(date);
        Description.setText(description);

        EditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditPosts();

            }
        });

        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeletePosts();
                
            }
        });


    }

    private void DeletePosts() {

        myRefrence.child(id).removeValue();
        Intent i = new Intent(NotesDetailedActivity.this, HomePageActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
        Toast.makeText(this, "Post Deleted", Toast.LENGTH_SHORT).show();
        
    }

    private void EditPosts() {

        AlertDialog.Builder mDailog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View popperView = inflater.inflate(R.layout.fba_create_post, null);
        mDailog.setView(popperView);

        AlertDialog popperDialog = mDailog.create();
        popperDialog.setCancelable(true);
        popperDialog.show();

        EditText title = popperView.findViewById(R.id.Popper_title);
        EditText Description = popperView.findViewById(R.id.Popper_desc);

        title.setText(name);
        Description.setText(description);

        Button Save = popperView.findViewById(R.id.popper_save_btn);
        Button Cancel = popperView.findViewById(R.id.popper_cancel_btn);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popperDialog.dismiss();
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String UploadTitle = title.getText().toString().trim();
                String UploadDesc = Description.getText().toString().trim();
                String ID = id;
                String DATE = date;

                FirebaseModel modelClass = new FirebaseModel(UploadTitle, UploadDesc, ID, DATE);

                if (TextUtils.isEmpty(UploadTitle) || TextUtils.isEmpty(UploadDesc)) {
                    Toast.makeText(NotesDetailedActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                } else {

                    myRefrence = FirebaseDatabase.getInstance().getReference().child("tasks").child(CurrentUID);
                    myRefrence.child(ID).setValue(modelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(NotesDetailedActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
                                popperDialog.dismiss();

                                Intent i = new Intent(NotesDetailedActivity.this, HomePageActivity.class);
                                startActivity(i);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();

                            } else {
                                Toast.makeText(NotesDetailedActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                popperDialog.dismiss();
                                Intent i = new Intent(NotesDetailedActivity.this, HomePageActivity.class);
                                startActivity(i);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            }

                        }
                    });


                }

            }
        });


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(this, HomePageActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}