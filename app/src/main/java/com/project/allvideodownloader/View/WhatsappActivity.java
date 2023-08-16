package com.project.allvideodownloader.View;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.viewpager.widget.ViewPager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.project.allvideodownloader.Adapter.ViewPagerAdapter;
import com.project.allvideodownloader.AdmobAds;
import com.project.allvideodownloader.Fragment.Whatsapp.ImageFragment;
import com.project.allvideodownloader.Fragment.Whatsapp.VideoFragment;
import com.project.allvideodownloader.R;
import com.google.android.material.tabs.TabLayout;
import com.hbb20.CountryCodePicker;
import com.project.allvideodownloader.databinding.ActivityWhatsappBinding;
import android.app.AlertDialog;

public class WhatsappActivity extends BaseActivity {
    private static final String TAG = WhatsappActivity.class.getSimpleName();
    private ActivityWhatsappBinding binding;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    CountryCodePicker codePicker;
    Intent mIntent;

    static EditText userPhoneNumber;
    EditText message;
    TextView whatsAppMessageText, messageSendButton;
    LinearLayout dialogClose;

    ViewPagerAdapter adapter;

    String defaultApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.black2));
        binding = ActivityWhatsappBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        setSupportActionBar(binding.whatsAppToolbarId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.whatsAppToolbarId.setNavigationIcon(R.drawable.ic_arrow_back);
        binding.whatsAppActionBarTitleId.setText("WhatsApp");

        binding.whatsAppToolbarId.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdmobAds.showRewardedAd(WhatsappActivity.this);
                startActivity(new Intent(WhatsappActivity.this, AllVideoDownloaderActivity.class));
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
                AdmobAds.loadRewarded(WhatsappActivity.this);
                finish();
            }
        });

        mTabLayout = binding.whatsAppTabLayoutId;
        mViewPager = binding.whatsAppViewPagerId;


        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new VideoFragment(), "Videos");
        adapter.addFragment(new ImageFragment(), "Images");
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private boolean isAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public void onLunchAnotherApp(String appPackageName) {
        mIntent = getPackageManager().getLaunchIntentForPackage(appPackageName);
        if (mIntent != null) {
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mIntent);
        } else {
            onGoToAnotherInAppStore(mIntent, appPackageName);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.whats_app_menu, menu);

        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_whatsapp) {
            String appPackage = "";
            if (isAppInstalled("com.whatsapp.w4b")) {
                appPackage = "com.whatsapp.w4b";
                onLunchAnotherApp(appPackage);
            } else if (isAppInstalled("com.whatsapp")) {
                appPackage = "com.whatsapp";
                onLunchAnotherApp(appPackage);
            } else {
                Toast.makeText(WhatsappActivity.this, "WhatsApp not installed", Toast.LENGTH_LONG).show();
            }
            return true;
        } else if (item.getItemId() == R.id.menu_whatsapp_message) {
            MessageSendtoWhatsApp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onGoToAnotherInAppStore(Intent intent, String appPackageName) {
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + appPackageName));
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName));
            startActivity(intent);
        }
    }

    private void MessageSendtoWhatsApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WhatsappActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.whatsapp_messgae, viewGroup, false);

        //CountryCodePicker ccp = dialogView.findViewById(R.id.ccp);
        codePicker= dialogView.findViewById(R.id.countryCode);
        userPhoneNumber = dialogView.findViewById(R.id.phoneNumberEdit);
        message = dialogView.findViewById(R.id.message);
        whatsAppMessageText = dialogView.findViewById(R.id.whatsAppMessageId);
        messageSendButton = dialogView.findViewById(R.id.shareButtonId);
        dialogClose = dialogView.findViewById(R.id.dialogCloseId);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(true);


        whatsAppMessageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(userPhoneNumber.getText().toString())){
                    Toast.makeText(WhatsappActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                }else {
                    defaultApp = "WhatsApp";

                    boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp", WhatsappActivity.this);
                    boolean isWhatsappBusinessInstalled = whatsappInstalledOrNot("com.whatsapp.w4b", WhatsappActivity.this);


                    if (isWhatsappBusinessInstalled || isWhatsappInstalled) {

                        try {

                            Intent sendIntent = new Intent("android.intent.action.MAIN");
                            sendIntent.setAction(Intent.ACTION_VIEW);

                            if (defaultApp.equalsIgnoreCase("WhatsApp")) {
                                sendIntent.setPackage("com.whatsapp");
                                Log.e("message", "WhatsApp");
                            } else if (defaultApp.equalsIgnoreCase("BusinessWhatsApp")) {
                                sendIntent.setPackage("com.whatsapp.w4b");
                                Log.e("message", "Business WhatsApp");
                            }

                            codePicker.registerCarrierNumberEditText(userPhoneNumber);
                            String messageText = message.getText().toString();

                            sendIntent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + codePicker.getFullNumberWithPlus() + "&text=" + messageText));
                            startActivity(sendIntent);
                            alertDialog.dismiss();

                        } catch (Exception e) {
                            Log.e("message", e.getMessage());
                            alertDialog.dismiss();
                        }

                    } else {
                        Toast.makeText(WhatsappActivity.this, "WhatsApp Not Install!", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }

                }
                //alertDialog.dismiss();
            }
        });

        messageSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(userPhoneNumber.getText().toString())){
                    Toast.makeText(WhatsappActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                }else {
                    String phnNumberText = userPhoneNumber.getText().toString();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "https://api.whatsapp.com/send?phone=+" + phnNumberText);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                    alertDialog.dismiss();
                }
            }
        });

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    public static boolean whatsappInstalledOrNot(String uri, Context context) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }






}