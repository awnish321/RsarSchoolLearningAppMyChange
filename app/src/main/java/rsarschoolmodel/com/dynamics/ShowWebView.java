package rsarschoolmodel.com.dynamics;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.net.URI;

import rsarschoolmodel.com.Common.ProgressHUD;
import rsarschoolmodel.com.rsarschoolmodel.R;

public class ShowWebView extends Activity {
	String Uri;
    //private Button button;
    DownloadManager manager=null;
    private long lastDownload=-1L;
    private WebView webView;
    File destinationDir;
    ProgressHUD dialog;
    String message = "Please Wait....";
    ProgressDialog progressDialog;

    public void onCreate(Bundle savedInstanceState) {
         
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_web_view);
        manager=(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        registerReceiver(onNotificationClick,
                new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("Linkpass")) {
             Uri = extras.getString("Linkpass");
            //Uri = extras.getString("https://www.google.co.in/");
        }
        //Get webview 
        webView = (WebView) findViewById(R.id.webView1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
         System.out.println("LInkkkkk"+" "+Uri);
         Log.d("LInkkkkk1",Uri);

        startWebView(Uri);
         
    }///storage/emulated/0/.rsarschoolmodel/Chapter1/videos/a.mp4
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("SetJavaScriptEnabled") private void startWebView(String url) {
        //Create new webview Client to show progress dialog
        //When opening a url or click on link
        /* webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;
          
            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {              
                view.loadUrl(url);
                 return true;
            }
   }); */
        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true); 
        // Other webview options
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            destinationDir = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Downloads");
        }else{
            destinationDir = new File(Environment.getExternalStorageDirectory(), "Downloads");
            // filev = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/" + Book_Zip_Title+".zip");
            //filev.mkdirs();
            Log.d("awa3",""+destinationDir);
        }
        if (!destinationDir.exists()) {
            destinationDir.mkdir(); // Don't forget to make the directory if it's not there
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading (WebView view, String url) {
                boolean shouldOverride = false;
                System.out.println("goosssscc"+"  "+url);
                //can handle normally
                if (url.endsWith(".pdf")) {
                    shouldOverride = true;
                    android.net.Uri source =  android.net.Uri.parse(url);
                    System.out.println("goossss"+"  "+url);
                    System.out.println("ppppppppp"+" "+Uri);
                    dialog = new ProgressHUD(ShowWebView.this, R.style.ProgressHUD);
                    dialog.setTitle("Pdf");
                    dialog.setContentView(R.layout.progress_hudd);
                    if (message == null || message.length() == 0) {
                        dialog.findViewById(R.id.message).setVisibility(View.GONE);
                    } else {
                        TextView txt = (TextView) dialog.findViewById(R.id.message);
                        txt.setText(message);
                    }
                    dialog.setCancelable(false);
                    dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
                    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                    lp.dimAmount = 0.2f;
                    dialog.getWindow().setAttributes(lp);
                    dialog.show();

                    // Make a new request pointing to the .doc url
                    DownloadManager.Request request = new DownloadManager.Request(source);

                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                    // Use the same file name for the destination
                    File destinationFile = new File (destinationDir, source.getLastPathSegment());
                    request.setDestinationUri( android.net.Uri.fromFile(destinationFile));
                    // Add it to the manager
                    manager.enqueue(request);

                    //webView.loadUrl(url);
                }
                return shouldOverride;
            }
        });

        //webView.loadUrl(url);
        ProgressDialog progressDialog=new ProgressDialog(ShowWebView.this);
        progressDialog.setMessage("Wait...");
        progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(Uri));
                        startActivity(intent);

                        progressDialog.dismiss();
                        finish();

                        Log.d("dedededede",Uri);
                    }
                },3000);
    }




    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(onComplete);
        unregisterReceiver(onNotificationClick);
    }

    BroadcastReceiver onComplete=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            //Toast.makeText(ctxt, "complete.........!", Toast.LENGTH_LONG).show();

            if (dialog.isShowing())
                dialog.dismiss();

            final Dialog dialoga = new Dialog(ShowWebView.this);
            dialoga.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialoga.setContentView(R.layout.dialog_open);
            dialoga.setCancelable(false);

            LinearLayout ln_outline= dialoga.findViewById(R.id.dia_ln_outline);
            LinearLayout ln_bg_blur= dialoga.findViewById(R.id.dia_ln_bg);
            TextView Txt_Title = (TextView) dialoga.findViewById(R.id.dia_error_title);
            View v_bg_color = (View)dialoga.findViewById(R.id.dia_view) ;
          //  ln_outline.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
          //  Txt_Title.setTextColor(Color.parseColor(Pref_Bg_Code));
          //  v_bg_color.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialoga.findViewById(R.id.dia_error_msg);
            text.setText("To View File Click OK Button!");
            Button dialogpdf = (Button) dialoga.findViewById(R.id.dia_b_error_button_pdf);
            //  dialogButton.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
            // if button is clicked, close the custom dialog
            dialogpdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(Uri));
                    startActivity(intent);
                    Log.d("dedededede",Uri);
                }
            });
            Button dialogButton = (Button) dialoga.findViewById(R.id.dia_b_error_button_link);
          //  dialogButton.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
                    Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(Uri));
                    startActivity(intent);

                    Log.d("dedededede",Uri);
                    dialoga.dismiss();

                }
            });
            Button dialogButtonCan = (Button) dialoga.findViewById(R.id.dia_b_error_button);
            dialogButtonCan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialoga.dismiss();
                }
            });

            dialoga.show();
        }
    };

    BroadcastReceiver onNotificationClick=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
           // Toast.makeText(ctxt, "Ummmm...hi!", Toast.LENGTH_LONG).show();
        }
    };

}
