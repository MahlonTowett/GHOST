package com.towett.ghost;



import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.io.IOException;
import java.security.Policy;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private CameraManager cameraManager;
    private boolean isFlashlightOn = false;
    private ImageButton button;
    private static final int CAMERA_REQUEST_CODE = 100;
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer,mediaPlayer1;
    private InterstitialAd mInterstitialAd;

public MainActivity(){}

    private void startBeep() {
        if (mediaPlayer == null && mediaPlayer1 == null) {
            mediaPlayer = MediaPlayer.create(this,R.raw.beep);
            mediaPlayer1 = MediaPlayer.create(this, R.raw.ll);


            mediaPlayer.setLooping(true);
            mediaPlayer1.setLooping(true);


        }

        mediaPlayer1.start();
        mediaPlayer.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startBeep();
        loadAd();

    button=(ImageButton)findViewById(R.id.home);
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.

onStop();
                        finish();

                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.

                        Log.d("TAG", "The ad failed to show.");
                    }

                });

            } else {
                loadAd();
                //load add again as above and visit second activity
onStop();

                finish();
            }
        }
    });
















        initializeCamera();
    }




    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
            // Add the RippleView as an overlay on the camera preview
            // Start the ripple animation




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }


    private void initCameraParams(Camera camera) {
        if (camera != null) {
                Camera.Parameters parameters = camera.getParameters();
            Camera.Parameters params = camera.getParameters();
            List<Camera.Size> sizes = params.getSupportedPreviewSizes();

            // Select the preview size that matches the device's aspect ratio
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point screenResolution = new Point();
            display.getSize(screenResolution);
            int screenWidth = screenResolution.x;
            int screenHeight = screenResolution.y;
            float screenAspectRatio = (float) screenWidth / (float) screenHeight;

            Camera.Size optimalSize = null;
            float diff = Float.POSITIVE_INFINITY;
            for (Camera.Size size : sizes) {
                float sizeAspectRatio = (float) size.width / (float) size.height;
                if (Math.abs(screenAspectRatio - sizeAspectRatio) < diff) {
                    optimalSize = size;
                    diff = Math.abs(screenAspectRatio - sizeAspectRatio);
                }
            }

            // Set the preview size and adjust the display orientation
            params.setPreviewSize(optimalSize.width, optimalSize.height);
            camera.setParameters(params);
            setCameraDisplayOrientation(this, 0, camera);

        } else {
            Log.e(TAG, "Camera object is null.");
        }



    }


    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.e(TAG, "Error opening camera: " + e.getMessage());
        }
        return camera;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (checkCameraHardware(this)) {
            camera = getCameraInstance();

            if (camera != null) {
                camera.startPreview();
                initCameraParams(camera);
            }
            //initCameraParams(camera);





            SurfaceView surfaceView = findViewById(R.id.camera_preview);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            if (camera != null) {
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.startPreview();
                } catch (IOException e) {
                    Log.e(TAG, "Error setting camera preview: " + e.getMessage());
                }
            }
        } else {
            Toast.makeText(this, "No camera detected on device", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeCamera();
            } else {
                Toast.makeText(this, "Camera permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initializeCamera() {
        surfaceView =  findViewById(R.id.camera_preview);
        surfaceView.setBackgroundColor(Color.parseColor("#5000FF00"));
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = parameters.getSupportedPreviewSizes().get(0);
        parameters.setPreviewSize(size.width, size.height);
        camera.setParameters(parameters);
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mediaPlayer1 != null) {
            mediaPlayer1.stop();
            mediaPlayer1.release();
            mediaPlayer1 = null;
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mediaPlayer1 != null) {
            mediaPlayer1.stop();
            mediaPlayer1.release();
            mediaPlayer1 = null;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Stop the ripple animation

    }
    @Override
    public void onBackPressed() {
        // Do nothing
    }
    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                "ca-app-pub-3799945535527779/7935504181",
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.

                        MainActivity.this.mInterstitialAd = interstitialAd;
                        Log.i(String.valueOf(TAG), "onAdLoaded");

                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MainActivity.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MainActivity.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error

                        Log.i(String.valueOf(TAG), loadAdError.getMessage());
                        mInterstitialAd = null;

                        String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());

                    }
                });
    }

}
