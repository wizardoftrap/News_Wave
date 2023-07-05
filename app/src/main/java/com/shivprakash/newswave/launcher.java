package com.shivprakash.newswave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class launcher extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIME_OUT = 5100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        VideoView logo= findViewById(R.id.videoView);
        String videoUrl ="android.resource://" + getPackageName() + "/" + R.raw.logo;
        logo.setVideoURI( Uri.parse(videoUrl));
        logo.start();
       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(launcher.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}