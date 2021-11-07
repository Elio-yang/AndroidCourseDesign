package com.example.androidcoursedesign;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

public class PreviewActivity extends AppCompatActivity {
    private final String curAct= "PreviewActivity";
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


        Button confirm =findViewById(R.id.pic_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保存到图库
                saveImageToGallery(PreviewActivity.this,file,fileName);
                //切换
                Intent it0 = new Intent(PreviewActivity.this, ReportGenActivity.class);
                it0.putExtra("path", path);
                it0.putExtra("fileName", fileName);
                //0默认晴天 1是阴天
                it0.putExtra("pattern",0);
                startActivity(it0);
            }
        });

        Button edit=findViewById(R.id.pic_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it1 = new Intent(PreviewActivity.this, EditActivity.class);
                it1.putExtra("path", path);
                it1.putExtra("fileName", fileName);
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
    public void saveImageToGallery(Context context, File file, String fileName) {
        // 把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
                    Toast.makeText(context, "保存照片成功", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "保存照片失败", Toast.LENGTH_SHORT).show();
        }

        // 通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
    }
}