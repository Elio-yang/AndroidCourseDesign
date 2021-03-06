package com.example.androidcoursedesign;
import com.example.androidcoursedesign.EditActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;





//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import java.io.FileOutputStream;
import java.io.IOException;



// TODO :
//  根据路径的不同 重构逻辑
//  从相册过来的 使用的是bitmap
//  从图像确认过来 使用的是path


public class EditActivity extends AppCompatActivity {
    String weather="default";
    //此处可以添加更多变量记录
    Collection<String> choice= new ArrayList<>();
    private Bitmap bitmap;
    private ImageView imv;
    private int weatherNumber;
    private String pathAlbum;
    public static final int PICTURE_CROPPING_CODE = 200;
    private Uri uri=null;
    private String savePath;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_window);
        weatherNumber=0;

        //通过相册到的编辑界面
        pathAlbum = getIntent().getStringExtra("path");
        savePath=pathAlbum;
        uri=Uri.parse(getIntent().getStringExtra("imageUri"));
        imv = findViewById(R.id.takePicture);
        if(pathAlbum!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(pathAlbum);
            bitmap=AlbumActivity.rotateBimap(this,bitmap);
            imv.setImageBitmap(bitmap);//将图片放置在控件上
        }else {
            Toast.makeText(this,"得到图片失败",Toast.LENGTH_SHORT).show();
        }


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


        // 取消操作的响应
        // 从而可以回退到正确的上层活动

        Button editCancel=findViewById(R.id.edit_cancel_frame);
        editCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //  对应进入到裁剪操作的活动中去
        Button cutPic = findViewById(R.id.func_cut);
        cutPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pictureCropping(uri);
            }
        });


        //  编辑操作得到确认
        //  进到方式选择的活动中去
        Button confirmEdit = findViewById(R.id.func_confirm);
        confirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(EditActivity.this, ReportGenActivity.class);
                it.putExtra("path", savePath);
                //Toast.makeText(EditActivity.this,Integer.toString(weatherNumber),Toast.LENGTH_SHORT).show();
                it.putExtra("pattern",weatherNumber);
                it.putExtra("imageUri", uri.toString());
                startActivity(it);
            }
        });
    }


    //    模式按钮的菜单响应
    //    加入选项值，不同的选项给后续算法不同的参数
    private void  showModeChioce(View thisView){
        PopupMenu modeMenu=new PopupMenu(this,thisView);
        modeMenu.getMenuInflater().inflate(R.menu.edit_menu,modeMenu.getMenu());

        modeMenu.show();
        //菜单点击
        modeMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                weather=(String) menuItem.getTitle();
                if(weather.equals("晴天")){
                    weatherNumber=0;
                }else if(weather.equals("阴天")){
                    weatherNumber=1;
                }
                return false;
            }
        });

        //菜单关闭
        modeMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu popupMenu) {
            }
        });

    }
    //popupwindow实现多选
    private void showPopupWindow(View thisview){
        View view= LayoutInflater.from(EditActivity.this).inflate(R.layout.edit,null);
        PopupWindow popupWindow= new PopupWindow(view, ActionBar.LayoutParams.WRAP_CONTENT,RadioGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(thisview,0,-500);

        CheckBox checkBox1 =view.findViewById(R.id.checkbox);
        CheckBox checkBox2 =view.findViewById(R.id.checkbox1);
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    choice.add(compoundButton.getTransitionName());
                else
                    choice.remove(compoundButton.getTransitionName());
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    choice.add(compoundButton.getTransitionName());
                else
                    choice.remove(compoundButton.getTransitionName());
            }
        });
    }

    /**
     * 图片剪裁
     *
     * @param uri 图片uri
     */
    private void pictureCropping(Uri uri) {
        // 调用系统中自带的图片剪裁
        //Toast.makeText(EditActivity.this, uri.toString(), Toast.LENGTH_LONG).show();
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

    /**
     * 返回Activity结果
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        数据
     */
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
                imv.setImageBitmap(image);
                savePath=saveFile(image);
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