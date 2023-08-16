package com.project.allvideodownloader.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.textfield.TextInputEditText;
import com.project.allvideodownloader.Adapter.ViewPagerAdapter;
import com.project.allvideodownloader.AdmobAds;
import com.project.allvideodownloader.Fragment.Facebook.FacebookDownloader;
import com.project.allvideodownloader.Fragment.Facebook.FacebookLink;
import com.project.allvideodownloader.Fragment.Facebook.FacebookVideo;
import com.project.allvideodownloader.R;
import com.google.android.material.tabs.TabLayout;
import com.project.allvideodownloader.databinding.ActivityFacebookBinding;


public class FacebookActivity extends BaseActivity {
    private static final String TAG = FacebookActivity.class.getSimpleName();
    private ActivityFacebookBinding binding;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    private TabLayout mTabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.black2));

        binding = ActivityFacebookBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });



        setSupportActionBar(binding.facebookToolbarId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.facebookToolbarId.setNavigationIcon(R.drawable.ic_arrow_back);

        binding.facebooActionBarTitleId.setText("Facebook");

        binding.facebookToolbarId.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdmobAds.showInterstitialAd(FacebookActivity.this);
                //showInterstitial();
                startActivity(new Intent(FacebookActivity.this, AllVideoDownloaderActivity.class));
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
                AdmobAds.loadInterstitial(FacebookActivity.this);
                finish();
            }
        });

        mTabLayout = binding.tabLayout;
        mViewPager = binding.viewPager;
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FacebookDownloader(), "Link");
        adapter.addFragment(new FacebookLink(), "Browse");
        adapter.addFragment(new FacebookVideo(), "Videos");
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        Fragment current=((ViewPagerAdapter)(mViewPager.getAdapter())).getItem(0);
                        TextInputEditText textInputEditText= current.getView().findViewById(R.id.facebookEditTextId);
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