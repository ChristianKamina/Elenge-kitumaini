package com.youthfimodd.elenges;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.common.util.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DetailSsrActivity extends AppCompatActivity {
    //
    private Toolbar toolbar;
    String FindsName, FindsDescr, recoitDescrip, recoitAdresse;
    TextView textWeb;
    PDFView textView;
    Button Lmore;
    private String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ssr);
        //
        FindsName = getIntent().getExtras().getString("titre");
        FindsDescr = getIntent().getExtras().getString("apercu");
        //Decriprtion
        recoitDescrip = getIntent().getExtras().getString("apercu");
        uri = FindsDescr;
        textView = findViewById(R.id.textDescrip);
        new RetrievePDFbyte().execute(uri);
        //Adresse
        textWeb = findViewById(R.id.txtweb);
        recoitAdresse = getIntent().getStringExtra("url");
        textWeb.setText(recoitAdresse);
        //Toolbar
        getSupportActionBar().setTitle(FindsName);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //lire plus
        Lmore = findViewById(R.id.btnLearmMore);

        Lmore.setOnClickListener(view -> {
            //
            Intent intshare = new Intent(getApplicationContext(), WebActivity.class);
            intshare.putExtra("Adresse", recoitAdresse);
            startActivity(intshare);
        });
    }
    /*code pdf*/
    class RetrievePDFbyte extends AsyncTask<String, Void, byte[]> {
        ProgressDialog progressDialog;
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(DetailSsrActivity.this);
            progressDialog.setMessage("Ouveture...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
        @Override
        protected byte[] doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                if (httpsURLConnection.getResponseCode() == 200){
                    inputStream = new BufferedInputStream(httpsURLConnection.getInputStream());
                }
            } catch (IOException e) {
                return null;
            }
            try {
                return IOUtils.toByteArray(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            textView.fromBytes(bytes).load();
            progressDialog.dismiss();
        }
    }

    //Option memu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate memu ressource
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }
    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.partage:
                shareIt();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //methode pour le partage
    private void shareIt() {
        //
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Elenge");
        intent.putExtra(intent.EXTRA_TEXT, "Elenge/Santé: "+FindsDescr);
        intent.setType("text/plain");
        startActivity(intent);
    }
}
