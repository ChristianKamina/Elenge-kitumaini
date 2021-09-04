package com.youthfimodd.elenges;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class NewPostTextActivity extends AppCompatActivity {

    private EditText txt_add_post;
    private FloatingActionButton btn_send_text_post;
    String coordLat, coordLog;

    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post_text);

        txt_add_post = findViewById(R.id.txt_add_post);
        btn_send_text_post = findViewById(R.id.float_post_text_btn);

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Blog");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        txt_add_post.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0){
                    btn_send_text_post.setVisibility(View.GONE);

                }else {
                    btn_send_text_post.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                btn_send_text_post.setVisibility(View.GONE);
            }
        });
        btn_send_text_post.setOnClickListener(v -> {
            String text_post = txt_add_post.getText().toString().trim();
            if (!TextUtils.isEmpty(text_post)){
                post_text(text_post);
            }
        });
    }

    private void post_text(String text_post) {
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

                if (ActivityCompat.checkSelfPermission(NewPostTextActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NewPostTextActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                                    newPost.child("desc").setValue(text_post);
                                    newPost.child("uid").setValue(mCurrentUser.getUid());
                                    newPost.child("timestamp").setValue(timestamps);
                                    newPost.child("status").setValue("text");
                                    newPost.child("latitude").setValue(coordLat);
                                    newPost.child("longitude").setValue(coordLog);
                                    newPost.child("userImg").setValue(dataSnapshot.child("image").getValue());
                                    newPost.child("username").setValue(dataSnapshot.child("name").getValue())

                                            .addOnCompleteListener(task -> {

                                                if (task.isSuccessful()){
                                                    Toast.makeText(NewPostTextActivity.this, "Poster avec succès!", Toast.LENGTH_SHORT).show();
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
}