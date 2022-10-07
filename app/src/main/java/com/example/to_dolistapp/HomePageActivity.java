package com.example.to_dolistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.to_dolistapp.ModelClasses.FirebaseModel;
import com.example.to_dolistapp.firebaseAdapter.FirebaseAdapter;
import com.example.to_dolistapp.interfaceClasses.OnClickListnerInterface;
import com.example.to_dolistapp.supportNotificationManager.Utils;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

import io.reactivex.rxjava3.internal.subscribers.LambdaSubscriber;

public class HomePageActivity extends AppCompatActivity implements OnClickListnerInterface {

    RecyclerView mRec;
    FirebaseAdapter myAdapter;

    ImageView HomeImage, ProfileImage;
    Button Home, Profile;

    FirebaseUser CurrentUser;
    DatabaseReference reference;

    String onlineUserId;

    String id;

    ProgressDialog mProgress;

    FloatingActionButton CreatePost;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Utils.blackStatusBar(this, R.color.m_white);



        mRec = findViewById(R.id.recView);
        //mRec.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRec.setLayoutManager(linearLayoutManager);


        // Here is the code for tbe firebase recycleriew Adapter and all that shit

        CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        onlineUserId = FirebaseAuth.getInstance().getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("tasks").child(onlineUserId);
        id = reference.push().getKey();


        FirebaseRecyclerOptions<FirebaseModel> options = new FirebaseRecyclerOptions.Builder<FirebaseModel>().setQuery(reference, FirebaseModel.class).build();

        myAdapter = new FirebaseAdapter(this, options, this);

        mRec.setAdapter(myAdapter);


        mProgress = new ProgressDialog(this);

        HomeImage = findViewById(R.id.home_vector_white);
        ProfileImage = findViewById(R.id.profile_vector_white);

        Home = findViewById(R.id.home);
        Profile = findViewById(R.id.profile);

        CreatePost = findViewById(R.id.floatingActionButton);


        CreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddTask();

            }
        });

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeImage.setAlpha(1f);
                ProfileImage.setAlpha(.6f);

            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileImage.setAlpha(1f);
                HomeImage.setAlpha(.6f);

                Intent i = new Intent(HomePageActivity.this, ProfileActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

    }


    

    private void AddTask() {

        AlertDialog.Builder mDailog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View popperView = inflater.inflate(R.layout.fba_create_post, null);
        mDailog.setView(popperView);

        AlertDialog popperDialog = mDailog.create();
        popperDialog.setCancelable(true);
        popperDialog.show();

        EditText title = popperView.findViewById(R.id.Popper_title);
        EditText description = popperView.findViewById(R.id.Popper_desc);

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
                String UploadDesc = description.getText().toString().trim();
                String date = DateFormat.getDateInstance().format(new Date());

                FirebaseModel modelClass = new FirebaseModel(UploadTitle, UploadDesc, id, date);

                if (TextUtils.isEmpty(UploadTitle) || TextUtils.isEmpty(UploadDesc)) {
                    Toast.makeText(HomePageActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    reference.child(id).setValue(modelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(HomePageActivity.this, "Note Uploaded", Toast.LENGTH_SHORT).show();
                                popperDialog.dismiss();

                                LinearLayoutManager layoutManager = (LinearLayoutManager) mRec.getLayoutManager();
                                layoutManager.scrollToPositionWithOffset(layoutManager.findLastVisibleItemPosition(), layoutManager.findLastVisibleItemPosition());

                            } else {
                                Toast.makeText(HomePageActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                popperDialog.dismiss();
                            }

                        }
                    });



                }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        myAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
       // myAdapter.stopListening();
    }



    @Override
    public void ItemCLicked(FirebaseModel MyModel) {

        //Toast.makeText(this, "Well atleast we got onclick covered", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(HomePageActivity.this, NotesDetailedActivity.class);
        i.putExtra("title", MyModel.getTitle());
        i.putExtra("desc",MyModel.getDescription());
        i.putExtra("date", MyModel.getDate());
        i.putExtra("id",MyModel.getId());
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();


    }



}
