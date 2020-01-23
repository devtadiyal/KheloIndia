package kheloindia.com.assessment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import kheloindia.com.assessment.util.ProgressDialogUtility;

import kheloindia.com.assessment.functions.Constant;

/**
 * Created by PC10 on 16-Mar-18.
 */

public class OpenScreenUrlActivity extends AppCompatActivity {

    Toolbar toolbar ;
    WebView webview;
    ProgressDialogUtility progressDialogUtility;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 0);
        }

        setContentView(R.layout.open_screen_url_activity);

        inti();
    }

    private void inti() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        webview = (WebView) findViewById(R.id.webview);

        toolbar.setTitle(Constant.SCREEN_NAME);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        webview = (WebView) findViewById(R.id.webview);

        progressDialogUtility=new ProgressDialogUtility(this);

        webview.setWebViewClient(new OpenScreenUrlActivity.MyBrowser());

        openURL();

    }
    private void openURL() {
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        Log.e("OpenScreenUrlActivity","url==> "+Constant.LOAD_URL);
        webview.loadUrl(Constant.LOAD_URL);
        webview.requestFocus();
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressDialogUtility.showProgressDialog();
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            progressDialogUtility.dismissProgressDialog();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

            view.loadUrl("http://mydiary.fitness365.me/Errorpage.html?aspxerrorpath=/");

        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



}
