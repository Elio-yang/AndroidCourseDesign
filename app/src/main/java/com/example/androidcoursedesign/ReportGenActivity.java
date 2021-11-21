package com.example.androidcoursedesign;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class ReportGenActivity extends AppCompatActivity {

    public static final int PICTURE_CROPPING_CODE = 200;
    private Uri uri=null;
    private ImageView pictureIdentified;
    private int pattern;
    private String savePath=null;
    private String path=null;
    private int level;
    private String levelDesc="无污染";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_gen_window);

        path = getIntent().getStringExtra("path");
        pattern=getIntent().getIntExtra("pattern",0);
        //Toast.makeText(ReportGenActivity.this,Integer.toString(pattern),Toast.LENGTH_SHORT).show();
        uri=Uri.parse(getIntent().getStringExtra("imageUri"));
        File file=new File(path);

//        显示图片
        pictureIdentified = findViewById(R.id.pattern_cho_Img);
        Bitmap bitmap= BitmapFactory.decodeFile(path);
        bitmap=AlbumActivity.rotateBimap(this,bitmap);
        pictureIdentified.setImageBitmap(bitmap);//将图片放置在控件上

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
                pictureCropping(uri);
            }
        });
    }
    public int grayCalculate(String target){
        Bitmap picCalculated = BitmapFactory.decodeFile(target);
        int bitmapHeight = picCalculated.getHeight();
        int bitmapWidth = picCalculated.getWidth();
        int level;
        int alllevel=0;
        int row=0;
        int col=0;
        for(;row<bitmapHeight;row++){
            for(col=0;col<bitmapWidth;col++){
                int pixel = picCalculated.getPixel(col, row);// ARGB
                int red = Color.red(pixel); // same as (pixel >> 16) &0xff
                int green = Color.green(pixel); // same as (pixel >> 8) &0xff
                int blue = Color.blue(pixel); // same as (pixel & 0xff)
                int max=red;
                if(green>max)
                    max=green;
                if(blue<max)
                    max=green;
                if(max>225&&max<=255)
                    alllevel+=0;
                else if(max>175&&max<=225)
                    alllevel+=1;
                else if(max>140&&max<=175)
                    alllevel+=2;
                else if(max>100&&max<=140)
                    alllevel+=3;
                else if(max>70&&max<=100)
                    alllevel+=4;
                else if(max>45&&max<=70)
                    alllevel+=5;
                else
                    alllevel+=6;
            }
        }
        level=alllevel/(bitmapHeight*bitmapWidth);
        return  level;
    }

    private void pictureCropping(Uri uri) {
        // 调用系统中自带的图片剪裁
        //Toast.makeText(ReportGenActivity.this, uri.toString(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        // 返回裁剪后的数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PICTURE_CROPPING_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICTURE_CROPPING_CODE && resultCode == RESULT_OK) {
            //图片剪裁返回
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                //在这里获得了剪裁后的Bitmap对象，可以用于上传
                Bitmap image = bundle.getParcelable("data");
                //设置到ImageView上
                //Glide.with(this).load(image).apply(requestOptions).into(imv);
                pictureIdentified.setImageBitmap(image);
                savePath=saveFile(image);
                //Toast.makeText(ReportGenActivity.this,"开始计算", Toast.LENGTH_LONG).show();
                level=grayCalculate(savePath);
                if(pattern==1){
                    level-=2;
                }
                switch (level){
                    case 0:
                        levelDesc="无污染";
                        break;
                    case 1:
                         levelDesc="较轻污染";
                        break;
                    case 2:
                        levelDesc="轻度污染";
                        break;
                    case 3:
                        levelDesc="中度污染";
                        break;
                    case 4:
                        levelDesc="较重污染";
                        break;
                    case 5:
                        levelDesc="重度污染";
                        break;
                    case 6:
                        levelDesc="重度污染";
                        break;
                }
                Intent showInt=new Intent(ReportGenActivity.this,ShowReportActivity.class);
                showInt.putExtra("level",levelDesc);
                showInt.putExtra("path",path);
                showInt.putExtra("imageUri",uri);
                if(pattern==0)
                    showInt.putExtra("pattern","sunny");
                else
                    showInt.putExtra("pattern","cloudy");
                startActivity(showInt);

            }
        }
    }

    private String saveFile(Bitmap bitmap) {
        //获取内部存储状态
        String state = Environment.getExternalStorageState();
        //如果状态不是mounted，无法读写
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            return "";
        }
        try {
            String fileName="";
            fileName = System.currentTimeMillis() + ".jpg";
            File file=File.createTempFile(fileName,"");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Log.e("TEST_FILE",file.getAbsolutePath());
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}