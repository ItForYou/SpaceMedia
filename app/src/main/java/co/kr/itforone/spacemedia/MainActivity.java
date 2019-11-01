package co.kr.itforone.spacemedia;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.webview) WebView webView;
    private long backPrssedTime = 0;
    ValueCallback<Uri[]> filePathCallbackLollipop;
    Uri mCapturedImageURI;
    private final int MY_PERMISSIONS_REQUEST_CAMERA=1001;
    public void set_filePathCallbackLollipop(ValueCallback<Uri[]> filePathCallbackLollipop){
        this.filePathCallbackLollipop = filePathCallbackLollipop;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        webView.setWebChromeClient(new ChromeManager(this, this));
        webView.setWebViewClient(new ViewManager(this));
        webView.addJavascriptInterface(new WebviewJavainterface(),"Android");
        WebSettings settings = webView.getSettings();
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setJavaScriptEnabled(true);
        ///settings.setUserAgentString(settings.getUserAgentString()+"//Brunei");
        webView.loadUrl(getString(R.string.home));
    }
    @Override
    public void onBackPressed(){
        if(webView.canGoBack()){
//            String url = webView.copyBackForwardList().getItemAtIndex(webView.copyBackForwardList().getCurrentIndex()-1).getUrl();
//            webView.loadUrl(url);
            webView.goBack();
        }else{
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPrssedTime;
            if (0 <= intervalTime && 2000 >= intervalTime){
                finish();
            }
            else
            {
                backPrssedTime = tempTime;
                Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누를시 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this,"승인이 허가되어 있습니다.",Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this,"아직 승인받지 않았습니다.",Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ChromeManager.FILECHOOSER_LOLLIPOP_REQ_CODE) {
            Uri[] result = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                            //String dataString = data.getDataString();
                            ClipData clipData = data.getClipData();
                            if (clipData != null) {
                                result = new Uri[clipData.getItemCount()];
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    ClipData.Item item = clipData.getItemAt(i);
                                    result[i] = item.getUri();
                                }
                            }
                            else {
                            result = ChromeManager.FileChooserParams.parseResult(resultCode, data);
                            //result = (data == null) ? new Uri[]{mCapturedImageURI} : WebChromeClient.FileChooserParams.parseResult(resultCode, data);
                        }
                        filePathCallbackLollipop.onReceiveValue(result);
                    }
                    else{
                                filePathCallbackLollipop.onReceiveValue(null);
                                filePathCallbackLollipop = null;
                    }
                }
                else{
                    try {
                        if (filePathCallbackLollipop != null) {
                            filePathCallbackLollipop.onReceiveValue(null);
                            filePathCallbackLollipop = null;
                        }
                    }catch (Exception e){

                    }
                }
            }
        }

    }

    private class WebviewJavainterface {

        @JavascriptInterface
        public void share(String url) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.link)+url);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

        @JavascriptInterface
        public void show_full(int idx) {
            Intent fullscreen = new Intent(MainActivity.this, FullscreenActivity.class);
            fullscreen.putExtra("idx", idx);
            MainActivity.this.startActivity(fullscreen);
        }
    }
}
