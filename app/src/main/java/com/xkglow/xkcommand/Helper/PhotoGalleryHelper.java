package com.xkglow.xkcommand.Helper;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PhotoGalleryHelper implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    private Context context;
    PhotoGalleryLoaderCallback callback;

    public void startPhotoLoader(FragmentActivity context, PhotoGalleryLoaderCallback callback) {
        this.context = context;
        this.callback = callback;
        LoaderManager.getInstance(context).initLoader(++AppGlobal.loaderId, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle bundle) {
        String photo_id = MediaStore.Images.Media._ID;// "_id"
        String path = MediaStore.Images.Media.DATA;// "_data"
        String mime_type = MediaStore.Images.Media.MIME_TYPE;
        String photo_date = MediaStore.Images.Media.DATE_ADDED;// "date_added"
        String photo_width = MediaStore.Images.Media.WIDTH;// "width"
        String photo_height = MediaStore.Images.Media.HEIGHT;// "height"
        String orientation = MediaStore.Images.Media.ORIENTATION;// "orientation"
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;// getContentUri("external")
        String[] columns = {photo_id, path, mime_type, photo_date, orientation, photo_width, photo_height};
        return new CursorLoader(this.context, uri, columns, null, null, photo_date + " DESC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        List<PhotoData> photos = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                int idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                int pathColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                int mimeTypeColumn = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE);
                int orientationColumn = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);
                int widthColumn = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH);
                int heightColumn = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT);
                int dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);

                long id = cursor.getLong(idColumn);
                String path = cursor.getString(pathColumn);
                String mimeType = cursor.getString(mimeTypeColumn);
                int orientation = cursor.getInt(orientationColumn);
                int width = cursor.getInt(widthColumn);
                int height = cursor.getInt(heightColumn);
                int date = cursor.getInt(dateColumn);

                String url = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ? getRealPathAndroid_Q(id) : cursor.getString
                        (cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                if (new File(path).exists()) {
                    PhotoData photoData = new PhotoData(path, url, mimeType, orientation, width, height, date);
                    photos.add(photoData);
                }
            } while (cursor.moveToNext());
        }

        callback.photoGalleryLoaderDone(photos);
        LoaderManager.getInstance((FragmentActivity) context).destroyLoader(AppGlobal.loaderId);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public interface PhotoGalleryLoaderCallback {
        void photoGalleryLoaderDone(List<PhotoData> photos);
    }

    private String getRealPathAndroid_Q(long id) {
        return QUERY_URI.buildUpon().appendPath(id + "").build().toString();
    }

}
