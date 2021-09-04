package com.youthfimodd.elenges;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.youthfimodd.elenges.custom.chat_model.Users;
import com.youthfimodd.elenges.fragments.ChatFragment;
import com.youthfimodd.elenges.fragments.ForumFragment;
import com.youthfimodd.elenges.fragments.JapprendsFragment;
import com.youthfimodd.elenges.fragments.JeminformeFragment;
import com.youthfimodd.elenges.fragments.KitumainiFragment;
import com.youthfimodd.elenges.fragments.ParametreFragment;
import com.youthfimodd.elenges.fragments.VbgFragment;

import java.util.Objects;

public class Main2Activity extends AppCompatActivity
        implements OnNavigationItemSelectedListener {
    //fire base code
    DatabaseReference mUserDatabase;
    FirebaseUser mCurrentUser;
    //
    TextView User_name, User_email;
    ImageView profil_image, image_back;
    String current_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        /*--*/
        View NavigView = navigationView.getHeaderView(0);
        image_back = NavigView.findViewById(R.id.image_back);
        profil_image = NavigView.findViewById(R.id.image_profil);
        User_name = NavigView.findViewById(R.id.txtFullName);
        User_email = NavigView.findViewById(R.id.txtEmail);

        image_back.setOnClickListener(v -> {
            Toast.makeText(Main2Activity.this,"closed",Toast.LENGTH_LONG).show();
            drawer.closeDrawers();
        });
        //fire base code
        //window par default
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.ftMain, new JeminformeFragment());
        ft.commit();
    }
    //fire base code
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //
        if (currentUser == null){
            //
            sendTostart();
        }else {

           // mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
            String current_uid = currentUser.getUid();
            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String Image = snapshot.child("image").getValue().toString();
                    String Name = snapshot.child("name").getValue().toString();
                    String Email = snapshot.child("email").getValue().toString();

                    User_name.setText(Name);
                    User_email.setText(Email);

                    Users user = snapshot.getValue(Users.class);
                    if (user.getImage().equals("default")){
                        profil_image.setImageResource(R.mipmap.blog_avatar);

                    }else {
                       Picasso.with(getApplicationContext()).load(Image).into(profil_image);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    //deconnexion
    private void sendTostart() {
        //online or not
        Intent intent = new Intent(Main2Activity.this, FirstActivity.class);
        startActivity(intent);
        finish();

    }
    //le tilre dans la Bar d'action
    public void SetActionBarTitle(String Title){
        Objects.requireNonNull(getSupportActionBar()).setTitle(Title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            //code firebase pour la deconnexion
            FirebaseAuth.getInstance().signOut();
            //appel de la method
            sendTostart();
            return true;

        }else if (id == R.id.notification){
            //
            Intent intent = new Intent(this, NotificationsActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.profil){
            //
            Intent intent = new Intent(this, ProfilActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_news) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.ftMain, new JeminformeFragment());
            ft.commit();
        }else if (id == R.id.nav_vbg) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.ftMain, new VbgFragment());
            ft.commit();
        }else if (id == R.id.nav_kitumaini){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.ftMain, new KitumainiFragment());
            ft.commit();
        }else if (id == R.id.nav_forum) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.ftMain, new ForumFragment());
            ft.commit();
        } else if (id == R.id.nav_chat) {
            FragmentTransaction fi = getSupportFragmentManager().beginTransaction();
            fi.replace(R.id.ftMain, new ChatFragment());
            fi.commit();
        } else if (id == R.id.nav_jassure) {
            Intent MapsIntent = new Intent(this, MapsActivity.class);
            MapsIntent.putExtra("type", "Centre");
            startActivity(MapsIntent);
            /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.ftMain, new JassureFragment());
            ft.commit();*/
        } else if (id == R.id.nav_learn){
            FragmentTransaction fpc = getSupportFragmentManager().beginTransaction();
            fpc.replace(R.id.ftMain, new JapprendsFragment());
            fpc.commit();
        } else if (id == R.id.nav_liens){
            /*FragmentTransaction fpc = getSupportFragmentManager().beginTransaction();
            fpc.replace(R.id.ftMain, new LinksFragment());
            fpc.commit();*/
        } else if (id == R.id.nav_setting){
            FragmentTransaction fpc = getSupportFragmentManager().beginTransaction();
            fpc.replace(R.id.ftMain, new ParametreFragment());
            fpc.commit();
        } else if (id == R.id.nav_deconnexion){
            FirebaseAuth.getInstance().signOut();
            sendTostart();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
