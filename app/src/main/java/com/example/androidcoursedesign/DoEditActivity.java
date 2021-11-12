package com.example.androidcoursedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class DoEditActivity extends AppCompatActivity {
    private Bitmap bitmap;
    private ImageView imv;  //还需绑定控件
    private String pathAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.do_edit_window);


        pathAlbum = getIntent().getStringExtra("path");
        //uri=getMediaUriFromPath(pathAlbum);
        imv = findViewById(R.id.Picture);
        if(pathAlbum!=null){
            bitmap=BitmapFactory.decodeFile(pathAlbum);
            bitmap=AlbumActivity.rotateBimap(this,bitmap);
            imv.setImageBitmap(bitmap);//将图片放置在控件上
        }else {
            Toast.makeText(this,"得到图片失败",Toast.LENGTH_SHORT).show();
        }

        Button returnEdit = findViewById(R.id.do_cancel_frame);
        returnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}