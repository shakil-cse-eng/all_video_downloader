package com.project.allvideodownloader.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.project.allvideodownloader.Adapter.ViewPagerAdapter;
import com.project.allvideodownloader.Constant;
import com.project.allvideodownloader.Fragment.Youtube.YoutubeDownloader;
import com.project.allvideodownloader.Fragment.Youtube.YoutubeLink;
import com.project.allvideodownloader.Fragment.Youtube.YoutubeVideo;
import com.project.allvideodownloader.Network.CheckInternetConnection;
import com.project.allvideodownloader.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.project.allvideodownloader.Utils.Utils;
import com.project.allvideodownloader.databinding.ActivityMainBinding;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import java.io.File;
import java.util.Objects;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    //private static final String TAG = ActivityMainBinding.class.getSimpleName();
    public static int UPDATE_CODE = 20;
    private ActivityMainBinding binding;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ViewPagerAdapter adapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    AppUpdateManager appUpdateManager;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.black2));
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ActivityCompat.requestPermissions(
                MainActivity.this,
                permissions(),
                REQUEST_CODE_STORAGE_PERMISSION
        );

        setSupportActionBar(binding.youtubeToolbarId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        binding.youtubeActionBarTitleId.setText("YouTube");

        navigationView = binding.navigationViewId;
        mDrawerLayout = binding.drawerLayoutId;
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        mTabLayout = binding.tabLayout;
        mViewPager = binding.viewPager;


        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new YoutubeDownloader(), "Link");
        adapter.addFragment(new YoutubeLink(), "Browse");
        adapter.addFragment(new YoutubeVideo(), "Videos");
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);



        MenuItem menuItem =navigationView.getMenu().findItem(R.id.nav_switch_item);
        @SuppressLint("UseSwitchCompatOrMaterialCode") SwitchCompat switchButton = (SwitchCompat) menuItem.getActionView().findViewById(R.id.darkModeSwitchId);

        SharedPreferences sharedPreferences=getSharedPreferences("save",MODE_PRIVATE);
        switchButton.setChecked(sharedPreferences.getBoolean("value",false));

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // When switch checked
                    SharedPreferences.Editor editor=getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",true);
                    editor.apply();
                    switchButton.setChecked(true);
                    Utils.setTheme(MainActivity.this, 1);
                    recreate();
                }else {
                    // When switch unchecked
                    SharedPreferences.Editor editor=getSharedPreferences("save",MODE_PRIVATE).edit();
                    editor.putBoolean("value",false);
                    editor.apply();
                    switchButton.setChecked(false);
                    Utils.setTheme(MainActivity.this, 0);
                    recreate();
                }
            }
        });


        addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        try {
                            Fragment current=((ViewPagerAdapter)(mViewPager.getAdapter())).getItem(0);
                            TextInputEditText textInputEditText= current.getView().findViewById(R.id.youtubeEditTextId);
                            textInputEditText.getText().clear();
                        }catch (Exception e) {
                            e.printStackTrace();
                        }

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };


    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Tag", "Permission Granted!");
                } else {
                    Log.d("Tag", "Permission Denied!");
                    ((ActivityManager) Objects.requireNonNull(this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
                    recreate();
                }

        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            if (getApplicationContext().getPackageName() == null) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                return true;
            }
            mDrawerLayout.closeDrawers();
        } else if (item.getItemId() == R.id.nav_downloader) {
            try {
                startActivity(new Intent(MainActivity.this, AllVideoDownloaderActivity.class));
            } finally {
                mDrawerLayout.closeDrawers();
                finish();
            }
            return true;
        }else if (item.getItemId() == R.id.nav_share) {
            final String appPackageName = MainActivity.this.getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Download Now : https://play.google.com/store/apps/details?id=" + appPackageName);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Share via"));
            mDrawerLayout.closeDrawers();
            return true;
        }else if (item.getItemId() == R.id.nav_about_us) {
            try {
                startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
            } finally {
                mDrawerLayout.closeDrawers();
                finish();
            }
            return true;
        }else if (item.getItemId() == R.id.nav_policy) {
            try {
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.PrivacyPolicyLink));
                startActivity(browserIntent2);
            } finally {
                mDrawerLayout.closeDrawers();
                finish();
            }
            return true;
        }else if (item.getItemId() == R.id.nav_app_update) {
            checkUpdate();
            mDrawerLayout.closeDrawers();
            return true;
        }else if (item.getItemId() == R.id.nav_location) {
            fileLocation();
            mDrawerLayout.closeDrawers();
            return true;
        }else if (item.getItemId() == R.id.nav_more_apps) {
            launchPlayStore(MainActivity.this, MainActivity.this.getPackageName());
            mDrawerLayout.closeDrawers();
            return true;
        }else if (item.getItemId() == R.id.nav_exit) {
            mDrawerLayout.closeDrawers();
            finish();
            return true;
        }
        return false;
    }


    public static void launchPlayStore(Context context, String packageName) {
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }


    public void fileLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.file_location, viewGroup, false);
        TextView textView = dialogView.findViewById(R.id.storageTextId);
        TextView cancel = dialogView.findViewById(R.id.storageCancelId);
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/StatusSaver/");
        textView.setText(path.getPath().toString());
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
        alertDialog.setCancelable(true);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }




    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
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


    public void checkUpdate() {
        if (CheckInternetConnection.hasInternetConnection(this)) {
            appUpdateManager = AppUpdateManagerFactory.create(this);
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, UPDATE_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        //e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            });

            appUpdateManager.registerListener(listener);
        }else {
            Toast.makeText(this, "Connect to the internet", Toast.LENGTH_SHORT).show();
        }
    }

    InstallStateUpdatedListener listener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(@NonNull InstallState installState) {
            Log.d("installState", installState.toString());
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            }
        }

    };

    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(findViewById(android.R.id.content), "An update has just been downloaded.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Restart", view -> appUpdateManager.completeUpdate());
        snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_blue_bright));
        snackbar.show();
    }
}

