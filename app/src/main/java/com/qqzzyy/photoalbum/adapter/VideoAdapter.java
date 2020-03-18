package com.qqzzyy.photoalbum.adapter;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qqzzyy.photoalbum.R;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>
{
    private List<String> mFileList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View musicListView;
        ImageView cover;
        String name;

        public ViewHolder(View view){
            super(view);
            musicListView = view;
            cover = (ImageView)view.findViewById(R.id.cover);
        }
    }
    public VideoAdapter(List<String> fileList){
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
        holder.name = mFileList.get(position);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(holder.name);
        Bitmap previewBitmap = mmr.getFrameAtTime();
        holder.cover.setImageBitmap(previewBitmap);
    }

    @Override
    public int getItemCount() {
        return mFileList.size();
    }
}
