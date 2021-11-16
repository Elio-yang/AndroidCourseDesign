package com.example.androidcoursedesign;


//import androidx.annotation.ContentView;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

public class PreviewActivity extends AppCompatActivity {
    private final String curAct= "PreviewActivity";
    private Uri uri=null;
    private Uri uri3=null;
    private String picpath;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_window);
        ImageView tookPic = findViewById(R.id.pic_cho_Img);

        String path = getIntent().getStringExtra("path");
        String fileName = getIntent().getStringExtra("fileName");
        File file=new File(path);
        tookPic.setImageURI(Uri.fromFile(file));
        //照片也进行一个旋转
        tookPic.setRotation(90);
        //保存到图库
        saveImageToGallery(PreviewActivity.this,file,fileName);

        Button confirm =findViewById(R.id.pic_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //切换
                Intent it0 = new Intent(PreviewActivity.this, ReportGenActivity.class);
                it0.putExtra("path", picpath);
                it0.putExtra("imageUri", uri.toString());
                //0默认晴天 1是阴天
                it0.putExtra("pattern",0);
                startActivity(it0);
            }
        });

        Button edit=findViewById(R.id.pic_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //切换
                Intent it1 = new Intent(PreviewActivity.this, EditActivity.class);
                it1.putExtra("path", picpath);
                it1.putExtra("imageUri", uri.toString());
                startActivity(it1);

                //TODO :
                //    进入编辑视图
                //    wj：加了一下fromwhich信息传送，原来没做
                //Intent intent=new Intent(PreviewActivity.this,EditActivity.class);
                //intent.putExtra("FromWhich",curAct);
                //startActivity(intent);

            }
        });

        Button cancel = findViewById(R.id.pic_cancel_frame);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }
    public String getRealPathFromURI(Uri contentUri) {//通过本地路经 content://得到URI路径
        Cursor cursor = null;
        String locationPath = null ;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor= getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            locationPath = cursor.getString(column_index);
        } catch (Exception e) {
        }finally{
            if(cursor != null)
            {
                cursor.close();
            }
        }
        return locationPath;
    }

    public void saveImageToGallery(Context context, File file, String fileName) {
        // 把文件插入到系统图库
        try {
            String bitpath = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
                    Toast.makeText(context, "保存照片成功", Toast.LENGTH_SHORT).show();
            uri=Uri.parse(bitpath);
            //Toast.makeText(PreviewActivity.this, uri3.toString(), Toast.LENGTH_LONG).show();
            picpath=getRealPathFromURI(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "保存照片失败", Toast.LENGTH_SHORT).show();
        }

        // 通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
    }


}