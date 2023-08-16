
package com.project.allvideodownloader.View;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.project.allvideodownloader.AdmobAds;
import com.project.allvideodownloader.R;
import com.project.allvideodownloader.databinding.ActivityAboutUsBinding;

public class AboutUsActivity extends BaseActivity {
    private static final String TAG = TikTokActivity.class.getSimpleName();
    ActivityAboutUsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.black2));
        binding = ActivityAboutUsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        AdmobAds.loadInterstitial(AboutUsActivity.this);

        setSupportActionBar(binding.aboutUsToolbarId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.aboutUsToolbarId.setNavigationIcon(R.drawable.arrow_back);


        binding.aboutUsToolbarId.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdmobAds.showInterstitialAd(AboutUsActivity.this);
                startActivity(new Intent(AboutUsActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
                finish();
            }
        });

        binding.appVersionId.setText(getAppVersion());

        binding.emailId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendEmail = new Intent(Intent.ACTION_SENDTO);
                String mail = "mailto:" + getString(R.string.mailId);
                sendEmail.setData(Uri.parse(mail));
                startActivity(sendEmail);
            }
        });

        binding.telegramId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendtelegram = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.telegram_link)));
                startActivity(sendtelegram);
            }
        });
    }

    private String getAppVersion(){

        String result = "";
        try {
            result = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(),0)
                    .versionName;
            result = result.replaceAll("[a-zA-Z]|-","");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }
}