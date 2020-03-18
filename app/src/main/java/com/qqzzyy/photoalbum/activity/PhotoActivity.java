package com.qqzzyy.photoalbum.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qqzzyy.photoalbum.MyApplication;
import com.qqzzyy.photoalbum.adapter.PhotoAdapter;
import com.qqzzyy.photoalbum.R;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemLongClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoActivity extends AppCompatActivity {

    private List<Drawable> photoList = new ArrayList<>();
    private PhotoAdapter photoAdapter;
    private MyApplication myApplication;
    private SwipeRecyclerView swipeRecyclerView;
    private List<String> nameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        myApplication = (MyApplication)getApplication();

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/media/com.qqzzyy.photoalbum");

        addPicture(file);
        swipeRecyclerView = (SwipeRecyclerView)findViewById(R.id.photo_recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        swipeRecyclerView.setLayoutManager(layoutManager);
        photoAdapter = new PhotoAdapter(photoList);
        swipeRecyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int adapterPosition) {
                myApplication.setDrawable(photoList.get(adapterPosition));
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                myApplication.setX(location[0]);
                myApplication.setY(location[1]);
                Intent intent = new Intent(PhotoActivity.this,ShowActivity.class);
                SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
                try {
                    intent.putExtra("comment",preferences.getString(nameList.get(adapterPosition),""));
                }catch (Exception e){}
                startActivity(intent);
            }
        });
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        swipeRecyclerView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int adapterPosition) {
                builder.setTitle("添加评价");

                final View v =  getLayoutInflater().inflate(R.layout.dialog,null);
                builder.setView(v);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        EditText comment = v.findViewById(R.id.comment);
                        SharedPreferences.Editor editor =getSharedPreferences("data",MODE_PRIVATE).edit();
                        editor.putString(nameList.get(adapterPosition),comment.getText().toString());
                        editor.apply();
                    }
                });
                builder.show();
            }
        });
        swipeRecyclerView.setAdapter(photoAdapter);

        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhotoActivity.this, Camera2.class));
            }
        });
    }

    public void addPicture(File f) {

        File[] file = f.listFiles();

        if (file != null) {

            for (File file2 : file) {

                    String s = file2.getPath();

                    if (s.endsWith("jpg")) {

                        Bitmap bm = BitmapFactory.decodeFile(file2.getAbsolutePath());

                        Drawable picture = new BitmapDrawable(bm);

                        photoList.add(picture);
                        nameList.add(file2.getName());

                    }
            }
        }
    }
}
