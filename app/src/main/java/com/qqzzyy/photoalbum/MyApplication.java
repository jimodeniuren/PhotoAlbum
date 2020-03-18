package com.qqzzyy.photoalbum;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;

public class MyApplication extends Application {
    private Drawable drawable ;
    private MediaStore.Video.Media media ;
    private int FixNum ;
    private float X;
    private float Y;

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public MediaStore.Video.Media getMedia() {
        return media;
    }

    public void setMedia(MediaStore.Video.Media media) {
        this.media = media;
    }

    public int getFixNum() {
        return FixNum;
    }

    public void setFixNum(int fixNum) {
        FixNum = fixNum;
    }

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }
}
