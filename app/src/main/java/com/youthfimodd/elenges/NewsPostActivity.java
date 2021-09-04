package com.youthfimodd.elenges;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.youthfimodd.elenges.custom.chat_model.Users;

public class NewsPostActivity extends AppCompatActivity {
    //
    private ImageView img_users_post, imageBtn, add_image, cancel_image, img_tout_le_monde;
    private TextView txt_users_post, modif_image, txt_tout_le_monde;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;
    private Uri uri;
    private EditText textDesc;
    private Button postBtn;
    String etat = "identite";
    String coordLat, coordLog;

    private LinearLayout line_post, ln_tout_le_monde;
    ;
    BottomSheetDialog bottomsheet_tou_le_monde;
    RadioButton rb_tout_le_monde, rb_anonyme;
    private StorageReference storage;
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    //Progress
    private ProgressDialog mProgress;
    ProgressBar new_post_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_post);
        //Toolbar
        getSupportActionBar().setTitle("Nouvelle publication");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*-progress-*/
        new_post_progress = findViewById(R.id.new_post_progress);
        new_post_progress.setVisibility(View.GONE);
        mProgress = new ProgressDialog(NewsPostActivity.this);
        //fire base
        storage = FirebaseStorage.getInstance().getReference();
        databaseRef = database.getInstance().getReference().child("Blog");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        //lien with xml
        img_tout_le_monde = findViewById(R.id.img_tout_le_monde);
        txt_tout_le_monde = findViewById(R.id.txt_tout_le_monde);
        img_users_post = findViewById(R.id.img_users_post);
        imageBtn = findViewById(R.id.new_post_image);
        add_image = findViewById(R.id.add_image);
        cancel_image = findViewById(R.id.img_cancel);
        modif_image = findViewById(R.id.txt_modifier);
        txt_users_post = findViewById(R.id.txt_users_post);
        textDesc = findViewById(R.id.editText);
        postBtn = findViewById(R.id.post_btn);
        line_post = findViewById(R.id.line_post);
        line_post.setVisibility(View.INVISIBLE);
        ln_tout_le_monde = findViewById(R.id.ln_tout_le_monde);
        ln_tout_le_monde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomDialogTouLemonde();
            }
        });
        /*-recuperer nom user-*/
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String NameUser = snapshot.child("name").getValue().toString();
                String ImageUser = snapshot.child("image").getValue().toString();

                txt_users_post.setText(NameUser);
                //
                Users user = snapshot.getValue(Users.class);
                if (user.getImage().equals("default")) {
                    img_users_post.setImageResource(R.mipmap.ic_launcher_profil2);

                } else {
                    Picasso.with(getApplicationContext()).load(ImageUser).into(img_users_post);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*--*/
        add_image.setOnClickListener(view -> {
            //
            Import_Image();
        });
        //
        modif_image.setOnClickListener(v -> Import_Image());
        cancel_image.setOnClickListener(v -> {
            line_post.setVisibility(View.INVISIBLE);
            imageBtn.setVisibility(View.INVISIBLE);
        });
        //
        //
        postBtn.setOnClickListener(view -> {

            final String PostDesc = textDesc.getText().toString().trim();

            if (TextUtils.isEmpty(PostDesc) && uri == null) {

                textDesc.setError("Ajouter la description!");

            } /*else if (!TextUtils.isEmpty(PostDesc) && uri == null) {
                //Progress
                mProgress.setTitle("POSTING...");
                mProgress.setMessage("Patientez s'il vous plait!");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();

                postTextData(PostDesc);

            }*/ else if (!TextUtils.isEmpty(PostDesc) && uri != null) {
                //Progress
                mProgress.setTitle("POSTING...");
                mProgress.setMessage("Patientez s'il vous plait!");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();

                postImageData(PostDesc);
            }
        });
    }

    private void postImageData(String postDesc) {

        new_post_progress.setVisibility(View.VISIBLE);
        //
        final StorageReference filepath = storage.child("post_images").child(uri.getLastPathSegment());
        /*recuperer l'image postée et ses details*/
        filepath.putFile(uri).addOnSuccessListener(taskSnapshot -> filepath.getDownloadUrl().addOnSuccessListener(uri -> {

            Toast.makeText(getApplicationContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();

            final DatabaseReference newPost = databaseRef.push();
            //adding post contents to database reference
            mDatabaseUsers.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Object timestamps = ServerValue.TIMESTAMP;

                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(10000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                    if (ActivityCompat.checkSelfPermission(NewsPostActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NewsPostActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    LocationServices.getFusedLocationProviderClient(getApplicationContext())
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    super.onLocationResult(locationResult);
                                    LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                            .removeLocationUpdates(this);
                                    if (locationResult != null && locationResult.getLocations().size() > 0) {
                                        int latestLocationIndex = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                                        double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                                        coordLat = (String.valueOf(latitude));
                                        coordLog = (String.valueOf(longitude));

                                        Location location = new Location("providerNA");
                                        location.setLatitude(latitude);
                                        location.setLongitude(longitude);
                                        //fetchAddressFromLatLong(location);
                                        newPost.child("desc").setValue(postDesc);
                                        newPost.child("imageUrl").setValue(uri.toString());
                                        newPost.child("uid").setValue(mCurrentUser.getUid());
                                        newPost.child("timestamp").setValue(timestamps);
                                        newPost.child("status").setValue("textimage");
                                        newPost.child("latitude").setValue(coordLat);
                                        newPost.child("longitude").setValue(coordLog);
                                        newPost.child("userImg").setValue(dataSnapshot.child("image").getValue());
                                        newPost.child("username").setValue(dataSnapshot.child("name").getValue())

                                                .addOnCompleteListener(task -> {

                                                    if (task.isSuccessful()) {
                                                        finish();
                                                    }
                                                });
                                    }
                                }
                            }, Looper.getMainLooper());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }));
    }

    private void postTextData(String postDesc) {

        final DatabaseReference newPost = databaseRef.push();
        //adding post contents to database reference
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Object timestamps = ServerValue.TIMESTAMP;

                LocationRequest locationRequest = new LocationRequest();
                locationRequest.setInterval(10000);
                locationRequest.setFastestInterval(3000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                if (ActivityCompat.checkSelfPermission(NewsPostActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NewsPostActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                LocationServices.getFusedLocationProviderClient(getApplicationContext())
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                LocationServices.getFusedLocationProviderClient(getApplicationContext())
                                        .removeLocationUpdates(this);
                                if (locationResult != null && locationResult.getLocations().size() > 0) {
                                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                                    double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                                    double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                                    coordLat = (String.valueOf(latitude));
                                    coordLog = (String.valueOf(longitude));

                                    Location location = new Location("providerNA");
                                    location.setLatitude(latitude);
                                    location.setLongitude(longitude);
                                    //fetchAddressFromLatLong(location);
                                    newPost.child("desc").setValue(postDesc);
                                    newPost.child("uid").setValue(mCurrentUser.getUid());
                                    newPost.child("timestamp").setValue(timestamps);
                                    newPost.child("status").setValue("text");
                                    newPost.child("latitude").setValue(coordLat);
                                    newPost.child("longitude").setValue(coordLog);
                                    newPost.child("userImg").setValue(dataSnapshot.child("image").getValue());
                                    newPost.child("username").setValue(dataSnapshot.child("name").getValue())

                                            .addOnCompleteListener(task -> {

                                                if (task.isSuccessful()){
                                                    mProgress.dismiss();
                                                    finish();
                                                }
                                            });
                                }
                            }
                        }, Looper.getMainLooper());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void openBottomDialogTouLemonde() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_dialog_tout_le_monde,null);
        bottomsheet_tou_le_monde = new BottomSheetDialog(this);
        bottomsheet_tou_le_monde.setContentView(view);
        rb_anonyme = view.findViewById(R.id.rb_anonyme);
        rb_tout_le_monde = view.findViewById(R.id.rb_tout_le_monde);

        rb_anonyme.setOnClickListener(v -> {
            rb_anonyme.isChecked();
            etat = "anonymat";
            txt_tout_le_monde.setText("Anonymat");
            img_tout_le_monde.setImageResource(R.drawable.ic_baseline_public_off_24);
            bottomsheet_tou_le_monde.dismiss();
        });
        rb_tout_le_monde.setOnClickListener(v -> {
            rb_tout_le_monde.isChecked();
            etat = "identite";
            txt_tout_le_monde.setText("Tout le monde");
            img_tout_le_monde.setImageResource(R.drawable.ic_public);
            bottomsheet_tou_le_monde.dismiss();
        });
        bottomsheet_tou_le_monde.show();
    }
    /*-Import Image-*/
    private void Import_Image() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Select Image"), IMAGE_PICK_GALLERY_CODE);
    }
    //prepare notification

    @Override
    // image from gallery result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){

            if (requestCode == IMAGE_PICK_GALLERY_CODE){

                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }

            if (requestCode == IMAGE_PICK_CAMERA_CODE){

                CropImage.activity(uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                uri = result.getUri();
                imageBtn.setImageURI(uri);
                line_post.setVisibility(View.VISIBLE);
                imageBtn.setVisibility(View.VISIBLE);
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
    //Notification
    private void PostNotification(){

    }
}
