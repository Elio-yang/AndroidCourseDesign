package com.example.androidcoursedesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.*;


public class MainActivity extends AppCompatActivity {
    private SurfaceView surfaceViewPreview;
    private Camera camera = null;
    //public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private Intent intent_1;

    private final SurfaceHolder.Callback cpHolderCallback = new SurfaceHolder.Callback() {
        //surface创建时触发，开启加载
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            startPreview();
        }
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            stopPreview();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent_1=new Intent(this,AlbumActivity.class);//创建跳转到Albums显示的窗口的Intent
        setContentView(R.layout.main_window);
        //bindViews();
        alumOperation();

    }

    private void openAlbum() {
        startActivity(intent_1);//进入album的窗口界面
    }

    private void alumOperation(){
        Button chooseFromAlbum = findViewById(R.id.album_frame);
        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CHOOSE_PHOTO);
                } else {
                    openAlbum();//打开album的界面
                }
            }
        });
    }

    private void bindViews() {
        surfaceViewPreview = findViewById(R.id.surfaceViewPreview);
        Button takeAShot = findViewById(R.id.camera_shot);
        surfaceViewPreview.getHolder().addCallback(cpHolderCallback);

        takeAShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAShot();
                Toast.makeText(MainActivity.this, "Take Pics", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //开始预览
    private void startPreview() {
        camera = Camera.open();
        try {
            camera.setPreviewDisplay(surfaceViewPreview.getHolder());
            camera.setDisplayOrientation(90);   //让相机旋转90度
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //停止预览
    private void stopPreview() {
        camera.stopPreview();
        camera.release();
        camera = null;
    }


    private void takeAShot( ) {
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                String path = "";
                String fileName="";
                path=saveFile(data,fileName);
                if (!path.equals("")) {
                    Intent it = new Intent(MainActivity.this, PreviewActivity.class);
                    it.putExtra("path", path);
                    it.putExtra("fileName", fileName);
                    startActivity(it);
                } else {
                    Toast.makeText(MainActivity.this, "保存照片失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //    TODO:
    //    保存在哪儿了？
    //    是不是需要更改命名方式？
    //    能不能存到系统相册？
    private String saveFile(byte[] bytes,String fileName) {
        try {
//            File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
//            if (!appDir.exists()) {
//                appDir.mkdir();
//            }

            fileName = System.currentTimeMillis() + ".jpg";
            File file=File.createTempFile(fileName,"");
//          File file = new File(appDir, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
            Log.e("TEST_FILE",file.getAbsolutePath());
            //     /data/data/com.example.androidcoursedesign/cache/img-xxxxx.png
            MainActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
            return file.getAbsolutePath();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}