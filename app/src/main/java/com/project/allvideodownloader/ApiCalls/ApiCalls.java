package com.project.allvideodownloader.ApiCalls;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.project.allvideodownloader.Utils.Utils;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiCalls {
    public final void DownloadLikeeVideos(Context context, String LikeeUrl, ProgressDialog dialog) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://likee-downloader-download-likee-videos.p.rapidapi.com/postid"+"?url=" + LikeeUrl)
                .get()
                .addHeader(
                        "X-RapidAPI-Key",
                        "b8228897e5msh87904000b031901p1d66c7jsnfe8a146758bc")
                .addHeader(
                        "X-RapidAPI-Host",
                        "likee-downloader-download-likee-videos.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                dialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    assert responseBody != null;
                    String myResponse = responseBody.string();
                    JSONObject jObj, jsonObject;
                    JSONArray jsonArray;
                    String url;

                    try {
                        jObj = new JSONObject(myResponse);
                        jsonArray = jObj.getJSONArray("data");
                        if (!jsonArray.toString().equals("[]")) {
                            jsonObject = jsonArray.getJSONObject(0);
                            url = jsonObject.getString("videoUrl");
                            if (!url.equals("")) {
                                Utils.download(url, Utils.RootDirectoryLikee+"/Video/", context,"likee_" + System.currentTimeMillis()+".mp4");
                            }
                        }else {
                            Looper.prepare();
                            Toast.makeText(context, "Error While Parsing Url", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }




    public final void DownloadTikTokVideos(Context context, String tikUrl, ProgressDialog dialog) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://tiktok-downloader-download-tiktok-videos-without-watermark.p.rapidapi.com/vid/index"+"?url=" + tikUrl)
                .get()
                .addHeader(
                        "X-RapidAPI-Key",
                        "6006d97495msh704b5f2cab5cd7ep12eb79jsnabb6766fe157")
                .addHeader(
                        "X-RapidAPI-Host",
                        "tiktok-downloader-download-tiktok-videos-without-watermark.p.rapidapi.com")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                dialog.dismiss();
                e.printStackTrace();
                //Looper.prepare();
                //Toast.makeText(context, "Error While Parsing Url", Toast.LENGTH_SHORT).show();
                //Looper.loop();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String myResponse = responseBody.string();
                    JSONObject jObj;
                    JSONArray jsonArray;
                    String url;

                    try {
                        jObj = new JSONObject(myResponse);
                        jsonArray = jObj.getJSONArray("video");
                        url = jsonArray.getString(0);
                        if (!url.equals("")) {
                            Utils.download(url, Utils.RootDirectoryTiktok+"/Video/", context,"tiktok_" + System.currentTimeMillis()+".mp4");
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public final void DownloadInstaVideos(Context context, String instaUrl, ProgressDialog dialog) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://instagram-downloader-download-instagram-videos-stories.p.rapidapi.com/index"+"?url=" + instaUrl)
                .get()
                .addHeader(
                        "X-RapidAPI-Key",
                        "b8228897e5msh87904000b031901p1d66c7jsnfe8a146758bc"
                )
                .addHeader(
                        "X-RapidAPI-Host",
                        "instagram-downloader-download-instagram-videos-stories.p.rapidapi.com"
                )
                .build();

        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                dialog.dismiss();
                e.printStackTrace();
            }

            public void onResponse(Call call, Response response) throws IOException {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String myResponse2 = responseBody.string();
                    JSONObject insObj;
                    String insUrl;

                    try {
                        insObj = new JSONObject(myResponse2);
                        insUrl = insObj.getString("media");
                        if (!insUrl.equals("")) {
                            Utils.download(insUrl, Utils.RootDirectoryInstagram+"/Video/", context, "instagram_"+System.currentTimeMillis()+".mp4");
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });
    }

    public final void DownloadFbVideos(Context context, String Fb_Url, ProgressDialog dialog) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://facebook-reel-and-video-downloader.p.rapidapi.com/app/main.php/"+"?url=" + Fb_Url)
                .get()
                .addHeader(
                        "X-RapidAPI-Key",
                        "b8228897e5msh87904000b031901p1d66c7jsnfe8a146758bc"
                )
                .addHeader(
                        "X-RapidAPI-Host",
                        "facebook-reel-and-video-downloader.p.rapidapi.com"
                )
                .build();

        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                dialog.dismiss();
                e.printStackTrace();
            }

            public void onResponse(Call call, Response response) throws IOException {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String myResponse = responseBody.string();
                    JSONObject jObj;
                    JSONObject video;
                    String sdVideo;

                    try {
                        jObj = new JSONObject(myResponse);
                        if (myResponse.contains("Download High Quality") && myResponse.contains("Download Low Quality")){
                            video = jObj.getJSONObject("links");
                            sdVideo = video.getString("Download High Quality");
                            if (!sdVideo.equals("")) {
                                Utils.download(sdVideo, Utils.RootDirectoryFacebook+"/Video/", context,"facebook_" + System.currentTimeMillis()+".mp4");
                            }
                        }else if(myResponse.contains("Download Low Quality") && !myResponse.contains("Download High Quality")) {
                            video = jObj.getJSONObject("links");
                            sdVideo = video.getString("Download High Quality");
                            if (!sdVideo.equals("")) {
                                Utils.download(sdVideo, Utils.RootDirectoryFacebook+"/Video/", context,"facebook_"+ System.currentTimeMillis()+".mp4");
                            }
                        }else {
                            Looper.prepare();
                            Toast.makeText(context, "Video Not Found", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });
    }
}
