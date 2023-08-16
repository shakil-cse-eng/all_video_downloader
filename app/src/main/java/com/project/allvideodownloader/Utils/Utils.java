package com.project.allvideodownloader.Utils;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.net.Uri;
import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import com.project.allvideodownloader.R;


public class Utils {
    public static String RootDirectoryYouTube = "/StatusSaver/youtube/";
    public static String RootDirectoryFacebook = "/StatusSaver/facebook/";
    public static String RootDirectoryInstagram = "/StatusSaver/instagram/";
    public static String RootDirectoryTiktok =  "/StatusSaver/tiktok/";
    public static String RootDirectoryTwitter = "/StatusSaver/twitter/";
    public static String RootDirectoryLikee = "/StatusSaver/Likee/";
    public static String RootDirectoryShareChat = "/StatusSaver/ShareChat/";


    public static void download(String downloadPath, String destinationPath, Context context, String fileName) {
        try {
            Uri uri = Uri.parse(downloadPath);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, destinationPath + fileName);
            request.setVisibleInDownloadsUi(true);
            request.allowScanningByMediaScanner();
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            downloadManager.enqueue(request);
        }catch (Exception e) {
            Looper.prepare();
            Log.d("TAG", "Message: " + e.getLocalizedMessage());
            Looper.loop();
        }

    }

    public static void setTheme(Context context, int theme) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt(context.getString(R.string.prefs_theme_key), theme).apply();
    }

    public static int getTheme(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(context.getString(R.string.prefs_theme_key), -1);
    }
}
