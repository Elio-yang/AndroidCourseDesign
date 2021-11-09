package com.example.androidcoursedesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

public class show_report_window extends AppCompatActivity {

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

        TextView time = findViewById(R.id.time);
        TextView location =findViewById(R.id.location);
        TextView mode = findViewById(R.id.mode);
        TextView status = findViewById(R.id.status);
        TextView level = findViewById(R.id.level);
        TextView reach = findViewById(R.id.reach);

        time.append(intent.getStringExtra("time"));
        location.append(intent.getStringExtra("location"));
        mode.append(intent.getStringExtra("mode"));
        status.append(intent.getStringExtra("status"));
        level.append(intent.getStringExtra("level"));
        reach.append(intent.getStringExtra("reach"));

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
        Intent intent= getIntent();
        String path = intent.getStringExtra("path");
        String fileName = intent.getStringExtra("fileName");
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),path,"fileName",null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri=Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}