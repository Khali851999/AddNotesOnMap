package com.example.easypg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddPost extends AppCompatActivity {

    DatabaseReference mref;
    StorageReference mStorage;
    Button post;
    EditText postTitle;
    EditText postDescription;
    ImageView imageView;
    Uri postimageuri;
    ProgressDialog progressDialog;
    private final static int GALLERY_INTENT=2;
    LatLng ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mStorage= FirebaseStorage.getInstance().getReference();
        mref= FirebaseDatabase.getInstance().getReference().child("POSTS");
        postTitle=findViewById(R.id.postTitle);
        postDescription=findViewById(R.id.post_description);
        post=findViewById(R.id.post_button);
        imageView=findViewById(R.id.addphotopost);
        progressDialog=new ProgressDialog(this);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = postTitle.getText().toString();
                final String post_des = postDescription.getText().toString();

                Bundle bundle = getIntent().getExtras();
                final String LatiLongi = bundle.getString("LatitudeLongitude");

                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(post_des) && postimageuri!=null) {
                    progressDialog.setMessage("Posting");
                    progressDialog.show();
                    final StorageReference filepath = mStorage.child("Blog_image")
                            .child(postimageuri.getLastPathSegment());
                    filepath.putFile(postimageuri).continueWithTask(new Continuation<UploadTask.TaskSnapshot,
                            Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful())
                            {
                                throw task.getException();
                            }
                            return filepath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful())
                            {
                                Uri uri =task.getResult();
                                DatabaseReference newpost=mref.push();
                                newpost.child("LatLong").setValue(LatiLongi);
                                newpost.child("Title").setValue(title);
                                newpost.child("Descript").setValue(post_des);
                                newpost.child("ImageUrl").setValue(uri.toString());

                                progressDialog.dismiss();
                                Toast.makeText(AddPost.this, "Posted", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(AddPost.this,MapsActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_INTENT){
            postimageuri=data.getData();
            imageView.setImageURI(postimageuri);
        }
    }
}

