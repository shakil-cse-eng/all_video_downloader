package com.project.allvideodownloader.View;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.project.allvideodownloader.AdmobAds;
import com.project.allvideodownloader.R;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import com.project.allvideodownloader.databinding.ActivityVideoPlayerBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;

import java.io.File;

public class VideoPlayerActivity extends AppCompatActivity {
    ActivityVideoPlayerBinding binding;
    private long playbackPosition = 0L;
    private ExoPlayer exoPlayer;
    private ImageView imageViewFullScreen;
    private ImageView imageViewLock;
    private LinearLayout linearLayoutControlUp;
    private LinearLayout linearLayoutControlBottom;
    private String videoPath ="";
    Uri URL;
    private static boolean isFullScreen;
    private static boolean isLock;
    private static final long INCREMENT_MILLIS = 5000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        videoPath = getIntent().getStringExtra("VideoPath");

        URL = Uri.parse(videoPath);
        setView();
        preparePlayer();
        setFindViewById();
        setLockScreen();
        setFullScreen();

    }

    private final void setView() {
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
    }

    private void setFindViewById() {
        imageViewFullScreen = findViewById(R.id.imageViewFullScreen);
        imageViewLock = findViewById(R.id.imageViewLock);
        linearLayoutControlUp = findViewById(R.id.linearLayoutControlUp);
        linearLayoutControlBottom = findViewById(R.id.linearLayoutControlBottom);
    }

    private void preparePlayer() {
        exoPlayer = new ExoPlayer.Builder(this).setSeekBackIncrementMs(INCREMENT_MILLIS)
                .setSeekForwardIncrementMs(INCREMENT_MILLIS)
                .build();
        DefaultHttpDataSource.Factory defaultHttpDataSourceFactory = new DefaultHttpDataSource.Factory();
        MediaItem mediaItem = MediaItem.fromUri(URL);
        if (exoPlayer != null) {
            exoPlayer.setMediaItem(mediaItem);
            binding.player.setPlayer(exoPlayer);
            exoPlayer.seekTo(playbackPosition);
            exoPlayer.setPlayWhenReady(true);
            exoPlayer.prepare();
        }
    }

    private void lockScreen(boolean lock) {
        if (lock) {
            linearLayoutControlUp.setVisibility(View.INVISIBLE);
            linearLayoutControlBottom.setVisibility( View.INVISIBLE);
        } else {
            linearLayoutControlUp.setVisibility(View.VISIBLE);
            linearLayoutControlBottom.setVisibility(View.VISIBLE);
        }
    }

    private void setLockScreen() {
        imageViewLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLock) {
                    imageViewLock.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_lock));
                } else {imageViewLock.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_lock_open));
                }
                isLock = !isLock;
                lockScreen(isLock);
            }
        });
    }

    private void setFullScreen() {
        imageViewFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFullScreen) {
                    imageViewFullScreen.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_fullscreen_exit));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                } else {
                    imageViewFullScreen.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_fullscreen));
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }
                isFullScreen = !isFullScreen;
            }
        });
    }


    public void onBackPressed() {
        if (!isLock) {
            Resources resources = getResources();
            if (resources.getConfiguration().orientation == 2) {
                ImageView imageView = this.imageViewFullScreen;
                imageView.performClick();
            } else {
                AdmobAds.loadRewarded(VideoPlayerActivity.this);
                super.onBackPressed();
            }

        }
    }

    protected void onStop() {
        super.onStop();
        if (exoPlayer != null) {
            exoPlayer.stop();
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
        }

    }

    protected void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            exoPlayer.pause();
        }

    }
}