package com.youthfimodd.elenges;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class PrivatePolicyActivity extends AppCompatActivity {

    WebView web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_policy);
        //Toolbar
        getSupportActionBar().setTitle("Politique de Confidentialité");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        web_view = findViewById(R.id.web_view);
        web_view.loadUrl("https://www.privacypolicies.com/live/00078c9f-ca16-4c3e-90fa-3055d2863a8c");
    }
}