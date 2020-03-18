package com.qqzzyy.photoalbum.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qqzzyy.photoalbum.R;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>
{
    private List<Drawable> mFileList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View musicListView;
        ImageView cover;
        TextView name;

        public ViewHolder(View view){
            super(view);
            musicListView = view;
            cover = (ImageView)view.findViewById(R.id.cover);
        }
    }
    public PhotoAdapter(List<Drawable> fileList){
        mFileList = fileList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Drawable drawable = mFileList.get(position);
        holder.cover.setImageDrawable(mFileList.get(position));
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }
}
