package com.munish.saferfoodproject.View.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.munish.saferfoodproject.R;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "WebViewActivity";
    ImageView back,share;
    TextView title;
    String url,name;
    ProgressBar progress;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        retrieveData();
        intialization();


    }
// retrieve
    private void retrieveData() {
        Bundle extras = getIntent().getExtras();
        name=extras.getString("name");
        url=extras.getString("url");
    }
    private void intialization() {
        back=findViewById(R.id.back);
        share=findViewById(R.id.plus);
        share.setImageResource(R.drawable.ic_share_black_24dp);
        title=findViewById(R.id.title);
        title.setText(name);
        webView = findViewById(R.id.webView);
        progress = findViewById(R.id.progress);

        //webview
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);

        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                progress.setVisibility(View.GONE);
                Log.e(TAG, "onPageFinished: hello" );
            }
        });

        back.setOnClickListener(this);
        share.setOnClickListener(this);

    }
//click events
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                onBackPressed();
                break;
            case R.id.plus:

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                startActivity(sendIntent);
                break;

        }

    }
}
