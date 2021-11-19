package com.xkglow.xkcommand;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.ButtonData;
import com.xkglow.xkcommand.Helper.Helper;
import com.xkglow.xkcommand.Helper.MessageEvent;
import com.xkglow.xkcommand.Helper.PhotoData;
import com.xkglow.xkcommand.Helper.PhotoGalleryHelper;
import com.xkglow.xkcommand.Helper.PhotoLoaderHelper;
import com.xkglow.xkcommand.Helper.RecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditButtonImageActivity extends FragmentActivity {
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    ButtonData buttonData;
    int photoPosition;
    List<PhotoData> photoList;
    int number;

    static final int REQUEST_READ_EXTERNAL_STORAGE = 111;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_edit_button_icon);

        buttonData = (ButtonData) getIntent().getSerializableExtra("button");
        photoList = new ArrayList<>();
        photoPosition = -1;

        FrameLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        number = Helper.CELL_NUMBER_IN_ROW;
        if (Helper.checkIfTablet(EditButtonImageActivity.this)) {
            number = Helper.CELL_NUMBER_IN_ROW_PAD;
        }

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(EditButtonImageActivity.this, number, RecyclerView.VERTICAL, false));
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 8 * number);

        adapter = new RecyclerViewAdapter(EditButtonImageActivity.this, 1, new ArrayList<>(), number);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickLister(new RecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                photoPosition = position;
            }

            @Override
            public void onCameraClick() {
                Intent intent = new Intent(EditButtonImageActivity.this, CameraActivity.class);
                intent.putExtra("button", buttonData);
                startActivity(intent);
            }
        });

        FrameLayout done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoList != null && photoList.size() > 0 && photoPosition > -1) {
                    PhotoData photoData = photoList.get(photoPosition);
                    savePhoto(photoData.path);
                } else {
                    finish();
                }
            }
        });

        TextView title = findViewById(R.id.title);
        title.setText("Take/Select Photo");

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            startPhotoLoader(0);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(sticky = true)
    public void onEvent(MessageEvent event) {
        if (event.type == MessageEvent.MessageEventType.SET_CAMERA_PHOTO) {
            finish();
        }
    }

    private void startPhotoLoader(final int index) {
//        new PhotoGalleryHelper().startPhotoLoader(this, index, new PhotoGalleryHelper.PhotoGalleryLoaderCallback() {
//            @Override
//            public void photoGalleryLoaderDone(List<PhotoData> photos) {
//                photoList.addAll(photos);
//                photoPosition = -1;
//                adapter.addData(photos);
//                if (photos.size() == Helper.LOADER_PAGE_SIZE) {
//                    startPhotoLoader(index + 1);
//                }
//            }
//        });
        PhotoLoaderHelper.loadPhotoPage(this, -1, index, new PhotoGalleryHelper.PhotoGalleryLoaderCallback() {
            @Override
            public void photoGalleryLoaderDone(List<PhotoData> photos) {
                photoList.addAll(photos);
                adapter.addData(photos);
                if (photos.size() == PhotoLoaderHelper.PAGE_SIZE) {
                    startPhotoLoader(index + 1);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startPhotoLoader(0);
                break;
        }
    }

    private void savePhoto(String photoPath) {
        try {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bb = BitmapFactory.decodeFile(photoPath, bmOptions);
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            Bitmap b = null;
            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    b = Helper.rotateImage(bb, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    b = Helper.rotateImage(bb, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    b = Helper.rotateImage(bb, 270);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    b = bb;
            }
            int x, y, width, height;
            if (b == null) return;
            if (b.getWidth() > b.getHeight()) {
                y = 0;
                height = b.getHeight();
                x = (b.getWidth() - b.getHeight()) / 2;
                width = b.getHeight();
            } else {
                x = 0;
                width = b.getWidth();
                y = (b.getHeight() - b.getWidth()) / 2;
                height = b.getWidth();
            }
            Bitmap bitmap = Bitmap.createBitmap(b, x, y, width, height);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp;
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            File image = null;

            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            FileOutputStream out = new FileOutputStream(image);
            bitmap = Helper.getResizedBitmapWithFixedWidth(bitmap, 270);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            buttonData.imagePath = image.getAbsolutePath();
            buttonData.type = 3;
            AppGlobal.getCurrentDevice().setButton(buttonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();
    }
}


