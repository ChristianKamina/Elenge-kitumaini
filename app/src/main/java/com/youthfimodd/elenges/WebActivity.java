package com.youthfimodd.elenges;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.Objects;

public class WebActivity extends AppCompatActivity {
    //
    private Toolbar toolbar;
    private WebView webView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;

    String VariableRec;
    String url = "https://www.google.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        /*init toolbar*/
        toolbar = findViewById(R.id.toolbar);
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        refreshLayout = findViewById(R.id.webViewRefresh);

        //recuperer Variable
        VariableRec = getIntent().getStringExtra("Adresse");

        // setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Learn more");
        getSupportActionBar().setSubtitle(VariableRec);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                supportFinishAfterTransition();
            }
        });
        //
        initWebView();
        //charger url
        webView.loadUrl("https://"+VariableRec);
        //
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //
                refreshLayout.setRefreshing(true);
                webView.reload();
                refreshLayout.setRefreshing(false);
            }
        });
    }
    //
    private void initWebView(){
        /**Zoom Eneble**/
        webView.getSettings().setBuiltInZoomControls(true);
        //
        webView.setWebChromeClient(new MyWebChromeClient(this));
        //
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicin){
                super.onPageStarted(view, url, favicin);
                //
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String newURL){
                //
                webView.loadUrl(newURL);
                url = newURL;
                //
                Objects.requireNonNull(getSupportActionBar()).setSubtitle(newURL);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url){
                super.onPageFinished(view, url);
                //
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                super.onReceivedError(view, request, error);
                //
                progressBar.setVisibility(View.GONE);
                Toast.makeText(WebActivity.this, "Error Loading "+url, Toast.LENGTH_SHORT).show();
            }
        });
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
    }
    //
    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context){
            super();
            this.context = context;
        }
    }
    @Override
    public void onBackPressed(){
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
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
        intent.putExtra(intent.EXTRA_TEXT, VariableRec);
        intent.setType("text/plain");
        startActivity(intent);
    }
}
