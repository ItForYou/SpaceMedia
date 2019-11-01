package co.kr.itforone.spacemedia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullscreenActivity extends AppCompatActivity {
    @BindView(R.id.webview)    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Intent i = getIntent();
        int idx = i.getIntExtra("idx",1);

        WebSettings settings = webView.getSettings();
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new ViewManager(this));
        webView.setWebChromeClient(new ChromeManager());
        webView.addJavascriptInterface(new FullscreenActivity.WebviewJavainterface(),"Android");
        ///settings.setUserAgentString(settings.getUserAgentString()+"//Brunei");
        webView.loadUrl(getString(R.string.fullslide) + String.valueOf(idx));
    }

    private class WebviewJavainterface {

        @JavascriptInterface
        public void killintent() {
          finish();
        }
    }
}

