package com.youthfimodd.elenges.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.youthfimodd.elenges.Main2Activity;
import com.youthfimodd.elenges.R;
import com.youthfimodd.elenges.adapters.EDiveAdapter;
import com.youthfimodd.elenges.firebase.DatabaseFireStore;
import com.youthfimodd.elenges.models.EDive;

import java.util.ArrayList;

public class JapprendsFragment extends Fragment {

    RecyclerView rv_e_dive;
    EDiveAdapter adapter;
    ArrayList<EDive> mList = new ArrayList<>();

    public JapprendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((Main2Activity) requireActivity()).SetActionBarTitle("J'apprends, J'assure");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_japprends, container, false);

        rv_e_dive = view.findViewById(R.id.rv_e_dive);
        rv_e_dive.setHasFixedSize(false);
        rv_e_dive.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        getData();

        return view;
    }

    private void getData() {
        DatabaseFireStore.get().collection("e-dive").get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (DocumentSnapshot document : task.getResult())
                        {
                            EDive eDive = new EDive();
                            eDive.setTitle(document.getString("title"));
                            eDive.setImageBg(document.getString("image"));

                            mList.add(eDive);
                            adapter = new EDiveAdapter(mList,getActivity());
                            rv_e_dive.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}