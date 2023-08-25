package rsarschoolmodel.com.rsarschoolmodel;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_web_view);
        WebView webView=findViewById(R.id.webView1);
        webView.loadUrl("https://www.rachnasagar.in/");
    }
}