package com.xkglow.xkcommand;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.xkglow.xkcommand.Helper.AppGlobal;
import com.xkglow.xkcommand.Helper.ButtonData;
import com.xkglow.xkcommand.Helper.Helper;
import com.xkglow.xkcommand.Helper.MessageEvent;
import com.xkglow.xkcommand.Helper.PhotoData;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraActivity extends Activity {
    private FrameLayout mPreviewLayout;
    private ImageView mTakePhoto;
    private Camera mCamera;
    private CameraPreview mCameraPreview;
    private int mCameraId;
    Camera.Size previewSize, pictureSize;
    int currentRotation;
    ButtonData buttonData;

    public static final int REQUEST_CAMERA = 120;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        buttonData = (ButtonData) getIntent().getSerializableExtra("button");
        mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        FrameLayout back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPreviewLayout = findViewById(R.id.preview_layout);
        final FrameLayout bottomLayout = findViewById(R.id.bottom_layout);
        mTakePhoto = findViewById(R.id.camera_image);
        int size = Helper.getScreenWidth(this);
        int height = Helper.getScreenHeight(this);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mPreviewLayout.getLayoutParams();
        layoutParams.height = size;
        mPreviewLayout.setLayoutParams(layoutParams);
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) bottomLayout.getLayoutParams();
        layoutParams2.topMargin = (height + size) / 2;
        bottomLayout.setLayoutParams(layoutParams2);
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) mTakePhoto.getLayoutParams();
        int cameraSize = Math.max(Helper.dpToPx(this, 60), (height - size) / 4);
        layoutParams3.width = cameraSize;
        layoutParams3.height = cameraSize;
        mTakePhoto.setLayoutParams(layoutParams3);

        if (checkCameraHardware(this)) {
            ArrayList<String> permissions = new ArrayList<>();
            String permission1 = Manifest.permission.CAMERA;
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(permission1);
                }
                if (permissions.size() > 0) {
                    ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), REQUEST_CAMERA);
                }
            }
        }

        OrientationEventListener orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation >= 330 || orientation < 30) {
                    currentRotation = 90;
                } else if (orientation >= 60 && orientation < 120) {
                    currentRotation = 180;
                } else if (orientation >= 150 && orientation < 210) {
                    currentRotation = 270;
                } else if (orientation >= 240 && orientation < 300) {
                    currentRotation = 0;
                }
                Log.d("orientation", String.format("%d", currentRotation));
            }
        };
        if (orientationEventListener.canDetectOrientation())
            orientationEventListener.enable();
    }

    @Override
    protected void onPause() {
        releaseCamera();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startCamera();
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void startCamera() {
        mPreviewLayout.removeAllViews();
        mCamera = getCameraInstance();
        if (mCamera != null) {
            Camera.Parameters cameraParameters = mCamera.getParameters();
            List<Camera.Size> list = cameraParameters.getSupportedPreviewSizes();
            previewSize = list.get(0);

            List<Camera.Size> list2 = cameraParameters.getSupportedPictureSizes();
            pictureSize = list2.get(0);
            for (Camera.Size size : list2) {
                if (size.width == previewSize.width && size.height == previewSize.height) {
                    pictureSize = size;
                    break;
                }

            }
            cameraParameters.setPictureSize(pictureSize.width, pictureSize.height);
            cameraParameters.setPreviewSize(previewSize.width, previewSize.height);

            List<String> focusModes = cameraParameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                cameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }

            mCamera.setParameters(cameraParameters);
            setDisplayOrientation();

            mCameraPreview = new CameraPreview(this);
            int width = Helper.getScreenWidth(CameraActivity.this);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.width = width;
            layoutParams.height = width * previewSize.width / previewSize.height;
            layoutParams.gravity = Gravity.CENTER;
            mCameraPreview.setLayoutParams(layoutParams);
            mPreviewLayout.addView(mCameraPreview);

            mTakePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCamera != null) {
                        mCamera.takePicture(null, null, mPictureCallback);
                    }
                }
            });
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;

        public CameraPreview(Context context) {
            super(context);
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {

        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            mHolder.removeCallback(this);
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            if (mHolder.getSurface() == null) {
                return;
            }
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (Exception e) {
                Log.e("start camera preview", e.getMessage());
            }
        }
    }

    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(mCameraId);
        } catch (Exception e) {

        }
        return c;
    }

    private void setDisplayOrientation() {
        final Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, info);
        int degrees = 0;
        final int rotation = ((WindowManager)
                getSystemService(Activity.WINDOW_SERVICE)).getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int displayOrientation = (info.orientation - degrees + 360) % 360;
        mCamera.setDisplayOrientation(displayOrientation);
    }

    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            savePhoto(data, camera);
        }
    };

    private void savePhoto(byte[] data, Camera camera) {
        int rotation = currentRotation;
        Bitmap b = BitmapFactory.decodeByteArray(data, 0, data.length);
        Matrix mat = new Matrix();
        mat.postRotate(rotation);
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
        Bitmap bitmap = Bitmap.createBitmap(b, x, y, width, height, mat, true);
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
            buttonData.type = 3;
            buttonData.imagePath = image.getAbsolutePath();
            AppGlobal.setButton(buttonData);
            EventBus.getDefault().postSticky(new MessageEvent(MessageEvent.MessageEventType.SET_CAMERA_PHOTO));
        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();
    }
}
