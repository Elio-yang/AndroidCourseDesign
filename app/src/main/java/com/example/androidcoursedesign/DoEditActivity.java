package com.example.androidcoursedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class DoEditActivity extends AppCompatActivity {
    private Bitmap bitmap;
    private ImageView imv;  //还需绑定控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.do_edit_window);


         Intent intent=getIntent();
        if(intent.hasExtra("byteArray")){
            imv = new ImageView(this);
            bitmap = BitmapFactory.decodeByteArray(intent.getByteArrayExtra("byteArry"),0,intent.getByteArrayExtra("byteArray").length);
            imv.setImageBitmap(bitmap);
        }
    }

}