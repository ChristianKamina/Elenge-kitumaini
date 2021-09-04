package com.youthfimodd.elenges;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.AdapterViewFlipper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.youthfimodd.elenges.adapters.MagAdapterFlipper;
import com.youthfimodd.elenges.adapters.MediAdapter;
import com.youthfimodd.elenges.models.Media;

import java.util.ArrayList;
import java.util.List;

public class MultimediaActivity extends AppCompatActivity {

    private AdapterViewFlipper adapterViewFlipper;

    private RecyclerView recyclerMedia;
    private MediAdapter mAdapter;
    private List<Media> mediaList;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimedia);

        mProgress = new ProgressDialog(this);

        firebaseDatabase = FirebaseDatabase.getInstance();

        recyclerMedia = findViewById(R.id.Media_list);
        recyclerMedia.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerMedia.setLayoutManager(layoutManager);

        int[] images = {
                R.mipmap.images,
                R.mipmap.shuga_babi,
                R.mipmap.shuga_baby,

        };
        String [] names = {
                "C'est la vie",
                "Chouga baby",
                "Jeunes"
        };

        adapterViewFlipper = findViewById(R.id.ImgFlipper_media);
        MagAdapterFlipper myAdapterFilpper = new MagAdapterFlipper(this, images, names);
        adapterViewFlipper.setAdapter(myAdapterFilpper);
        adapterViewFlipper.setFlipInterval(2000);
        adapterViewFlipper.setAutoStart(true);

        mProgress.setMessage("Chargement...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        getAllMedia();
    }

    private void getAllMedia() {

        recyclerMedia.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        DatabaseReference santeRef = firebaseDatabase.getReference("Medias");
        santeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //
                mediaList = new ArrayList<>();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                    Media media = snapshot.getValue(Media.class);
                    mediaList.add(media);

                    mProgress.dismiss();
                }
                mAdapter = new MediAdapter(getApplicationContext(), mediaList);
                recyclerMedia.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MultimediaActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}