package com.youthfimodd.elenges.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.youthfimodd.elenges.R;


public class JeminformeFragment extends Fragment {

    private BottomNavigationView navigation;

    public JeminformeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jeminforme, container, false);

        navigation = (BottomNavigationView) view.findViewById(R.id.navigation);
        navigation.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new NewsFragment());
        ft.commit();

        button_navifation();

        return view;
    }

    private void button_navifation(){

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                switch (menuItem.getItemId()){

                    case R.id.navigation_actual:
                        navigation.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_container, new NewsFragment());
                        ft.commit();
                        return true;
                    case R.id.navigation_book:
                        navigation.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        FragmentTransaction ftM = getActivity().getSupportFragmentManager().beginTransaction();
                        ftM.replace(R.id.fragment_container, new SantesrFragment());
                        ftM.commit();
                        return true;
                }

                return false;
            }
        });
    }
}