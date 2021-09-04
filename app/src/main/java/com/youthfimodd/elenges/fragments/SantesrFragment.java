package com.youthfimodd.elenges.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.youthfimodd.elenges.Main2Activity;
import com.youthfimodd.elenges.R;
import com.youthfimodd.elenges.custom.santesr_model.SanteAdapter;
import com.youthfimodd.elenges.custom.santesr_model.SanteSr;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SantesrFragment extends Fragment {

    private RecyclerView recyclerSante;
    private SanteAdapter mAdapter;
    private List<SanteSr> msanteSrs;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog mProgress;


    public SantesrFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((Main2Activity)getActivity()).SetActionBarTitle("Santé");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_santesr, container, false);
        //
        mProgress = new ProgressDialog(getContext());
        //
        firebaseDatabase = FirebaseDatabase.getInstance();

        recyclerSante = view.findViewById(R.id.Sante_listview);
        recyclerSante.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerSante.setLayoutManager(layoutManager);
        //
        //databaseSante = FirebaseDatabase.getInstance().getReference().child("SanteSr");

        //databaseSante.keepSynced(true);
        mProgress.setMessage("Chargement...");
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();

        intAdapter();

        return view;
    }

    private void intAdapter() {

        recyclerSante.setLayoutManager(new GridLayoutManager(getContext(), 2));

        DatabaseReference santeRef = firebaseDatabase.getReference("SanteSr");
        santeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //
                msanteSrs = new ArrayList<>();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                    SanteSr sr = snapshot.getValue(SanteSr.class);
                    msanteSrs.add(sr);

                    mProgress.dismiss();
                }
                mAdapter = new SanteAdapter(getContext(), msanteSrs);
                recyclerSante.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //mProgress.dismiss();
    }
}
