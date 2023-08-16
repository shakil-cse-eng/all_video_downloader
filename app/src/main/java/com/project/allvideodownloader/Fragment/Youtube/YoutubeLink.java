package com.project.allvideodownloader.Fragment.Youtube;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.project.allvideodownloader.databinding.FragmentLinkBinding;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.annotation.SuppressLint;
import android.widget.Toast;

public class YoutubeLink extends Fragment {
    WebView youtubeWebView;
    private FragmentLinkBinding binding;
    WebSettings webSetting;
    boolean isLoaded;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLinkBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        youtubeWebView = binding.webViewLinkId;

        binding.browseId.setText("Browse YouTube");
        binding.browseId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.browseLayoutId.setVisibility(view.GONE);
                binding.swipeId.setVisibility(view.VISIBLE);

                checkLink();
                binding.swipeId.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                binding.swipeId.setRefreshing(false);
                                checkLink();
                                //facebookWebView.reload();
                            }
                        }, 3000);
                    }
                });
            }
        });

    }

    private void checkLink() {
        try {
            youtubeWebView.setWebViewClient(new WebViewClient());
            webSetting = youtubeWebView.getSettings();
            webSetting.setJavaScriptEnabled(true);
            youtubeWebView.getSettings().setDisplayZoomControls(true);
            youtubeWebView.getSettings().setDisplayZoomControls(true);
            shouldOverrideUrlLoading(youtubeWebView, "https://www.youtube.com/");
            if (isLoaded) {
                youtubeWebView.setWebViewClient(new WebViewClient(){
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        binding.linkMaterialId.setText(url);
                    }
                });
            }

        }catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void shouldOverrideUrlLoading(WebView view, String url) {
        if (!isConnected()) {
            isLoaded = false;
            Toast.makeText(getActivity(), "No Internet!", Toast.LENGTH_SHORT).show();
        } else {
            isLoaded = true;
            view.loadUrl(url);
        }
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != cm) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        }
        return false;

    }


}