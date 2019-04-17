package com.example.easypg;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easypg.model.PostBlog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class mapsempty extends AppCompatActivity {
    private static final String TAG = "mapsempty";
    private static final int ERROR_DIALOG_REQUEST=9001;
    RecyclerView recyclerView;
    DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapsempty);
        Log.d(TAG, "onCreate: oncreate mapsempty");
        if (isServicesok())
        { init();}
        Log.d(TAG, "onCreate: services ok");


        mref= FirebaseDatabase.getInstance().getReference().child("POSTS");
        recyclerView=findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(mapsempty.this));
    }

    private void init() {
        Button tomaps=findViewById(R.id.tomaps);
        tomaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mapsempty.this,MapsActivity.class);
                startActivity(intent);
                Log.d(TAG, "onClick: moving to map activity");
            }
        });

    }

    public boolean isServicesok() {
        Log.d(TAG, "isServicesok: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mapsempty.this);

        if (available == ConnectionResult.SUCCESS) {//everything is fine till here
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Dialog dialog=GoogleApiAvailability.getInstance().
                    getErrorDialog(this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else Toast.makeText(this, "You cant make make request", Toast.LENGTH_SHORT).show();
        return false;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        View view;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }
        private void setPost(String title,String postDescription,String imageURL){
            TextView posttitle=view.findViewById(R.id.posttitlerecyclerview);
            TextView postdescp=view.findViewById(R.id.postdec);
            ImageView imageView=view.findViewById(R.id.dtunewimage);
            postdescp.setText(postDescription);
            posttitle.setText(title);
            Picasso.get().load(imageURL).into(imageView);


        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<PostBlog,PostViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<PostBlog, PostViewHolder>(
                PostBlog.class,
                R.layout.cardviewlayout,
                PostViewHolder.class,
                mref
        ) {
            @Override
            protected void populateViewHolder(PostViewHolder viewHolder, PostBlog model, int position) {
                viewHolder.setPost(model.getTitle(),model.getDescript(),model.getImageUrl());
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}

