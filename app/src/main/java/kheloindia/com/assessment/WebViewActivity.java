package kheloindia.com.assessment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


import kheloindia.com.assessment.util.ProgressDialogUtility;

import kheloindia.com.assessment.functions.Constant;

/**
 * Created by PC10 on 13-Nov-17.
 */

public class WebViewActivity extends AppCompatActivity {

    WebView webview;
    Toolbar toolbar;
    TextView title_tv;
    ProgressDialogUtility progressDialogUtility;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);

        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title_tv = (TextView) toolbar.findViewById(R.id.title_tv);
        webview = (WebView) findViewById(R.id.webview);

        webview.setWebViewClient(new MyBrowser());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        openURL(Constant.WEBVIEW_URL);
        title_tv.setText(Constant.TITLE);
        progressDialogUtility=new ProgressDialogUtility(this);

    }

    private void openURL(String url) {
        webview.getSettings().setLoadsImagesAutomatically(true);
         webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.loadUrl(url);
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

            progressDialogUtility.dismissProgressDialog();
           // view.stopLoading();
            webview.loadUrl("http://mydiary.fitness365.me/Errorpage.html?aspxerrorpath=/");


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
