package com.youthfimodd.elenges;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ModifActivity extends AppCompatActivity {
    //Fire base
    private DatabaseReference mProfilDatabase;
    private FirebaseUser mCurrentUser;
    //
    EditText nom, phone, ville, genre, age, email;
    Button btnUpdate;
    //Progress
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modif);
        //Toolbar
        getSupportActionBar().setTitle("Modifier le Profil");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //fire base
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        //
        mProfilDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mProfilDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //
                String name = dataSnapshot.child("name").getValue().toString();
                String Email = dataSnapshot.child("email").getValue().toString();
                String Phone = dataSnapshot.child("phone").getValue().toString();
                String Age = dataSnapshot.child("age").getValue().toString();
                String Genre = dataSnapshot.child("sexe").getValue().toString();
                String Ville = dataSnapshot.child("ville").getValue().toString();
                //
                nom.setText(name);
                email.setText(Email);
                phone.setText(Phone);
                ville.setText(Ville);
                genre.setText(Genre);
                age.setText(Age);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //pour update dans la BD
        String nom_value = getIntent().getStringExtra("nom_value");
        String phone_value = getIntent().getStringExtra("phone_value");
        String ville_value = getIntent().getStringExtra("ville_value");
        String genre_value = getIntent().getStringExtra("genre_value");
        String age_value = getIntent().getStringExtra("age_value");
        String email_value = getIntent().getStringExtra("email_value");

        //lien avec xml
        nom = findViewById(R.id.nom_input);
        phone = findViewById(R.id.phone_input);
        ville = findViewById(R.id.ville_input);
        genre = findViewById(R.id.genre_input);
        age = findViewById(R.id.age_input);
        email = findViewById(R.id.email_input);
        btnUpdate = findViewById(R.id.btn_updatepro);
        //
        nom.setText(nom_value);
        phone.setText(phone_value);
        ville.setText(ville_value);
        genre.setText(genre_value);
        age.setText(age_value);
        email.setText(email_value);
        //
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Progress
                mProgress = new ProgressDialog(ModifActivity.this);
                mProgress.setTitle("Update");
                mProgress.setMessage("Please, wait while we save changes");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.show();
                //
                String Nom = nom.getText().toString();
                String Phone = phone.getText().toString();
                String Ville = ville.getText().toString();
                String Genre = genre.getText().toString();
                String Age = age.getText().toString();
                String Email = email.getText().toString();
                //
                mProfilDatabase.child("name").setValue(Nom).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //
                        if (task.isSuccessful()){
                            //
                            mProgress.dismiss();
                        }else {
                            //
                            Toast.makeText(getApplicationContext(), "There was some error is saving Change", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                mProfilDatabase.child("phone").setValue(Phone).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //
                        if (task.isSuccessful()){
                            //
                            mProgress.dismiss();
                        }else {
                            //
                            Toast.makeText(getApplicationContext(), "There was some error is saving Change", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                mProfilDatabase.child("ville").setValue(Ville).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            //
                            mProgress.dismiss();
                        }else {
                            //
                            Toast.makeText(getApplicationContext(), "There was some error is saving Change", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                mProfilDatabase.child("age").setValue(Age).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //
                        if (task.isSuccessful()){
                            //
                            mProgress.dismiss();
                        }else {
                            //
                            Toast.makeText(getApplicationContext(), "There was some error is saving Change", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                mProfilDatabase.child("sexe").setValue(Genre).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //
                        if (task.isSuccessful()){
                            //
                            mProgress.dismiss();
                        }else {
                            //
                            Toast.makeText(getApplicationContext(), "There was some error is saving Change", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                mProfilDatabase.child("email").setValue(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            //
                            mProgress.dismiss();
                        }else {
                            //
                            Toast.makeText(getApplicationContext(), "There was some error is saving Change", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
    //Option memu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
