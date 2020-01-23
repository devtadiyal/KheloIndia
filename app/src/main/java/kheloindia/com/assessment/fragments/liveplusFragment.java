package kheloindia.com.assessment.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import kheloindia.com.assessment.functions.Constant;
import kheloindia.com.assessment.util.ProgressDialogUtility;
import kheloindia.com.assessment.R;

/**
 * Created by PC10 on 13-Nov-17.
 */

public class liveplusFragment extends Fragment {

    View rootView;
    WebView webview;
    ProgressDialogUtility progressDialogUtility;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            rootView = inflater.inflate(R.layout.fragment_liveplus, container, false);


            init();
        } catch ( Exception e){
            e.printStackTrace();
        }

        return rootView;
    }

    private void init() {
        webview = (WebView) rootView.findViewById(R.id.webview);

        progressDialogUtility=new ProgressDialogUtility(getActivity());

        webview.setWebViewClient(new MyBrowser());

        openURL();
    }

    private void openURL() {
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
       // webview.loadUrl("http://103.65.20.125:88/kheloindia.aspx");
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
}
