package com.example.chen;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private WebView mWebView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(R.id.webview);
        // 启用javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 从assets目录下面加载html
        mWebView.loadUrl("file:///android_asset/web.html");
        mWebView.addJavascriptInterface(MainActivity.this,"android");


        //Android无参调用Js
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 无参数调用
                mWebView.loadUrl("javascript:javacalljs()");

            }
        });
        //Android有参调用Js
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 传递参数调用
                mWebView.loadUrl("javascript:javacalljswith(" + "'http://www.baidu.com/'" + ")");
            }
        });


    }

    //由于安全原因 需要加 @JavascriptInterface
    @JavascriptInterface
    //JS无参调用Android
    public void startFunction(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,"show",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @JavascriptInterface
    //JS有参调用Android
    public void startFunction(final String text){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                new AlertDialog.Builder(MainActivity.this).setMessage(text).show();

            }
        });
    }
}

