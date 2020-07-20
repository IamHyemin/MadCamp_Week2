package com.example.myapplication.Fragment2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.myapplication.Retrofit.IMyService;
import com.example.myapplication.Retrofit.RetrofitClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        final Bitmap image = mData.get(position).getImage();
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
