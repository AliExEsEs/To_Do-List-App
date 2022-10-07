package com.example.to_dolistapp.firebaseAdapter;

import static com.example.to_dolistapp.R.id.card_date;
import static com.example.to_dolistapp.R.id.card_title;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_dolistapp.HomePageActivity;
import com.example.to_dolistapp.ModelClasses.FirebaseModel;
import com.example.to_dolistapp.NotesDetailedActivity;
import com.example.to_dolistapp.R;
import com.example.to_dolistapp.interfaceClasses.OnClickListnerInterface;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

import soup.neumorphism.NeumorphCardView;

public class FirebaseAdapter extends FirebaseRecyclerAdapter<FirebaseModel, FirebaseAdapter.ourViewHolder> {


    Context ourContext;
    ArrayList<FirebaseModel> ourArrayModel;
    OnClickListnerInterface ClickListner;
    FirebaseRecyclerOptions<FirebaseModel> options;

    public FirebaseAdapter(Context ourContext, FirebaseRecyclerOptions<FirebaseModel> options, OnClickListnerInterface ClickListner) {
        super(options);
        this.ourContext = ourContext;
        this.ClickListner = ClickListner;
        this.options = options;
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ourViewHolder holder, int position, FirebaseModel model) {

        RecyclerAnimation(holder.itemView, position);

        holder.Title.setText(model.getTitle());
        holder.Date.setText(model.getDate());
        holder.Desc.setText(model.getDescription());

        holder.MainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClickListner.ItemCLicked(options.getSnapshots().get(position));
            }
        });


    }

    @NonNull
    @Override
    public ourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View ourView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_rec_card, parent, false);
        return new ourViewHolder(ourView);
    }

    public static class ourViewHolder extends RecyclerView.ViewHolder {

        TextView Title, Date, Desc;
        NeumorphCardView MainCard;

        public ourViewHolder(View itemView) {
            super(itemView);

            Title = itemView.findViewById(card_title);
            Date = itemView.findViewById(card_date);
            Desc = itemView.findViewById(R.id.card_desc);
            MainCard = itemView.findViewById(R.id.mainCard);
        }
    }

    private void RecyclerAnimation(View ViewToAnimate, int position) {

        Animation SlideIn = AnimationUtils.loadAnimation(ourContext, android.R.anim.slide_in_left);

        ViewToAnimate.startAnimation(SlideIn);

    }


}
