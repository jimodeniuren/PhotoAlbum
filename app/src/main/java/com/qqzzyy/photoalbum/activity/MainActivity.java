package com.qqzzyy.photoalbum.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.qqzzyy.photoalbum.MyApplication;
import com.qqzzyy.photoalbum.R;

public class MainActivity extends AppCompatActivity {
    private DisplayMetrics displayMetrics;
    private MyApplication myApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayMetrics = getResources().getDisplayMetrics();

        myApplication = (MyApplication)getApplication();

        Button photo = (Button)findViewById(R.id.photo);
        Button video = (Button)findViewById(R.id.video);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, PhotoActivity.class));
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, VideoActivity.class));
            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
            Rect outRect = new Rect();
            getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(outRect);
            myApplication.setFixNum(displayMetrics.heightPixels - outRect.height());
    }

}
