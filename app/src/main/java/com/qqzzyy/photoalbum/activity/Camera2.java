package com.qqzzyy.photoalbum.activity;

import android.annotation.SuppressLint;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.Display;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.qqzzyy.photoalbum.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class Camera2 extends AppCompatActivity implements LifecycleOwner {
    private TextureView viewFinder;
    private HashMap findViewCache;
    
    private static final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA"};

    // $FF: synthetic method
    public static String[] getREQUIRED_PERMISSIONS() {
        return REQUIRED_PERMISSIONS;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_camera2);
        View view = this.findViewById(R.id.view_finder);
        this.viewFinder = (TextureView)view;
        TextureView viewFinder;
        if (this.allPermissionsGranted()) {
            viewFinder = this.viewFinder;

            viewFinder.post(new Runnable() {
                public final void run() {
                    Camera2.this.startCamera();
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, getREQUIRED_PERMISSIONS(), 10);
        }

        viewFinder = this.viewFinder;

        viewFinder.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            public final void onLayoutChange(View $noName_0, int $noName_1, int $noName_2, int $noName_3, int $noName_4, int $noName_5, int $noName_6, int $noName_7, int $noName_8) {
                Camera2.this.updateTransform();
            }
        });
    }

    private void startCamera() {
        PreviewConfig.Builder previewConfigBuilder = new PreviewConfig.Builder();
        previewConfigBuilder.setTargetAspectRatio(new Rational(1, 1));
        previewConfigBuilder.setTargetResolution(new Size(960, 960));
        PreviewConfig previewConfig = previewConfigBuilder.build();
        Preview preview = new Preview(previewConfig);
        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            public final void onUpdated(Preview.PreviewOutput it) {
                ViewParent viewFinder = Camera2.getViewFinder(Camera2.this).getParent();
                    ViewGroup parent = (ViewGroup)viewFinder;
                    parent.removeView(Camera2.getViewFinder(Camera2.this));
                    parent.addView(Camera2.getViewFinder(Camera2.this), 0);
                    TextureView strings = Camera2.getViewFinder(Camera2.this);
                    strings.setSurfaceTexture(it.getSurfaceTexture());
                    Camera2.this.updateTransform();
                }
        });
        androidx.camera.core.ImageCaptureConfig.Builder imageCaptureConfigBuilder = new androidx.camera.core.ImageCaptureConfig.Builder();
        imageCaptureConfigBuilder.setTargetAspectRatio(new Rational(1, 1));
        imageCaptureConfigBuilder.setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY);
        ImageCaptureConfig imageCaptureConfig = imageCaptureConfigBuilder.build();
        final ImageCapture imageCapture = new ImageCapture(imageCaptureConfig);
        this.findViewById(R.id.capture_button).setOnClickListener(new OnClickListener() {
            public final void onClick(View it) {
                File[] files = Camera2.this.getExternalMediaDirs();
                File file = new File(files[0],System.currentTimeMillis() + ".jpg");
                imageCapture.takePicture(file, new ImageCapture.OnImageSavedListener() {

                    public void onImageSaved(@NotNull File file) {
                        String msg = "Photo capture succeeded: " + file.getAbsolutePath();
                        Toast.makeText(Camera2.this.getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                        Log.d("CameraXApp", msg);
                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                        String msg = "Photo capture failed: " + message;
                        Toast.makeText(Camera2.this.getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                        Log.e("CameraXApp", msg);
                        if (cause != null) {
                            cause.printStackTrace();
                        }
                    }
                });
            }
        });
        androidx.camera.core.ImageAnalysisConfig.Builder previewConfigBuilder2 = new androidx.camera.core.ImageAnalysisConfig.Builder();
        HandlerThread handlerThread = new HandlerThread("LuminosityAnalysis");
        handlerThread.start();
        previewConfigBuilder2.setCallbackHandler(new Handler(handlerThread.getLooper()));
        previewConfigBuilder2.setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE);
        ImageAnalysisConfig analyzerConfig = previewConfigBuilder2.build();
        ImageAnalysis previewConfigBuilder3 = new ImageAnalysis(analyzerConfig);
        previewConfigBuilder3.setAnalyzer(new LuminosityAnalyzer());
        CameraX.bindToLifecycle(this, preview, imageCapture, previewConfigBuilder3);
    }

    @SuppressLint("SwitchIntDef")
    private void updateTransform() {
        Matrix matrix = new Matrix();
        TextureView viewFinder = this.viewFinder;

        float centerX = (float)viewFinder.getWidth() / 2.0F;
        viewFinder = this.viewFinder;

        float centerY = (float)viewFinder.getHeight() / 2.0F;
        viewFinder = this.viewFinder;

        Display diaplay = viewFinder.getDisplay();
        short rotation;
        switch(diaplay.getRotation()) {
            case 0:
                rotation = 0;
                break;
            case 1:
                rotation = 90;
                break;
            case 2:
                rotation = 180;
                break;
            case 3:
                rotation = 270;
                break;
            default:
                return;
        }

        int rotationDegrees = rotation;
        matrix.postRotate(-((float)rotationDegrees), centerX, centerY);
        viewFinder = this.viewFinder;

        assert viewFinder != null;
        viewFinder.setTransform(matrix);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 10) {
            if (this.allPermissionsGranted()) {
                TextureView viewFinder = this.viewFinder;

                viewFinder.post(new Runnable() {
                    public final void run() {
                        Camera2.this.startCamera();
                    }
                });
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }

    }

    private boolean allPermissionsGranted() {
        Object[] objects = Camera2.getREQUIRED_PERMISSIONS();
        String[] strings = (String[]) objects;
        int var4 = objects.length;
        int diaplay = 0;

        boolean viewFinder;
        while(true) {
            if (diaplay >= var4) {
                viewFinder = true;
                break;
            }

            String element = strings[diaplay];
            if (ContextCompat.checkSelfPermission(this.getBaseContext(), element) != 0) {
                viewFinder = false;
                break;
            }

            ++diaplay;
        }

        return viewFinder;
    }

    public static TextureView getViewFinder(Camera2 camera2) {
        return camera2.viewFinder;
    }

