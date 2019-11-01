package co.kr.itforone.spacemedia;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.File;

public class ChromeManager extends WebChromeClient {

    private final int MY_PERMISSIONS_REQUEST_CAMERA=1001;
    Activity activity;
    MainActivity mainActivity;
    static final int FILECHOOSER_LOLLIPOP_REQ_CODE=1300;
    ChromeManager(Activity activity, MainActivity mainActivity){
        this.activity = activity;
        this.mainActivity = mainActivity;
    }
    ChromeManager(){}
    Uri mCapturedImageURI;



    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        mainActivity.set_filePathCallbackLollipop(filePathCallback);

        // Create AndroidExampleFolder at sdcard
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AndroidExampleFolder");
        if (!imageStorageDir.exists()) {
            // Create AndroidExampleFolder at sdcard
            imageStorageDir.mkdirs();
        }

        // Create camera captured image file path and name
        /*File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        mCapturedImageURI = Uri.fromFile(file);

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
        mCapturedImageURI = null; */
//        Intent audioIntent = new Intent();
//        audioIntent.setType("audio/*");
//        audioIntent.setAction(Intent.ACTION_PICK);




        Intent i = new Intent();
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);

        // Create file chooser intent
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{audioIntent});

        mainActivity.startActivityForResult(chooserIntent, FILECHOOSER_LOLLIPOP_REQ_CODE);
        return true;
    }
    public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
        new AlertDialog.Builder(view.getContext())
                .setTitle("")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,
                        new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                .setCancelable(false)
                .create()
                .show();
        return true;
    }


}