package kheloindia.com.assessment;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.util.Utility;

import kheloindia.com.assessment.functions.Constant;

/**
 * Created by PC10 on 22-Feb-18.
 */

public class HMActivity extends AppCompatActivity {

    WebView webview;
    ProgressDialogUtility progressDialogUtility;
    ImageView logout_img;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hm);

        init();
    }

    private void init() {
        webview = (WebView) findViewById(R.id.webview);
        progressDialogUtility=new ProgressDialogUtility(HMActivity.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        logout_img = (ImageView) toolbar.findViewById(R.id.logout_img);

        logout_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showLogoutDialog(HMActivity.this);
            }
        });

        webview.setWebViewClient(new HMActivity.MyBrowser());

        openURL();

    }

    private void openURL() {
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.loadUrl(Constant.USER_URl);
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


}
