package com.qqzzyy.photoalbum.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.qqzzyy.photoalbum.MyApplication;
import com.qqzzyy.photoalbum.R;
import com.qqzzyy.photoalbum.ShowView;

public class ShowActivity extends AppCompatActivity {

    ShowView showView;
    private DisplayMetrics dm;
    float X ;
    float Y ;
    float height;
    float width;
    float fromXDelta;
    float fromYDelta;
    TranslateAnimation outAnimation;
    private MyApplication myApplication ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        try {
            setTitle(getIntent().getStringExtra("comment"));
        }catch (Exception e){}

        myApplication =(MyApplication)getApplication();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        dm = getResources().getDisplayMetrics();

        X = myApplication.getX();
        Y = myApplication.getY();

        showView = (ShowView)findViewById(R.id.show_dialog);

        BitmapDrawable bitmapDrawable= (BitmapDrawable) myApplication.getDrawable();

        showView.setImageBitmap(bitmapDrawable.getBitmap());

        showView.setupView();

        height = bitmapDrawable.getBitmap().getHeight();
        width = bitmapDrawable.getBitmap().getWidth();

        fromXDelta = X - ((float)(dm.widthPixels) - width)/2 ;
        fromYDelta = Y - ((float)(dm.heightPixels)-height)/2 - myApplication.getFixNum();

        TranslateAnimation translateAnimation = new TranslateAnimation(fromXDelta,0, fromYDelta,0);
        translateAnimation.setDuration(400);
        showView.startAnimation(translateAnimation);
        outAnimation = new TranslateAnimation(0,fromXDelta, 0,fromYDelta);
        outAnimation.setDuration(400);
        outAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showView.startAnimation(outAnimation);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
