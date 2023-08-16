package com.project.allvideodownloader.Fragment.Instagram;

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
import com.project.allvideodownloader.databinding.InstagramDownloaderBinding;
import java.net.MalformedURLException;
import java.net.URL;


public class InstagramDownloader extends Fragment {
    private InstagramDownloaderBinding binding;
    private ProgressDialog dialog;
    String instagramUrl;
    private View mView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = InstagramDownloaderBinding.inflate(getLayoutInflater());
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

        binding.instagramDownloadButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                instagramUrl = binding.instagramEditTextId.getText().toString();
                if (instagramUrl.equals("")){
                    Toast.makeText(getActivity(), "Paste Instagram video link", Toast.LENGTH_SHORT).show();
                    return;
                }
                getData();
            }
        });
        binding.instagramPasteLinkId.setOnClickListener(new View.OnClickListener() {
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

            binding.instagramEditTextId.setText(pasteData);
            Log.i("Tag", "text: " + pasteData);
        }
    }

    private void getData(){
        URL url;
        try {
            url = new URL(instagramUrl);
            String host = url.getHost();
            if(host.contains("instagram.com")) {
                binding.instagramEditTextId.setText("");
                dialog.show();
                ApiCalls instagramApiCall = new ApiCalls();
                instagramApiCall.DownloadInstaVideos(getActivity(), instagramUrl, dialog);
            } else {
                Toast.makeText(getActivity(),"URL is invalid", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}