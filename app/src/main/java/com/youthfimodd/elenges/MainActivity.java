package com.youthfimodd.elenges;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    //
    TextInputLayout User, pwd;
    //
    private ProgressDialog mloginProgress;
    //firebase
    private FirebaseAuth mAuth;
    //
    Button mButtonLogin, mButtonSignin;

    RelativeLayout rallay1;

    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //
            rallay1.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //fire base
        mAuth = FirebaseAuth.getInstance();
        //Progress bar
        mloginProgress = new ProgressDialog(this);
        //lien avec xml
        rallay1 = findViewById(R.id.rellay1);
        User = findViewById(R.id.Username);
        pwd = findViewById(R.id.Password);
        //
        handler.postDelayed(runnable, 1000);
        //
        mButtonLogin = findViewById(R.id.btnLogin);
        //
        mButtonSignin = findViewById(R.id.btnSign);
        //Button Connexion
        mButtonLogin.setOnClickListener(v -> {
            //
            String Email = User.getEditText().getText().toString().trim();
            String Passw = pwd.getEditText().getText().toString().trim();

            if (!TextUtils.isEmpty(Email) || !TextUtils.isEmpty(Passw)){
                //
                mloginProgress.setTitle("Connection...");
                mloginProgress.setMessage("Veuillez patienter, s'il vous plait !");
                mloginProgress.setCanceledOnTouchOutside(false);
                mloginProgress.show();
                //
                loginUser(Email, Passw);
            }else if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(Passw)){
                //
                User.setError("Error, champ vide");
                pwd.setError("Error, champ vide");
            }
        });
        //Button s'incrire
        mButtonSignin.setOnClickListener(v -> {
            //
            Intent intentSing = new Intent(MainActivity.this, AccueilActivity.class);
            startActivity(intentSing);
        });
    }
    //
    private void loginUser(String email, String passw) {
        //
        mAuth.signInWithEmailAndPassword(email, passw).addOnCompleteListener(task -> {
            //
            if (task.isSuccessful()){
                //
                mloginProgress.dismiss();
                //
                Intent intentLogin = new Intent(MainActivity.this, Main2Activity.class);
                intentLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentLogin);
                finish();

            }else {
                //
                mloginProgress.hide();
                //
                Toast.makeText(MainActivity.this, " Impossible de se connceter! Vérifier votre connection puis réessayer", Toast.LENGTH_LONG).show();
            }
        });
    }
}
