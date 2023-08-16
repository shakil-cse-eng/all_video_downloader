package com.project.allvideodownloader.Fragment.Likee;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.project.allvideodownloader.AdmobAds;
import com.project.allvideodownloader.ApiCalls.ApiCalls;
import com.project.allvideodownloader.databinding.LikeeDownloaderBinding;
import java.net.MalformedURLException;
import java.net.URL;

public class LikeeDownloader extends Fragment {
    private LikeeDownloaderBinding binding;
    String likeeUrl;

    ProgressDialog dialog;
    private View mView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = LikeeDownloaderBinding.inflate(getLayoutInflater());
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

        binding.likeeDownloadButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeeUrl = binding.likeeEditTextId.getText().toString();
                if (likeeUrl.equals("")){
                    Toast.makeText(getActivity(), "Paste Likee video link", Toast.LENGTH_SHORT).show();
                    return;
                }
                getData();
            }
        });
        binding.likeePasteLinkId.setOnClickListener(new View.OnClickListener() {
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

            binding.likeeEditTextId.setText(pasteData);
            Log.i("Tag", "text: " + pasteData);
        }
    }

    private void getData() {
        URL url;
        try {
            url = new URL(likeeUrl);
            String host = url.getHost();
            if(host.length() > 0 && host.contains("likee.video")) {
                binding.likeeEditTextId.setText("");
                dialog.show();
                ApiCalls likeeApiCall = new ApiCalls();
                likeeApiCall.DownloadLikeeVideos(getActivity(), likeeUrl, dialog);
            }
            else {
                Toast.makeText(getActivity(),"URL is invalid", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

}
