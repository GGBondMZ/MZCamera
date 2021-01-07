package com.mz.mzcamera;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;

public class MainActivity extends Activity {
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageView imageView;
    private Camera camera = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = findViewById(R.id.surface_view);
        imageView = findViewById(R.id.image_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.addCallback(new SurfaceCallback());
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        Button take_picture = findViewById(R.id.take);
        Button back = findViewById(R.id.back);

    }

    private final class SurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            startCamera();
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            try {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setPictureFormat(PixelFormat.JPEG);
                parameters.setPictureSize(1920, 1080);
                camera.setParameters(parameters);
                camera.startPreview();
                startCamera();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
            stopCamera();
        }
    }

    private void startCamera() {
        try {
            if (null == camera) {
                camera = Camera.open();
            }
            if (camera != null) {
                camera.setPreviewDisplay(surfaceHolder);
                //camera.setErrorCallback(); //相机错误回调函数
                camera.setDisplayOrientation(90);
                camera.startPreview();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "相机启动失败", Toast.LENGTH_LONG).show();

            //释放手机摄像头
            if (camera != null) {
                camera.release();
                camera = null;
            }
            finish();
        }
    }

    private void stopCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}