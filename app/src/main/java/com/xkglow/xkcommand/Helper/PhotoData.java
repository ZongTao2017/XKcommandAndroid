package com.xkglow.xkcommand.Helper;

public class PhotoData {
    public int resourceId;
    public long id;
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

    public PhotoData(long id, String path, String url, String mimeType, int orientation, int width, int height, long date) {
        this.id = id;
        this.path = path;
        this.url = url;
        this.mimeType = mimeType;
        this.orientation = orientation;
        this.width = width;
        this.height = height;
        this.date = date;
    }
}
