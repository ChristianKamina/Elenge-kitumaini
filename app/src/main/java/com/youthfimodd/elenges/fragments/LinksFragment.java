package com.youthfimodd.elenges.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.youthfimodd.elenges.Main2Activity;
import com.youthfimodd.elenges.R;

import java.util.Objects;

public class LinksFragment extends Fragment {


    public LinksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Nom dans Action Bar
        ((Main2Activity) Objects.requireNonNull(getActivity())).SetActionBarTitle("Liens des partenaires");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_links, container, false);

        return view;
    }
}