package co.kr.itforone.spacemedia;

import android.app.Activity;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class ViewManager extends WebViewClient {
    Activity context;
    public ViewManager(Activity context){
        this.context = context;
    }
    public ViewManager(){
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
        return true;
    }
}

