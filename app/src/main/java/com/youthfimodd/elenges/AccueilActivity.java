package com.youthfimodd.elenges;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class AccueilActivity extends AppCompatActivity {
    //
    AdapterViewFlipper adapterViewFlipper;
    //
    int[] image = {
            R.mipmap.logo_elenge,
            R.mipmap.logo,
            R.mipmap.logo_pf2,
            R.mipmap.logo_violence,
            R.mipmap.logo_pf,
    };

    Button btnPar, btnPair, btnUser;
    //
    String Parrain, PairEduc, Users;
    String UserMode, ParrainMode, PairEdMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        //
        Parrain = "Parrain"; UserMode = getResources().getString(R.string.Useur_mode);
        PairEduc = "Pair Educateur"; ParrainMode = getResources().getString(R.string.Parrain_mode);
        Users = "User"; PairEdMode = getResources().getString(R.string.PairEd_mode);
        //
        adapterViewFlipper = findViewById(R.id.ViewFlipper);
        //
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), image);
        //
        adapterViewFlipper.setAdapter(customAdapter);
        adapterViewFlipper.setFlipInterval(2600);
        adapterViewFlipper.setAutoStart(true);
        //
        btnPar = findViewById(R.id.btnParrain);
        btnPair = findViewById(R.id.btnPairEd);
        btnUser = findViewById(R.id.btnUser);
        //
        btnPar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Intent intent = new Intent(AccueilActivity.this, SecurityCodeActivity.class);
                intent.putExtra("PairEd", Parrain);
                //intent.putExtra("Detail", ParrainMode);
                startActivity(intent);
            }
        });
        btnPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Intent intentPair = new Intent(AccueilActivity.this, SecurityCodeActivity.class);
                intentPair.putExtra("PairEd", PairEduc);
                //intentPair.putExtra("Detail", PairEdMode);
                startActivity(intentPair);
            }
        });
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Intent intentUsers = new Intent(AccueilActivity.this, SinginActivity.class);
                intentUsers.putExtra("PairEd", Users);
                //intentUsers.putExtra("Detail", UserMode);
                startActivity(intentUsers);
            }
        });
    }

    //
    class CustomAdapter extends BaseAdapter {
        Context context;
        int[] images;
        LayoutInflater inflater;

        public CustomAdapter(Context applicationContext, int[] image) {
            this.context = applicationContext;
            this.images = image;
            inflater = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.list_images, null);
            ImageView image = convertView.findViewById(R.id.imageChange);
            image.setImageResource(images[position]);
            return convertView;
        }
    }
}
