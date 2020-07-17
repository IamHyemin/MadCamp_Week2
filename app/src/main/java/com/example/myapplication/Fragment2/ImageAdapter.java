package com.example.myapplication.Fragment2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.CustomViewHolder> {

    private ArrayList<ImageInfo> mData;
    private Context mContext;

    public ImageAdapter(Context context, ArrayList<ImageInfo> mData) {
        this.mContext = context;
        this.mData = mData;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        if (mContext == null) {
            System.out.println("context is null >>>>>>>>>>>");
        }

        final Integer image = mData.get(position).getImage();
        final String title = mData.get(position).getImageTitle();
        final String menu = mData.get(position).getImageMenu();
        Glide.with(mContext).load(image).into(holder.image);
        holder.title.setText(title + "");
        holder.content.setText(menu + "");

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView content;

        public CustomViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cafe_image);
            title = itemView.findViewById(R.id.cafe_name);
            content = itemView.findViewById(R.id.cafe_detail);
        }
    }

}
