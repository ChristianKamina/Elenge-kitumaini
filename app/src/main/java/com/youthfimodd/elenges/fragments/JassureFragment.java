package com.youthfimodd.elenges.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.youthfimodd.elenges.Main2Activity;
import com.youthfimodd.elenges.MapsActivity;
import com.youthfimodd.elenges.R;

import java.util.Objects;

/** cette fragment n"est plus utilisable
 * A simple {@link Fragment} subclass.
 */
public class JassureFragment extends Fragment {
    //
    ImageButton maps;

    public JassureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((Main2Activity) Objects.requireNonNull(getActivity())).SetActionBarTitle("J'en ai j'assure");
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_jassure, container, false);
        //
        maps = view.findViewById(R.id.maps);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                Intent intent = new Intent(getContext(), MapsActivity.class);
                startActivity(intent);

            }
        });
        return view;
    }

}
