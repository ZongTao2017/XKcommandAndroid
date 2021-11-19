package com.xkglow.xkcommand.Helper;

import android.net.Uri;

import java.io.Serializable;

public class PhotoData implements Serializable {
    public int resourceId;
    public String path;
    public String url;
    public String mimeType;
    public int orientation;
    public int width;
    public int height;
    public long date;

    public PhotoData(int resourceId) {
        this.resourceId = resourceId;
    }

    public PhotoData(String path, int orientation, int width, int height, long date) {
        this.path = path;
        this.orientation = orientation;
        this.width = width;
        this.height = height;
        this.date = date;
    }

    public PhotoData(String path, String url, String mimeType, int orientation, int width, int height, long date) {
        this.path = path;
        this.url = url;
        this.mimeType = mimeType;
        this.orientation = orientation;
        this.width = width;
        this.height = height;
        this.date = date;
    }
}
