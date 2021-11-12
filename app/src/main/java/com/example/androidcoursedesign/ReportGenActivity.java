package com.example.androidcoursedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class ReportGenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_gen_window);

        String path = getIntent().getStringExtra("path");
        int par=getIntent().getIntExtra("pattern",0);
        String fileName = getIntent().getStringExtra("fileName");
        File file=new File(path);

//        显示图片
        ImageView pictureIdentified = findViewById(R.id.pattern_cho_Img);
        Bitmap bitmap= BitmapFactory.decodeFile(path);
        bitmap=AlbumActivity.rotateBimap(this,bitmap);
        pictureIdentified.setImageBitmap(bitmap);//将图片放置在控件上
        //pictureIdentified.setImageURI(Uri.fromFile(file));
        //pictureIdentified.setRotation(90);

//        返回键
        Button cancel = findViewById(R.id.pattern_choose_cancel_frame);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button regionChoose=findViewById(R.id.region_recognition);
        regionChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                待解决图片裁剪问题
//                UCrop.of(sourceUri, destinationUri).start(activity,PHONE_CODE__CUT);
            }
        });
    }
}