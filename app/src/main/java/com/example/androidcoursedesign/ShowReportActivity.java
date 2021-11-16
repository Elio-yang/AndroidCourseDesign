package com.example.androidcoursedesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShowReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_report_window);
        Intent intent= getIntent();
        ImageView smokePic = findViewById(R.id.smokePic);
        String path = intent.getStringExtra("path");
        if(path != null){
            smokePic.setImageURI(Uri.fromFile(new File(path)));
            //照片也进行一个旋转
//            smokePic.setRotation(90);
        }

//        TextView time = findViewById(R.id.time);
//        TextView location =findViewById(R.id.location);
//        TextView mode = findViewById(R.id.mode);
//        TextView status = findViewById(R.id.status);
//        TextView level = findViewById(R.id.level);
//        TextView reach = findViewById(R.id.reach);
//
//        time.append(intent.getStringExtra("time"));
//        location.append(intent.getStringExtra("location"));
//        mode.append(intent.getStringExtra("mode"));
//        status.append(intent.getStringExtra("status"));
//        level.append(intent.getStringExtra("level"));
//        reach.append(intent.getStringExtra("reach"));

        Button cancel = findViewById(R.id.to_main_frame);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setClassName("com.example.androidcoursedesign","com.example.androidcoursedesign.MainActivity");
                startActivity(intent);
            }
        });

        ImageButton share =findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showReporChoice(view);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
                shareIntent.setType("image/jpeg");
                startActivity(shareIntent);
            }
        });
        ImageButton download =findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });
    }

//    private void showReporChoice(View view){
//        PopupMenu popupMenu =new PopupMenu(this,view);
//        popupMenu.getMenuInflater().inflate(R.menu.share_menu,popupMenu.getMenu());
//        popupMenu.show();
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                Toast.makeText(getApplicationContext(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
//    }
    private void save(View thisview){
            View view = getWindow().getDecorView();
            //允许当前窗口保存缓存信息
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();

            //获取状态栏高度
            Rect rect = new Rect();
            view.getWindowVisibleDisplayFrame(rect);
            int statusBarHeight = rect.top;

            WindowManager windowManager = getWindowManager();

            //获取屏幕宽和高
            DisplayMetrics outMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(outMetrics);
            int width = outMetrics.widthPixels;
            int height = outMetrics.heightPixels;

            //去掉状态栏
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeight, width,
                    height-statusBarHeight);

            //销毁缓存信息
            view.destroyDrawingCache();
            view.setDrawingCacheEnabled(false);
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            Toast.makeText(this, "请至权限中心打开应用相册权限", Toast.LENGTH_SHORT).show();
        } else {

            // 新建目录appDir，并把图片存到其下
            File appDir = new File(getExternalFilesDir(null).getPath()+ "BarcodeBitmap");
            if (!appDir.exists()) {

                appDir.mkdir();
            }
            String fileName = System.currentTimeMillis() + ".jpg";
            File file = new File(appDir, fileName);
            try {

                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

            // 把file里面的图片插入到系统相册中
            try {

                MediaStore.Images.Media.insertImage(getContentResolver(),
                        file.getAbsolutePath(), fileName, null);
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            }

            Toast.makeText(this, "保存"+fileName, Toast.LENGTH_SHORT).show();

            // 通知相册更新
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        }
    }

}