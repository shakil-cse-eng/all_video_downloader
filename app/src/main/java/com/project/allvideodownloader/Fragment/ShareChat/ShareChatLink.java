package com.project.allvideodownloader.Fragment.ShareChat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.project.allvideodownloader.databinding.FragmentLinkBinding;

public class ShareChatLink extends Fragment {
    WebView sharechatWebView;
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

        sharechatWebView = binding.webViewLinkId;

        binding.browseId.setText("Browse ShareChat");
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
            sharechatWebView.setWebViewClient(new WebViewClient());
            webSetting = sharechatWebView.getSettings();
            webSetting.setJavaScriptEnabled(true);
            sharechatWebView.getSettings().setDisplayZoomControls(true);
            sharechatWebView.getSettings().setDisplayZoomControls(true);
            shouldOverrideUrlLoading(sharechatWebView, "https://sharechat.com/");
            if (isLoaded) {
                sharechatWebView.setWebViewClient(new WebViewClient(){
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
