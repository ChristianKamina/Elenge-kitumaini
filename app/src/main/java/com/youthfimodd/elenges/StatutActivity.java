package com.youthfimodd.elenges;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

public class StatutActivity extends AppCompatActivity {
    //
    private DatabaseReference mProfilDatabase;
    //
    private TextView status_name, status_time, statut_numero, statut_ville, statut_email, statut_typeUser;
    private ImageView status_img;
    private String posKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statut);
        //
        status_name = findViewById(R.id.staut_username);
        status_time = findViewById(R.id.statut_timeonline);
        status_img = findViewById(R.id.status_userimg);
        statut_numero = findViewById(R.id.txtNumero);
        statut_ville = findViewById(R.id.txtVille);
        statut_email = findViewById(R.id.txtEmail);
        statut_typeUser = findViewById(R.id.txtUserType);

        //prendre les informations vennant de chatActivity
        posKey = getIntent().getExtras().getString("Id");

        String statusimg = getIntent().getExtras().getString("image");
        Picasso.with(this).load(statusimg).into(status_img);

        String username = getIntent().getExtras().getString("name");
        status_name.setText(username);

        String statustime = timeStatut(getIntent().getExtras().getString("timeStatus"));
        status_time.setText(statustime);
        //
        mProfilDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(posKey);

        mProfilDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String Usertype = dataSnapshot.child("typeusesr").getValue().toString();
                String Email = dataSnapshot.child("email").getValue().toString();
                String Phone = dataSnapshot.child("phone").getValue().toString();
                String Age = dataSnapshot.child("age").getValue().toString();
                String Ville = dataSnapshot.child("ville").getValue().toString();

                statut_email.setText(Email);
                statut_numero.setText(Phone);
                statut_typeUser.setText(Usertype);
                statut_ville.setText(Ville);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        findViewById(R.id.btnChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatutActivity.this, ChatActivity.class);
                intent.putExtra("user_id", posKey);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);
            }
        });

    }
    //
    public String timeStatut(String timestatut){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        String date = DateFormat.format("dd-MM-yyyy hh:mm ss", calendar).toString();

        return date;
    }
}
