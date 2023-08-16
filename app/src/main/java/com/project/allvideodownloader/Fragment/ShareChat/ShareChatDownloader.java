package com.project.allvideodownloader.Fragment.ShareChat;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
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
import com.project.allvideodownloader.Utils.Utils;
import com.project.allvideodownloader.databinding.ShareChatDownloaderBinding;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ShareChatDownloader extends Fragment {
    private ShareChatDownloaderBinding binding;
    String shareChatUrl;

    ProgressDialog dialog;
    private View mView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ShareChatDownloaderBinding.inflate(getLayoutInflater());
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

        binding.shareChatDownloadButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareChatUrl = binding.shareChatEditTextId.getText().toString();
                if (shareChatUrl.equals("")){
                    Toast.makeText(getActivity(), "Paste ShareChat video link", Toast.LENGTH_SHORT).show();
                    return;
                }

                getShareChatData1();
            }
        });
        binding.shareChatPasteLinkId.setOnClickListener(new View.OnClickListener() {
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

            binding.shareChatEditTextId.setText(pasteData);
            Log.i("Tag", "text: " + pasteData);
        }
    }






    private  void getShareChatData1() {
        URL url;
        try {
            url = new URL(shareChatUrl);
            String host = url.getHost();
            if(host.contains("sharechat.com")) {
                binding.shareChatEditTextId.setText("");
                dialog.show();
                new getShareChatData2().execute(shareChatUrl);
            } else {
                Toast.makeText(getActivity(),"URL is invalid", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    class getShareChatData2 extends AsyncTask<String, Void, Document> {
        Document scDoc;

        @Override
        protected Document doInBackground(String... strings) {
            dialog.dismiss();
            try {
                scDoc = Jsoup.connect(strings[0])
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return scDoc;
        }

        @Override
        protected void onPostExecute(Document document) {
            super.onPostExecute(document);
            dialog.dismiss();
            String videoURL = "";

            try {
                videoURL = document.select("meta[property=\"og:video:secure_url\"]").last().attr("content");
                if(!videoURL.equals("")) {
                    Utils.download(videoURL, Utils.RootDirectoryShareChat+"/Video/", getActivity(),"share_chat_" + System.currentTimeMillis()+".mp4");
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
