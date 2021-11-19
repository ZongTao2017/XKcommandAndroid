package com.xkglow.xkcommand.Helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xkglow.xkcommand.CameraActivity;
import com.xkglow.xkcommand.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private Context context;
    private int type;
    private ItemClickListener itemClickListener;
    private List<PhotoData> photos;
    private int itemNumberInRow;
    private int select;

    public RecyclerViewAdapter(Context context, int type, List<PhotoData> photos, int itemNumberInRow) {
        this.context = context;
        this.type = type;
        this.photos = photos;
        this.itemNumberInRow = itemNumberInRow;
        this.select = -1;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {

        int size = Helper.getScreenWidth(context) / itemNumberInRow;
        holder.rootLayout.setLayoutParams(new ViewGroup.LayoutParams(size, size));

        if (type == 0) {
            PhotoData photoData = photos.get(position);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setImageResource(photoData.resourceId);
            holder.imageViewFull.setVisibility(View.GONE);
            holder.imageViewCamera.setVisibility(View.GONE);
        } else if (type == 1) {
            if (position == 0) {
                holder.imageView.setVisibility(View.GONE);
                holder.imageViewFull.setVisibility(View.GONE);
                holder.imageViewCamera.setVisibility(View.VISIBLE);
            } else {
                PhotoData photoData = photos.get(position - 1);
                holder.imageView.setVisibility(View.GONE);
                holder.imageViewFull.setVisibility(View.VISIBLE);
                Glide.with(context).load(photoData.path).into(holder.imageViewFull);
                holder.imageViewCamera.setVisibility(View.GONE);
            }
        }
        if (type == 0) {
            if (position == select) {
                holder.rootLayout.setForeground(AppCompatResources.getDrawable(context, R.drawable.rect_border));
            } else {
                holder.rootLayout.setForeground(null);
            }
        }
        if (type == 1) {
            if (position - 1 == select && position != 0) {
                holder.rootLayout.setForeground(AppCompatResources.getDrawable(context, R.drawable.rect_border));
            } else {
                holder.rootLayout.setForeground(null);
            }
        }
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 1 && position == 0) {
                    if (itemClickListener != null) {
                        itemClickListener.onCameraClick();
                    }
                } else {
                    if (itemClickListener != null) {
                        if (type == 0) {
                            itemClickListener.onItemClick(position);
                            select = position;
                        }
                        if (type == 1) {
                            itemClickListener.onItemClick(position - 1);
                            select = position - 1;
                        }
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (type == 1) return photos.size() + 1;
        return photos.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        FrameLayout rootLayout;
        ImageView imageView;
        ImageView imageViewFull;
        ImageView imageViewCamera;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            rootLayout = itemView.findViewById(R.id.root_view);
            imageView = itemView.findViewById(R.id.image);
            imageViewFull = itemView.findViewById(R.id.image_full);
            imageViewCamera = itemView.findViewById(R.id.camera_image);
        }
    }

    public void setItemClickLister(ItemClickListener itemClickLister) {
        this.itemClickListener = itemClickLister;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
        void onCameraClick();
    }

    public void addData(List<PhotoData> photos) {
        this.photos.addAll(photos);
        notifyDataSetChanged();
    }
}
