package com.project.allvideodownloader.Model;

import android.net.Uri;

public class VideoStatus {
    private long id = -1;
    private String name;
    private Uri uri;
    private String path;
    private int progress = 0;
    private String filename;
    private Long duration;
    private String size;
    private String completeSize;
    private Long date;
    private boolean isSelected = false;

    public VideoStatus() {
    }

    public VideoStatus(String name, Uri uri, String path, String filename, Long duration, String size, Long date) {
        this.name = name;
        this.uri = uri;
        this.path = path;
        this.filename = filename;
        this.duration = duration;
        this.size = size;
        this.date = date;
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

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
