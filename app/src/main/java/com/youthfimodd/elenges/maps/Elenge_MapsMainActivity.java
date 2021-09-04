package com.youthfimodd.elenges.maps;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.youthfimodd.elenges.R;

public class Elenge_MapsMainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private Uri uri;
    ImageView img_loc;
    //Progress
    private ProgressDialog mProgress;
    private StorageReference storage;
    private DatabaseReference databaseReference, mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    String id_local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elenge__maps_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*Toolbar
        getSupportActionBar().setTitle("Elenge Maps");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        mProgress = new ProgressDialog(Elenge_MapsMainActivity.this);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Maps_Coordinates");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        id_local = databaseReference.push().get().toString();

        storage = FirebaseStorage.getInstance().getReference();

        findViewById(R.id.txt_maps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.float_btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Elenge_MapsMainActivity.this);

                final AlertDialog dialog = builder.create();
                LayoutInflater inflater = dialog.getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.dialog_maps_add, null);
                dialog.setView(dialogLayout);
                dialog.setCanceledOnTouchOutside(false);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                ImageButton btn_closed_list = dialog.findViewById(R.id.btn_close_dialog_addList);
                btn_closed_list.setOnClickListener(v1 -> dialog.cancel());

                img_loc = dialog.findViewById(R.id.img_maps);
                final EditText nameLieu = dialog.findViewById(R.id.maps_name);
                final EditText namePrestataire = dialog.findViewById(R.id.maps_prestataire);
                final EditText namePhone = dialog.findViewById(R.id.maps_phone);
                final EditText nameType = dialog.findViewById(R.id.maps_type);
                final EditText Address = dialog.findViewById(R.id.maps_address);
                final EditText coordLat = dialog.findViewById(R.id.maps_coord_lat);
                final EditText coordLog = dialog.findViewById(R.id.maps_coord_log);

                img_loc.setOnClickListener(v12 -> {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent.createChooser(intent, "Select Image"), IMAGE_PICK_GALLERY_CODE);
                });

                dialog.findViewById(R.id.map_get_coordinate).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(dialog.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) dialog.getContext(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_CODE_LOCATION_PERMISSION);
                        } else {
                            /*-get current position-*/
                            LocationRequest locationRequest = new LocationRequest();
                            locationRequest.setInterval(10000);
                            locationRequest.setFastestInterval(3000);
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                                                coordLat.setText(String.valueOf(latitude));
                                                coordLog.setText(String.valueOf(longitude));

                                                Location location = new Location("providerNA");
                                                location.setLatitude(latitude);
                                                location.setLongitude(longitude);
                                                //fetchAddressFromLatLong(location);
                                            }
                                        }
                                    }, Looper.getMainLooper());
                        }
                    }
                });

                ImageButton btn_add_list = dialog.findViewById(R.id.btn_okay_dialog_addList);
                btn_add_list.setOnClickListener(v13 -> {

                    String LieuName = nameLieu.getText().toString();
                    String Prestateur = namePrestataire.getText().toString();
                    String PhoneLieu = namePhone.getText().toString();
                    String TypeLieu = nameType.getText().toString();
                    String AddressLieu = Address.getText().toString();
                    Double Latitude = Double.valueOf(coordLat.getText().toString());
                    Double Longitude = Double.valueOf(coordLog.getText().toString());

                    if ((TextUtils.isEmpty(LieuName) || TextUtils.isEmpty(Prestateur) || TextUtils.isEmpty(PhoneLieu) || TextUtils.isEmpty(TypeLieu)
                            || TextUtils.isEmpty(AddressLieu) /*|| TextUtils.isEmpty(Latitude) || TextUtils.isEmpty(Longitude)*/) && uri != null) {
                        Toast.makeText(Elenge_MapsMainActivity.this, "Champs vides!", Toast.LENGTH_SHORT).show();
                    } else {
                        saveLocalisation(LieuName, Prestateur, PhoneLieu, TypeLieu, AddressLieu, Latitude, Longitude);
                        dialog.cancel();
                    }

                    Toast.makeText(Elenge_MapsMainActivity.this, "List add....", Toast.LENGTH_SHORT).show();
                });
            }

            private void saveLocalisation(String lieuName, String prestateur, String phoneLieu, String typeLieu, String addressLieu, Double latitude, Double longitude) {

                mProgress.setTitle("POSTING...");
                mProgress.setMessage("Patientez s'il vous plait!");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();

                StorageReference filepath = storage.child("maps_images").child(uri.getLastPathSegment());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(getApplicationContext(), "Successfully Uploaded", Toast.LENGTH_SHORT).show();

                        DatabaseReference Maps_coordinate = databaseReference.push();
                        //adding post contents to database reference
                        mDatabaseUsers.addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Object timestamps = ServerValue.TIMESTAMP;

                                Maps_coordinate.child("imageUrl").setValue(uri.toString());
                                Maps_coordinate.child("place").setValue(lieuName);
                                Maps_coordinate.child("type").setValue(typeLieu);
                                Maps_coordinate.child("address").setValue(addressLieu);
                                Maps_coordinate.child("provider").setValue(prestateur);
                                Maps_coordinate.child("phone").setValue(phoneLieu);
                                Maps_coordinate.child("latitude").setValue(latitude);
                                Maps_coordinate.child("longitude").setValue(longitude)

                                        .addOnCompleteListener(new OnCompleteListener<Void>() {

                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    //finish();
                                                    mProgress.dismiss();
                                                }
                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        });

        findViewById(R.id.txt_list).setOnClickListener(v -> {
            final AlertDialog.Builder builderList = new AlertDialog.Builder(Elenge_MapsMainActivity.this);

            final AlertDialog dialogList = builderList.create();
            LayoutInflater inflaterList = dialogList.getLayoutInflater();
            View dialogLayout = inflaterList.inflate(R.layout.dialog_maps_list, null);
            dialogList.setView(dialogLayout);
            dialogList.setCanceledOnTouchOutside(false);
            dialogList.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogList.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogList.show();
        });
    }

    @Override
    // image from gallery result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_PICK_GALLERY_CODE) {

                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }

            if (requestCode == IMAGE_PICK_CAMERA_CODE) {

                CropImage.activity(uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                uri = result.getUri();
                img_loc.setImageURI(uri);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        DatabaseReference MapReference = FirebaseDatabase.getInstance().getReference("Maps_Coordinates");
        MapReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                        String ImageLoc = (String) snapshot.child("imageUrl").getValue();
                        String Place = (String) snapshot.child("place").getValue();
                        String Type = (String) snapshot.child("type").getValue();
                        String Address = (String) snapshot.child("address").getValue();
                        String Provider = (String) snapshot.child("provider").getValue();
                        String Phone = (String) snapshot.child("phone").getValue();
                        Double Latitude = snapshot.child("latitude").getValue(Double.class);
                        Double Longitude = snapshot.child("longitude").getValue(Double.class);

                        mMap = googleMap;

                    if(Latitude != null && Longitude != null){
                        LatLng latLng = new LatLng(Latitude,Longitude);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(Place).snippet(Type+"\n"+Address));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Latitude,Longitude),10F));
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(Elenge_MapsMainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                            return;
                        }else{
                            mMap.setMyLocationEnabled(true);
                        }
                    }

                   /* if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        ActivityCompat.requestPermissions(Elenge_MapsMainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }else{
                        mMap.setMyLocationEnabled(true);
                    }*/

                    //Toast.makeText(Elenge_MapsMainActivity.this, "Données: "+Latitude +Longitude, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }
    }
}