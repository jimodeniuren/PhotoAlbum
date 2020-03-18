package com.qqzzyy.photoalbum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class ShowView extends ImageView {
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    private Bitmap bitmap = null;

    private DisplayMetrics dm;

    float minScaleR = 1.0f;
    static final float MAX_SCALE = 15f;

    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;

    int mode = NONE;

    PointF prev = new PointF();
    PointF mid = new PointF();

    float dist = 1f;

    public ShowView(Context context){
        super(context);
    }

    public ShowView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void setImageBitmap(Bitmap bitmap)
    {
        super.setImageBitmap(bitmap);
        this.bitmap = bitmap;
    }


    @SuppressLint("ClickableViewAccessibility")
    public void setupView(){
        final Context context = getContext();
        dm = context.getResources().getDisplayMetrics();

        BitmapDrawable bd = (BitmapDrawable)this.getDrawable();
        if(bd != null){
            bitmap = bd.getBitmap();
        }
        this.setScaleType(ScaleType.MATRIX);

        this.setImageBitmap(bitmap);
        if(bitmap != null){
            center(true, true);
        }

        this.setImageMatrix(matrix);

        this.setOnTouchListener(new OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix);
                        prev.set(event.getX(), event.getY());
                        mode = DRAG;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        dist = spacing(event);
                        if (spacing(event) > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                        }
                        break;
                    case MotionEvent.ACTION_UP:{
                        break;
                    }
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        //savedMatrix.set(matrix);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            matrix.set(savedMatrix);
                            matrix.postTranslate(event.getX() - prev.x, event.getY()
                                    - prev.y);
                        } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            if (newDist > 10f) {
                                matrix.set(savedMatrix);
                                float tScale = newDist / dist;
                                matrix.postScale(tScale, tScale, mid.x, mid.y);
                            }
                        }
                        break;
                }
                ShowView.this.setImageMatrix(matrix);
                CheckView();
                return true;
            }
        });
    }

    public void center(boolean horizontal, boolean vertical) {
        Matrix m = new Matrix();
        m.set(matrix);
        RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        m.mapRect(rect);

        float height = rect.height();
        float width = rect.width();

        float deltaX = 0, deltaY = 0;


        int screenHeight = dm.heightPixels;

        if (vertical) {
            if (height < screenHeight) {
                deltaY = (screenHeight - height) / 2 - rect.top;
            } else if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < screenHeight) {
                deltaY = this.getHeight() - rect.bottom;
            }
        }


        int screenWidth = dm.widthPixels;

        if (horizontal) {
            if (width < screenWidth) {
                deltaX = (screenWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < screenWidth) {
                deltaX = screenWidth - rect.right;
            }
        }
        matrix.postTranslate(deltaX, deltaY);

    }

    private void CheckView() {
        float[] p = new float[9];
        matrix.getValues(p);
        if (mode == ZOOM) {
//            if (p[0] < minScaleR) {
//                matrix.setScale(minScaleR, minScaleR);
//            }
            if (p[0] > MAX_SCALE) {
                matrix.set(savedMatrix);
            }
        }
        center(true, true);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}

