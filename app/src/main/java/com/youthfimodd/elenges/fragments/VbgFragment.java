package com.youthfimodd.elenges.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.youthfimodd.elenges.Main2Activity;
import com.youthfimodd.elenges.MapsActivity;
import com.youthfimodd.elenges.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VbgFragment extends Fragment {
    /*--*/

    public VbgFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((Main2Activity) requireActivity()).SetActionBarTitle("Parlons Violences BG");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vbg, container, false);
        /*-lien avec xml*--*/
        view.findViewById(R.id.line_book).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent MapsIntent = new Intent(getContext(), All_about_vbgActivity.class);
                startActivity(MapsIntent);*/
            }
        });
        view.findViewById(R.id.line_police).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MapsIntent = new Intent(getContext(), MapsActivity.class);
                MapsIntent.putExtra("type", "Police");
                startActivity(MapsIntent);
            }
        });
        view.findViewById(R.id.line_centre).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MapsIntent = new Intent(getContext(), MapsActivity.class);
                MapsIntent.putExtra("type", "Centre");
                startActivity(MapsIntent);
            }
        });
        view.findViewById(R.id.line_justice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MapsIntent = new Intent(getContext(), MapsActivity.class);
                MapsIntent.putExtra("type", "Justice");
                startActivity(MapsIntent);
            }
        });

        return view;
    }
}
