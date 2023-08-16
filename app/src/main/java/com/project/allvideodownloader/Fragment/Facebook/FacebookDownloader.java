package com.project.allvideodownloader.Fragment.Facebook;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import com.project.allvideodownloader.ApiCalls.ApiCalls;
import com.project.allvideodownloader.databinding.FacebookDownloaderBinding;
import java.net.MalformedURLException;
import java.net.URL;


public class FacebookDownloader extends Fragment {
    private FacebookDownloaderBinding binding;
    String facebookUrl;
    ProgressDialog dialog;
    private View mView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FacebookDownloaderBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

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


        binding.facebookDownloadButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookUrl = binding.facebookEditTextId.getText().toString();
                if (facebookUrl.equals("")){
                    Toast.makeText(getActivity(), "Paste Facebook video link", Toast.LENGTH_SHORT).show();
                    return;
                }
                getData();
            }
        });
        binding.facebookPasteLinkId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pasteText();
            }
        });




    }
    private void pasteText() {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        String pasteData = "";
        if (!(clipboard.hasPrimaryClip())) {

        } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
            Log.i("Tag", "text: " + pasteData);
        } else {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

            pasteData = item.getText().toString();

            binding.facebookEditTextId.setText(pasteData);
            Log.i("Tag", "text: " + pasteData);
        }
    }

    private void getData(){
        URL url;
        try {
            url = new URL(facebookUrl);
            String host = url.getHost();
            if(host.contains("web.facebook.com") || host.contains("fb.watch") || host.contains("www.facebook.com")) {
                binding.facebookEditTextId.setText("");
                dialog.show();
                ApiCalls facebookApiCall = new ApiCalls();
                facebookApiCall.DownloadFbVideos(getActivity(), facebookUrl, dialog);
            } else {
                Toast.makeText(getActivity(),"URL is invalid", Toast.LENGTH_LONG).show();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}