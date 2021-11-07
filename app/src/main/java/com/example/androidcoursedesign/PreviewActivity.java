package com.example.androidcoursedesign;

import androidx.annotation.ContentView;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class PreviewActivity extends AppCompatActivity {
    private final String curAct= "PreviewActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_window);
        ImageView tookPic = findViewById(R.id.takePicImg);

        String path = getIntent().getStringExtra("path");
        if(path != null){
            tookPic.setImageURI(Uri.fromFile(new File(path)));
            //照片也进行一个旋转
            tookPic.setRotation(90);
        }

        Button confirm =findViewById(R.id.pic_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO:
                //   无论是否确认 图片都会保存
                //   进入方式选择视图
            }
        });

        Button edit=findViewById(R.id.pic_confirm_edit);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO :
                //    进入编辑视图
                //    wj：加了一下fromwhich信息传送，原来没做
                Intent intent=new Intent(PreviewActivity.this,EditActivity.class);
                intent.putExtra("FromWhich",curAct);
                startActivity(intent);
            }
        });

        Button cancel = findViewById(R.id.cancel_frame);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO:
                //  返回上一层调用界面
                //  利用intent传递识别信息

            }
        });

    }
}