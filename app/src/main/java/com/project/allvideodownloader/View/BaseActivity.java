package com.project.allvideodownloader.View;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import com.project.allvideodownloader.Utils.Utils;
import com.project.allvideodownloader.R;

public class BaseActivity extends AppCompatActivity {
    private final static int THEME_GREEN = 0;
    private final static int THEME_BLACK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTheme();
    }
    public void updateTheme() {
        if (Utils.getTheme(getApplicationContext()) == THEME_GREEN) {
            setTheme(R.style.Theme_AllVideoDownloader);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //getWindow().setStatusBarColor(getResources().getColor(R.color.black2));
        } else if (Utils.getTheme(getApplicationContext()) == THEME_BLACK) {
            setTheme(R.style.OverlayThemeBlack);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //getWindow().setStatusBarColor(getResources().getColor(R.color.black2, this.getTheme()));
        }

    }

}