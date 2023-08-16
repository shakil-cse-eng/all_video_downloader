package com.project.allvideodownloader.Fragment.Youtube;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.project.allvideodownloader.AdmobAds;
import com.project.allvideodownloader.Utils.Utils;
import com.project.allvideodownloader.databinding.YoutubeDownloaderBinding;
import java.net.MalformedURLException;
import java.net.URL;
import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;


public class YoutubeDownloader extends Fragment {
    //private static final String TAG = YoutubeActivity.class.getSimpleName();
    private YoutubeDownloaderBinding binding;
    private int tag;
    String youtubeLink;
    private String ext = null;
    private ClipboardManager clipboard;
    ProgressDialog dialog;
    private View mView;


    //viewPager

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = YoutubeDownloaderBinding.inflate(getLayoutInflater());
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

        /*AdView adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");*/


        mView = getActivity().getWindow().getDecorView().getRootView();

        AdmobAds.loadBanner(getActivity(), view);

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);



        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please wait .....");
        dialog.setCancelable(true);

        binding.youtubeDownloadButtonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL url;
                youtubeLink = binding.youtubeEditTextId.getText().toString();
                if (youtubeLink.equals("")){
                    Toast.makeText(getActivity(), "Paste YouTube video link", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    url = new URL(youtubeLink);
                    String host = url.getHost();
                    if(host.contains("youtube.com") || host.contains("youtu.be")) {
                        if (binding.radBtn720.isChecked()) {
                            tag = 22;
                            ext = ".mp4";
                            binding.youtubeEditTextId.setText("");
                            DownloadMyVideo(youtubeLink, tag, ext);
                        } else if (binding.radBtn480.isChecked()) {
                            tag = 135;
                            ext = ".mp4";
                            binding.youtubeEditTextId.setText("");
                            DownloadMyVideo(youtubeLink, tag, ext);
                        } else if (binding.radBtn360.isChecked()) {
                            tag = 18;
                            ext = ".mp4";
                            binding.youtubeEditTextId.setText("");
                            DownloadMyVideo(youtubeLink, tag, ext);
                        } else if (binding.radBtnaudio.isChecked()) {
                            tag = 251;
                            ext = ".mp3";
                            binding.youtubeEditTextId.setText("");
                            DownloadMyVideo(youtubeLink, tag, ext);
                        }else {
                            Toast.makeText(getActivity(), "Please check resolution", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(),"URL is invalid", Toast.LENGTH_LONG).show();
                    }
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        binding.youtubePasteLinkId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pasteText();
            }
        });

    }

    private void pasteText() {
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        String pasteData = "";
        if (!(clipboard.hasPrimaryClip())) {

        } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
            Log.i("Tag", "text: " + pasteData);

        } else {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            pasteData = item.getText().toString();

            binding.youtubeEditTextId.setText(pasteData);
            Log.i("Tag", "text: " + pasteData);
        }
    }


    public void DownloadMyVideo(String link, int mTag, String extension) {
        dialog.show();
        @SuppressLint("StaticFieldLeak") YouTubeExtractor youTubeExtractor = new YouTubeExtractor(getContext()) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                dialog.dismiss();
                if (ytFiles != null) {
                    try {
                        String youtubeLink = ytFiles.get(mTag).getUrl();
                        String title = videoMeta.getTitle();
                        String title1 = title.replace(".", "");
                        title1 = title1.replace("#","");
                        String title2 = title1.replace(":"  ,  "");
                        String title3 = title2.replace(";"  ,  "");
                        String title4 = title3.replace("?"  ,  "");
                        String title5 = title4.replace("/"  ,  "");

                        if (!youtubeLink.equals("")) {
                            if (extension == ".mp4") {
                                Utils.download(youtubeLink, Utils.RootDirectoryYouTube+"/Video/", getActivity(),title5+".mp4");
                            } else if (extension == ".mp3") {
                                Utils.download(youtubeLink, Utils.RootDirectoryYouTube+"/Video/", getActivity(), title5+".mp3");
                            }
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        youTubeExtractor.execute(link);


    }
}