package com.youthfimodd.elenges;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.youthfimodd.elenges.custom.chat_model.Users;
import com.youthfimodd.elenges.database.DatabaseHelper;

import java.util.HashMap;

public class ProfilActivity extends AppCompatActivity {
    //DateBase SQLite
    DatabaseHelper myDb;
    //fire base
    DatabaseReference mUserDatabase;
    FirebaseUser mCurrentUser;
    StorageReference storageReference;
    private StorageTask uploadTask;
    private ProgressDialog mProgress;
    //
    private ImageView image_profil;
    private Uri ImageUrl;
    //
    private static final int GALLERY_PICK = 1;
    //
    private TextView DisplayAge, DisplayName, DisplaySex, DisplayNumber, DisplayVille, DisplayEmail, DisplayUserType, txtModifPr;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        //Toolbar
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //SQLite
        myDb = new DatabaseHelper(this);
        //lien avec xml
        txtModifPr = findViewById(R.id.txt_ModifPr);
        image_profil = findViewById(R.id.setting_image_profil);

        mProgress = new ProgressDialog(this);
        //
        DisplayAge = findViewById(R.id.profil_date_nais);
        DisplayName = findViewById(R.id.textname);
        DisplaySex = findViewById(R.id.profil_genre);
        DisplayNumber = findViewById(R.id.textnumber);
        DisplayVille = findViewById(R.id.textVille);
        DisplayEmail = findViewById(R.id.txtmail);
        DisplayUserType = findViewById(R.id.type_user);
        progressBar = findViewById(R.id.progressBar2);
        //
       progressBar.setVisibility(View.VISIBLE);
        //store picture in fire base
        storageReference = FirebaseStorage.getInstance().getReference().child("profile_image");
        //fire base
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        //
        String current_uid = mCurrentUser.getUid();
        //
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //
                String Email = dataSnapshot.child("email").getValue().toString();
                String Age = dataSnapshot.child("age").getValue().toString();
                String Name = dataSnapshot.child("name").getValue().toString();
                String Sex = dataSnapshot.child("sexe").getValue().toString();
                String Phone = dataSnapshot.child("phone").getValue().toString();
                String Ville = dataSnapshot.child("ville").getValue().toString();
                String TUser = dataSnapshot.child("typeusesr").getValue().toString();
                String Image = dataSnapshot.child("image").getValue().toString();

                DisplayName.setText(Name);
                DisplaySex.setText(Sex);
                DisplayAge.setText(Age);
                DisplayNumber.setText(Phone);
                DisplayVille.setText(Ville);
                DisplayEmail.setText(Email);
                DisplayUserType.setText(TUser);
                //
                Users user = dataSnapshot.getValue(Users.class);
                if (user.getImage().equals("default")){
                    image_profil.setImageResource(R.mipmap.ic_launcher_profil2);

                }else {
                    Picasso.with(getApplicationContext()).load(Image).into(image_profil);
                    //Insertion();

                }
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //
        //Read();
        //
        image_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                openImage();
            }
        });
        //
        txtModifPr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Intent Modifintent = new Intent(getApplicationContext(), ModifActivity.class);
                startActivity(Modifintent);
            }
        });
    }
    //
    private void openImage() {
        //
        Intent gallery_intent = new Intent();
        gallery_intent.setType("image/*");
        gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery_intent, "SELECT IMAGE"), GALLERY_PICK);
    }
    //
    private String getFileExtention(Uri uri){
        //
        ContentResolver contentResolver = ProfilActivity.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    //
    private void uplaodImage(){
        //
        final ProgressDialog  pb= new ProgressDialog(this);
        pb.setMessage("Uploading");
        pb.setCanceledOnTouchOutside(false);
        pb.show();

        if (ImageUrl != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtention(ImageUrl));
            uploadTask = fileReference.putFile(ImageUrl);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>(){
                @Override
                public Task<Uri> then (@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    //
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String  mUri = downloadUri.toString();

                        mUserDatabase = FirebaseDatabase.getInstance().getReference("Users").child(mCurrentUser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("image", mUri);
                        mUserDatabase.updateChildren(map);

                        pb.dismiss();
                    }else {
                        Toast.makeText(ProfilActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        pb.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //
                    Toast.makeText(ProfilActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pb.dismiss();
                }
            });
        }else {
            Toast.makeText(ProfilActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
    //
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            ImageUrl = data.getData();

            CropImage.activity(ImageUrl)
                    .setAspectRatio(1, 1)
                    .start(ProfilActivity.this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK){

                if (uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(ProfilActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                }else {
                    uplaodImage();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }

        }
    }
    //Option memu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
