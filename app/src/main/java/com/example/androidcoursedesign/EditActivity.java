package com.example.androidcoursedesign;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.net.Uri;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


// TODO :
//  根据路径的不同 重构逻辑
//  从相册过来的 使用的是bitmap
//  从图像确认过来 使用的是path


public class EditActivity extends AppCompatActivity {
    String weather;
    //此处可以添加更多变量记录
    private Bitmap bitmap;
    private ImageView imv;
    private String pathAlbum;
    public static final int PICTURE_CROPPING_CODE = 200;
    private Uri uri=null;
    private String savePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_window);

        //通过相册到的编辑界面
        pathAlbum = getIntent().getStringExtra("path");
        uri=Uri.parse(getIntent().getStringExtra("imageUri"));
        imv = findViewById(R.id.takePicture);
        if(pathAlbum!=null){
            Bitmap bitmap=BitmapFactory.decodeFile(pathAlbum);
            imv.setImageBitmap(bitmap);//将图片放置在控件上
        }else {
            Toast.makeText(this,"得到图片失败",Toast.LENGTH_SHORT).show();
        }


        Intent intent = getIntent();
        ImageView tookPic = findViewById(R.id.takePicture);
        String path = intent.getStringExtra("path");
        // TODO:
        //    页面需要加载出上层活动选择的照片
        //      同preview不知道可不可行
        if(path != null){
            tookPic.setImageURI(Uri.fromFile(new File(path)));
            //照片也进行一个旋转
            //tookPic.setRotation(90);
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

        //TODO:
        // 取消操作的响应
        // 需要上一个活动发消息 告诉是那个活动来到的这( 数字参数形式 )
        // 从而可以回退到正确的上层活动
        // 使用Intent
        Button editCancel=findViewById(R.id.edit_cancel_frame);
        editCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //TODO:
        //  对应进入到裁剪操作的活动中去
        Button cutPic = findViewById(R.id.func_cut);
        cutPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 从相册过来的
                // TODO : BUG EXITS FROM UPPER ACTIVITY(ALBUM)
                //Intent intent = new Intent(EditActivity.this, DoEditActivity.class);
                //intent.setClassName("com.example.androidcoursedegin","com.example.androidcoursedegin.DoEditActivity");
                //intent.putExtra("path", pathAlbum);
                //startActivity(intent);
                pictureCropping(uri);
            }
        });

        // TODO:
        //  编辑操作得到确认
        //  进到方式选择的活动中去
        Button confirmEdit = findViewById(R.id.func_confirm);
        confirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(EditActivity.this,ReportGenActivity.class);
                it.putExtra("Mode",weather);
                it.putExtra("path",savePath);
                //it.setClassName("com.example.androidcoursedegin","com.example.androidcoursedegin.DoComputeActivity");
                startActivity(it);
            }
        });
    }


    // TODO:
    //    模式按钮的菜单响应
    //    加入选项值，不同的选项给后续算法不同的参数
    private void  showModeChioce(View thisView){
        PopupMenu modeMenu=new PopupMenu(this,thisView);
        //modeMenu.getMenuInflater().inflate(R.menu.edit_menu,modeMenu.getMenu());
        //菜单点击
        modeMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(getApplicationContext(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                if(menuItem.getItemId()==R.id.sunny)
                    weather="sunny";
                else if(menuItem.getItemId()==R.id.cloudy)
                    weather="cloudy";
                else
                    weather="default";
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

    /**
     * 图片剪裁
     *
     * @param uri 图片uri
     */
    private void pictureCropping(Uri uri) {
        // 调用系统中自带的图片剪裁
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