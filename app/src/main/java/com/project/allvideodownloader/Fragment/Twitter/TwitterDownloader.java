package com.project.allvideodownloader.Fragment.Twitter;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.project.allvideodownloader.AdmobAds;
import com.project.allvideodownloader.Constant;
import com.project.allvideodownloader.Utils.Utils;
import com.project.allvideodownloader.databinding.TwitterDownloaderBinding;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import java.net.MalformedURLException;
import java.net.URL;
import retrofit2.Call;
import retrofit2.Response;


public class TwitterDownloader extends Fragment {
    TwitterDownloaderBinding binding;
    private View mView;
    private ProgressDialog dialog;
    String twitterUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = TwitterDownloaderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTwitterConfig();

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });



        mView = getActivity().getWindow().getDecorView().getRootView();

        AdmobAds.loadBanner(getActivity(), view);

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait .....");
        dialog.setCancelable(true);

        binding.twitterDownloadButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL url;
                twitterUrl = binding.twitterEditTextId.getText().toString();
                if (twitterUrl.equals("")) {
                    Toast.makeText(getActivity(), "Paste Twitter video link", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    url = new URL(twitterUrl);
                    String host = url.getHost();
                    if(host.contains("twitter.com")) {
                        Long id = getTweetId(twitterUrl);
                        if (id != null) {
                            binding.twitterEditTextId.setText("");
                            getTweet(id);
                        } else {
                            Log.d("Tag", "Error");
                        }
                    } else {
                        Toast.makeText(getActivity(),"URL is invalid", Toast.LENGTH_LONG).show();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

    }



    private Long getTweetId(String s) {
        try {
            String[] split = s.split("\\/");
            String id = split[5].split("\\?")[0];
            return Long.parseLong(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setTwitterConfig() {
        TwitterConfig config = new TwitterConfig.Builder(getContext())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(Constant.TWITTER_KEY, Constant.TWITTER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);
    }

    private void getTweet(Long id) {
        dialog.show();
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        StatusesService statusesService = twitterApiClient.getStatusesService();
        Call<Tweet> tweetCall = statusesService.show(id, null, null, null);

        tweetCall.enqueue(new retrofit2.Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                dialog.dismiss();
                //Check if media is present
                if (response.body().extendedEntities == null && response.body().entities.media == null) {
                    Log.d("TAG", "ResponseBody");
                } else if (response.body().extendedEntities != null) {
                    if (!(response.body().extendedEntities.media.get(0).type).equals("video") &&
                            !(response.body().extendedEntities.media.get(0).type).equals("animated_gif")) {
                        Log.d("TAG", "ResponseBody2");
                    } else {
                        String url;

                        if ((response.body().extendedEntities.media.get(0).type).equals("video") ||
                                (response.body().extendedEntities.media.get(0).type).equals("animated_gif")) {
                            Log.d("TAG", "ResponseBody3");

                        }

                        int i = 0;
                        url = response.body().extendedEntities.media.get(0).videoInfo.variants.get(i).url;

                        while (!url.contains(".mp4")) {
                            try {
                                if (response.body().extendedEntities.media.get(0).videoInfo.variants.get(i) != null) {
                                    url = response.body().extendedEntities.media.get(0).videoInfo.variants.get(i).url;
                                    i += 1;
                                }
                            } catch (IndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                        }

                        if (!url.equals("")) {
                            Utils.download(url, Utils.RootDirectoryTwitter+"/Video/", getActivity(), "twitter_" + System.currentTimeMillis()+".mp4");
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
            }
        });
    }


}