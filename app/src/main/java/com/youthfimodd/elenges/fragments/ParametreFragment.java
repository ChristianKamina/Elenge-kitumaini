package com.youthfimodd.elenges.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.youthfimodd.elenges.Main2Activity;
import com.youthfimodd.elenges.PrivatePolicyActivity;
import com.youthfimodd.elenges.R;
import com.youthfimodd.elenges.maps.Elenge_MapsMainActivity;

public class ParametreFragment extends Fragment {

    TextView politique_de_confindetialite;

    public ParametreFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Nom dans Action Bar
        ((Main2Activity) requireActivity()).SetActionBarTitle("Paramètres");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parametre, container, false);

        politique_de_confindetialite = view.findViewById(R.id.politique_de_confindetialite);

        politique_de_confindetialite.setOnClickListener(v -> startActivity(new Intent(getContext(), PrivatePolicyActivity.class)));

        view.findViewById(R.id.Elenge_Maps).setOnClickListener(v -> {

            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            final AlertDialog dialog = builder.create();
            LayoutInflater inflater1 = dialog.getLayoutInflater();
            View dialogLayout = inflater1.inflate(R.layout.dialog_maps_elenge, null);
            dialog.setView(dialogLayout);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.show();

            EditText editCode = dialog.findViewById(R.id.edt_code);
            ProgressBar progressBar = dialog.findViewById(R.id.progress_dialog);
            progressBar.setVisibility(View.GONE);

            dialog.findViewById(R.id.btn_code).setOnClickListener(v1 -> {
                String code = editCode.getText().toString();

                if (TextUtils.isEmpty(code)){
                    Toast.makeText(dialog.getContext(), "Champs vide!", Toast.LENGTH_SHORT).show();
                }
                if (!TextUtils.isEmpty(code) && code.equals("E-762-934")){
                    progressBar.setVisibility(View.VISIBLE);
                    editCode.setEnabled(false);
                    editCode.setBackgroundColor(android.R.color.darker_gray);
                    startActivity(new Intent(getContext(), Elenge_MapsMainActivity.class));
                    editCode.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    getActivity().finish();
                }else {
                    Toast.makeText(dialog.getContext(), "Code Error!", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.findViewById(R.id.btn_close_dialog_code).setOnClickListener(v12 -> dialog.dismiss());
        });

        view.findViewById(R.id.evaluer).setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("make://details?id=com.tutorial.personal.androidstudiopro"));
                if (!isActivityStarted(intent)){
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.youthfimodd.elenges"));
                    if (!isActivityStarted(intent)){
                        Toast.makeText(getContext(),"Could not open Android market ", Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception e){
                Toast.makeText(getContext(), "Please install this App", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private boolean isActivityStarted(Intent aIntent){
        try {
            startActivity(aIntent);
            return true;
        }catch (ActivityNotFoundException e){
            return false;
        }
    }
}