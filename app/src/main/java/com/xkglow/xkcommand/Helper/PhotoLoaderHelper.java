package com.xkglow.xkcommand.Helper;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhotoLoaderHelper {
    public static final int PAGE_SIZE = 16;
    private static final Uri QUERY_URI = MediaStore.Files.getContentUri("external");
    private static final String COLUMN_BUCKET_ID = "bucket_id";

    private static final String[] PROJECTION_PAGE = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.WIDTH,
            MediaStore.MediaColumns.HEIGHT,
            MediaStore.MediaColumns.DURATION,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.DISPLAY_NAME,
            COLUMN_BUCKET_ID,
            MediaStore.MediaColumns.DATE_ADDED,
            MediaStore.MediaColumns.ORIENTATION,
            MediaStore.MediaColumns.RESOLUTION
    };

    public static void loadPhotoPage(Context context, int bucketId, int page, PhotoGalleryHelper.PhotoGalleryLoaderCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Cursor data = null;
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        Bundle queryArgs = createQueryArgsBundle(getPageSelection(bucketId), getPageSelectionArgs(bucketId), PAGE_SIZE, page * PAGE_SIZE);
                        data = context.getContentResolver().query(QUERY_URI, PROJECTION_PAGE, queryArgs, null);
                    } else {
                        String orderBy = page == -1 ? MediaStore.Files.FileColumns._ID + " DESC" : MediaStore.Files.FileColumns._ID + " DESC limit " + PAGE_SIZE + " offset " + page * PAGE_SIZE;
                        data = context.getContentResolver().query(QUERY_URI, PROJECTION_PAGE, getPageSelection(bucketId), getPageSelectionArgs(bucketId), orderBy);
                    }
                    final List<PhotoData> photos = new ArrayList<>();
                    if (data != null) {
                        if (data.getCount() > 0) {
                            int dataColumn = data.getColumnIndexOrThrow(PROJECTION_PAGE[1]);
                            int widthColumn = data.getColumnIndexOrThrow(PROJECTION_PAGE[3]);
                            int heightColumn = data.getColumnIndexOrThrow(PROJECTION_PAGE[4]);
                            int dateAddedColumn = data.getColumnIndexOrThrow(PROJECTION_PAGE[10]);
                            int orientationColumn = data.getColumnIndexOrThrow(PROJECTION_PAGE[11]);
                            data.moveToFirst();
                            do {
                                String absolutePath = data.getString(dataColumn);
                                if (!isFileExists(absolutePath)) {
                                    continue;
                                }
                                int width = data.getInt(widthColumn);
                                int height = data.getInt(heightColumn);
                                long date = data.getLong(dateAddedColumn);
                                int orientation = data.getInt(orientationColumn);

                                PhotoData photoData = new PhotoData(absolutePath, orientation, width, height, date);
                                photos.add(photoData);
                            } while (data.moveToNext());
                        }
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.photoGalleryLoaderDone(photos);
                        }
                    });
                } catch (Exception e) {
                    Log.e("load photo error", e.getMessage());
                } finally {
                    if (data != null && !data.isClosed()) {
                        data.close();
                    }
                }
            }
        });
    }

    public static Bundle createQueryArgsBundle(String selection, String[] selectionArgs, int limitCount, int offset) {
        Bundle queryArgs = new Bundle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection);
            queryArgs.putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, selectionArgs);
            queryArgs.putString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER, MediaStore.Files.FileColumns._ID + " DESC");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                queryArgs.putString(ContentResolver.QUERY_ARG_SQL_LIMIT, limitCount + " offset " + offset);
            }
        }
        return queryArgs;
    }



    private static String[] getPageSelectionArgs(long bucketId) {
        return bucketId == -1 ? new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)} : new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE), Long.toString(bucketId)};
    }

    private static String getPageSelection(long bucketId) {
        String sizeCondition = getFileSizeCondition();
        String queryMimeCondition = getQueryMimeCondition();
        return getPageSelectionArgsForImageMediaCondition(bucketId, queryMimeCondition, sizeCondition);
    }

    private static String getFileSizeCondition() {
        return String.format("%d <%s " + MediaStore.MediaColumns.SIZE + " and " + MediaStore.MediaColumns.SIZE + " <= %d",
                1024, "=", Long.MAX_VALUE);
    }

    private static String getQueryMimeCondition() {
        return "";
    }

    private static String getPageSelectionArgsForImageMediaCondition(long bucketId, String queryMimeCondition, String sizeCondition) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(").append(MediaStore.Files.FileColumns.MEDIA_TYPE).append("=?");
        if (bucketId == -1) {
            return stringBuilder.append(queryMimeCondition).append(") AND ").append(sizeCondition).toString();
        } else {
            return stringBuilder.append(queryMimeCondition).append(") AND ").append(COLUMN_BUCKET_ID).append("=? AND ").append(sizeCondition).toString();
        }
    }

    public static String getRealPathUri(long id) {
        Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        return ContentUris.withAppendedId(contentUri, id).toString();
    }

    public static boolean isFileExists(String path) {
        return TextUtils.isEmpty(path) || new File(path).exists();
    }
}
