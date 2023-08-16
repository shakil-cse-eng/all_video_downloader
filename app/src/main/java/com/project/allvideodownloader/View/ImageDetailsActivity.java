package com.project.allvideodownloader.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.WindowManager;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.project.allvideodownloader.Adapter.ImageFragmentAdapter;
import com.project.allvideodownloader.AdmobAds;
import com.project.allvideodownloader.Model.ImageStatus;
import com.project.allvideodownloader.R;
import java.util.ArrayList;
import java.util.Objects;

public class ImageDetailsActivity extends BaseActivity {
    ViewPager viewPager;
    ArrayList<ImageStatus> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_details);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        viewPager = findViewById(R.id.pagerId);

        arrayList = getIntent().getParcelableArrayListExtra("data");
        int pos = getIntent().getExtras().getInt("ImagePosition");
        ImageFragmentAdapter adapter = new ImageFragmentAdapter(getSupportFragmentManager(), arrayList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(pos);



    }

    @Override
    public void onBackPressed() {
        AdmobAds.loadRewarded(ImageDetailsActivity.this);
        finish();
        super.onBackPressed();
    }

}