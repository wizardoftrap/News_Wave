package com.shivprakash.newswave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class NewsDetail extends AppCompatActivity {
    WebView result;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Intent intent = getIntent();
        url = intent.getStringExtra("url").toString();
        result= findViewById(R.id.googleSearch);
        result.getSettings().setJavaScriptEnabled(true);
        result.loadUrl(url);

    }
}