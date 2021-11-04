package com.example.androidcoursedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AlbumActivity extends AppCompatActivity {
    // go to edit window
    private final String curAct= "AlbumActivity";
    private Intent editAct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_window);
//        Button edit=findViewById(R.id.album_edit);
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                editAct=new Intent(AlbumActivity.this,EditActivity.class);
//                //sent data to editWindow
//                editAct.putExtra("FromWhich",curAct);
//                startActivity(editAct);
//            }
//        });
    }
}