package com.xkglow.xkcommand;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xkglow.xkcommand.Helper.ButtonData;
import com.xkglow.xkcommand.Helper.Helper;
import com.xkglow.xkcommand.Helper.PhotoData;
import com.xkglow.xkcommand.Helper.PhotoGalleryHelper;
import com.xkglow.xkcommand.Helper.RecyclerViewAdapter;

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

    static final int REQUEST_READ_EXTERNAL_STORAGE = 111;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_button_icon);

        buttonData = (ButtonData) getIntent().getSerializableExtra("button");

        FrameLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        ArrayList<PhotoData> photos = new ArrayList<>();

        recyclerView.setLayoutManager(new GridLayoutManager(EditButtonImageActivity.this, 4, RecyclerView.VERTICAL, false));

        FrameLayout done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoList != null && photoList.size() > 0 && photoPosition > -1) {
                    savePhoto(photoList.get(photoPosition).path);
                } else {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            startPhotoLoader();
        }
    }



    private void startPhotoLoader() {
        new PhotoGalleryHelper().startPhotoLoader(this, new PhotoGalleryHelper.PhotoGalleryLoaderCallback() {
            @Override
            public void photoGalleryLoaderDone(List<PhotoData> photos) {
                photoList = photos;
                photoPosition = -1;
                adapter = new RecyclerViewAdapter(EditButtonImageActivity.this, 1, photos, 4);
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
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startPhotoLoader();
                break;
        }
    }

    private void savePhoto(String photoPath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap b = BitmapFactory.decodeFile(photoPath, bmOptions);
        int x, y, width, height;

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
        try {
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
            String path = image.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();
    }
}

