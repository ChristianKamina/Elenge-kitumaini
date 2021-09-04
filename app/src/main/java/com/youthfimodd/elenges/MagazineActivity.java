package com.youthfimodd.elenges;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.AdapterViewFlipper;

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
import com.youthfimodd.elenges.adapters.MagazineAdapter;
import com.youthfimodd.elenges.models.Magazin;

import java.util.ArrayList;
import java.util.List;

public class MagazineActivity extends AppCompatActivity {

    private AdapterViewFlipper adapterViewFlipper;

    private RecyclerView recyclerMagazin;
    private MagazineAdapter mAdapter;
    private List<Magazin> magazinList;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine);

        mProgress = new ProgressDialog(this);

        firebaseDatabase = FirebaseDatabase.getInstance();

        recyclerMagazin = findViewById(R.id.Mag_list);
        recyclerMagazin.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerMagazin.setLayoutManager(layoutManager);

        int[] images = {
                R.mipmap.top_jeunes_mag_a,
                R.mipmap.jeunes_afrique,
                R.mipmap.ados_jeunes,

        };

        String [] names = {
                "Jeunes",
                "Afrique",
                "Ados"
        };

        adapterViewFlipper = findViewById(R.id.ImgFlipper);
        MagAdapterFlipper myAdapterFilpper = new MagAdapterFlipper(this, images, names);
        adapterViewFlipper.setAdapter(myAdapterFilpper);
        adapterViewFlipper.setFlipInterval(2000);
        adapterViewFlipper.setAutoStart(true);

        mProgress.setMessage("Chargement...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        getAllMags();
    }

    private void getAllMags() {

        recyclerMagazin.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        DatabaseReference santeRef = firebaseDatabase.getReference("Magazines");
        santeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //
                magazinList = new ArrayList<>();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                    Magazin mag = snapshot.getValue(Magazin.class);
                    magazinList.add(mag);

                    mProgress.dismiss();
                }
                mAdapter = new MagazineAdapter(getApplicationContext(), magazinList);
                recyclerMagazin.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
