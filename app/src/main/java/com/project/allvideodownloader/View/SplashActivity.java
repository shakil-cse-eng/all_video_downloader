package com.project.allvideodownloader.View;

import android.os.Bundle;
import com.project.allvideodownloader.R;
import android.content.Intent;
import android.os.Handler;


public class SplashActivity extends BaseActivity {
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.black2));
        setContentView(R.layout.activity_splash);
        next();
    }

    private void next() {
        handler.postDelayed(() -> {

            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    //ArrayList<String> list = new String[]{"Admob", "Facebook", "Instagram", "Tiktok", "Twitter", "Likee"};
}