package com.example.androidcoursedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class EditActivity extends AppCompatActivity {
    private Bitmap bitmap;
    private ImageView imv;  //还需绑定控件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_window);

        Intent intent=getIntent();
        if(intent.hasExtra("byteArray")){
            imv = new ImageView(this);
            bitmap = BitmapFactory.decodeByteArray(intent.getByteArrayExtra("byteArry"),0,intent.getByteArrayExtra("byteArray").length);
            imv.setImageBitmap(bitmap);
        }


        //String fromWhich=intent.getStringExtra("FromWhich");
        //Log.d("EDIT_ACT_TAG",fromWhich);



        // TODO:
        //    页面需要加载出上层活动选择的照片



        //模式按钮加载菜单选项
        // TODO:
        //   菜单选择的参数需要保存，并影响后续算法
        //   选项可以增加
        //   是否可以改成多选菜单？
        Button mode=findViewById(R.id.func_mode);
        mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showModeChioce(view);
            }
        });

        //TODO:
        // 取消操作的响应
        // 需要上一个活动发消息 告诉是那个活动来到的这( 数字参数形式 )
        // 从而可以回退到正确的上层活动
        // 使用Intent
        Button editCancel=findViewById(R.id.cancel_frame);
        editCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //TODO:
        //  对应进入到裁剪操作的活动中去
        Button cutPic = findViewById(R.id.func_cut);
        cutPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCut = new Intent(EditActivity.this,DoEditActivity.class);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,bs);
                intentCut.putExtra("byteArray",bs.toByteArray());
                startActivity(intentCut);
            }
        });

        // TODO:
        //  编辑操作得到确认
        //  进到方式选择的活动中去
        Button confirmEdit = findViewById(R.id.func_confirm);
        confirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




    }

    // 模式按钮的菜单响应
    // TODO:
    //    加入选项值，不同的选项给后续算法不同的参数
    private void  showModeChioce(View thisView){
        PopupMenu modeMenu=new PopupMenu(this,thisView);
        //modeMenu.getMenuInflater().inflate(R.menu.edit_menu,modeMenu.getMenu());
        //菜单点击
        modeMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(getApplicationContext(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //菜单关闭
        modeMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu popupMenu) {
            }
        });
        modeMenu.show();
    }
}