//     $FF: synthetic method
    public static void setViewFinder(Camera2 camera2, TextureView tv) {
        camera2.viewFinder = tv;
    }

    public View findCachedViewById(int tv) {
        if (this.findViewCache == null) {
            this.findViewCache = new HashMap();
        }

        View previewConfigBuilder = (View)this.findViewCache.get(tv);
        if (previewConfigBuilder == null) {
            previewConfigBuilder = this.findViewById(tv);
            this.findViewCache.put(tv, previewConfigBuilder);
        }

        return previewConfigBuilder;
    }

    public void clearFindViewByIdCache() {
        if (this.findViewCache != null) {
            this.findViewCache.clear();
        }

    }

    private static final class LuminosityAnalyzer implements ImageAnalysis.Analyzer {
        private long lastAnalyzedTimestamp;

        private byte[] toByteArray(@NotNull ByteBuffer toByteArray) {
            toByteArray.rewind();
            byte[] data = new byte[toByteArray.remaining()];
            toByteArray.get(data);
            return data;
        }

        public void analyze(@NotNull ImageProxy image, int rotationDegrees) {
            long currentTimestamp = System.currentTimeMillis();
            if (currentTimestamp - this.lastAnalyzedTimestamp >= TimeUnit.SECONDS.toMillis(1L)) {
                ImageProxy.PlaneProxy viewFinder = image.getPlanes()[0];
                ByteBuffer buffer = viewFinder.getBuffer();
                byte[] data = this.toByteArray(buffer);
                Collection destination = new ArrayList(data.length);

                for (byte item : data) {
                    Integer previewConfigBuilder0 = item & 255;
                    destination.add(previewConfigBuilder0);
                }

                List pixels = (List)destination;
                double sum = 0;
                for (  Object x: pixels) {
                    sum += (int)x;
                }

                double luma = sum/pixels.size();
                Log.d("CameraXApp", "Average luminosity: " + luma);
                this.lastAnalyzedTimestamp = currentTimestamp;
            }

        }
    }
}
