package com.youthfimodd.elenges;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FirstActivity extends AppCompatActivity {

    TextView logo,slogan;
    ImageView image;
    Animation topAnim,bottomAnim,middleAnimation;
    private static  int SPLASH_SCREEN=5000;
    private SharedPreferences onBoardingScreen;
    ProgressBar progess_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /*--vue--*/
        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView);
        slogan = findViewById(R.id.textView2);
        progess_main = findViewById(R.id.progess_main);
        /*--Animation--*/
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation);

        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(middleAnimation);
        /*--Calling New Activity after SPLASH_SCREEN seconds 1s = 1000--*/
        new Handler().postDelayed(() -> {

            if(Connexion.verifyConnection(getApplicationContext())) {
                progess_main.setVisibility(View.GONE);
                onBoardingScreen = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
                boolean isfirstTime = onBoardingScreen.getBoolean("firstTime",true);
//                        if (isfirstTime){
//                            SharedPreferences.Editor editor = onBoardingScreen.edit();
//                            editor.putBoolean("firstTime",false);
//                            editor.commit();
//                            sendTokenToFirebase();
//                            startActivity(new Intent(MainActivity.this, OnBoardingActivity.class));
//                            finish();
//                        }
//                        else {
//                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
//                            finish();
//                        }
                startActivity(new Intent(FirstActivity.this, OnBoardingActivity.class));
                finish();
            }
            else {
                progess_main.setVisibility(View.VISIBLE);
                Toast.makeText(FirstActivity.this, "Problème de connexion!", Toast.LENGTH_SHORT).show();
            }

        },SPLASH_SCREEN);
    }

    private void sendTokenToFirebase() {

    }
}