package com.project.allvideodownloader.Model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class ImageStatus implements Parcelable{
    private String name;
    private Uri uri;
    private String path;
    private String filename;

    public ImageStatus(String name, Uri uri, String path, String filename) {
        this.name = name;
        this.uri = uri;
        this.path = path;
        this.filename = filename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(path);
        parcel.writeString(filename);

    }
    public static final Creator<ImageStatus> CREATOR = new Creator<ImageStatus>() {
        public ImageStatus createFromParcel(Parcel in) {
            return new ImageStatus(in);
        }

        public ImageStatus[] newArray(int size) {
            return new ImageStatus[size];
        }
    };

    private ImageStatus(Parcel in) {
        name = in.readString();
        path = in.readString();
        filename = in.readString();
    }

}
