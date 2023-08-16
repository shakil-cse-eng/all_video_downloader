package com.project.allvideodownloader.View;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.project.allvideodownloader.Adapter.ViewPagerAdapter;
import com.project.allvideodownloader.AdmobAds;
import com.project.allvideodownloader.Fragment.Likee.LikeeDownloader;
import com.project.allvideodownloader.Fragment.Likee.LikeeLink;
import com.project.allvideodownloader.Fragment.Likee.LikeeVideo;
import com.project.allvideodownloader.R;
import com.project.allvideodownloader.databinding.ActivityLikeeBinding;

public class LikeeActivity extends BaseActivity {
    private static final String TAG = TikTokActivity.class.getSimpleName();
    private ActivityLikeeBinding binding;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private TabLayout mTabLayout;
    private long back_pressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.black2));
        binding = ActivityLikeeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        setSupportActionBar(binding.tiktokToolbarId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.tiktokToolbarId.setNavigationIcon(R.drawable.ic_arrow_back);
        binding.youtubeActionBarTitleId.setText("Likee");


        binding.tiktokToolbarId.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LikeeActivity.this, AllVideoDownloaderActivity.class));
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
                AdmobAds.loadInterstitial(LikeeActivity.this);
                finish();
            }
        });

        mTabLayout = binding.tabLayout;
        mViewPager = binding.viewPager;

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LikeeDownloader(), "Link");
        adapter.addFragment(new LikeeLink(), "Browse");
        adapter.addFragment(new LikeeVideo(), "Videos");
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        Fragment current=((ViewPagerAdapter)(mViewPager.getAdapter())).getItem(0);
                        TextInputEditText textInputEditText= current.getView().findViewById(R.id.likeeEditTextId);
                        textInputEditText.getText().clear();
                        break;
                }
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }

        });

    }

    @Override
    public void onBackPressed() {
        switch (mViewPager.getCurrentItem()){
            case 0:
                finish();
                return;
            case 1:
                mViewPager.setCurrentItem(0);
                return;
            case 2:
                mViewPager.setCurrentItem(1);
                return;
        }
        super.onBackPressed();
    }

}