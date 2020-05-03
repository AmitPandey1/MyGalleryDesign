package com.mygallerydesign.sample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mygallerydesign.sample.MyModel;
import com.mygallerydesign.sample.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context mContext;
    private List<MyModel> albumList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;

        MyViewHolder(View view) {
            super(view);
            thumbnail = view.findViewById(R.id.thumnail_image);
        }
    }

    public MyAdapter(Context mContext, List<MyModel> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thumbnail_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyAdapter.MyViewHolder holder, int position) {
        MyModel album = albumList.get(position);
        Glide.with(mContext).load(album.getImgUrl().getDownloadUrl().getResult()).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
