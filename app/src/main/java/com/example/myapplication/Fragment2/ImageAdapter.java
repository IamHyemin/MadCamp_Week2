package com.example.myapplication.Fragment2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit.File;
import com.example.myapplication.Retrofit.IMyService;
import com.example.myapplication.Retrofit.RetrofitClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Looper.getMainLooper;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.CustomViewHolder> {

    private ArrayList<ImageInfo> mData;
    private Context mContext;

    final IMyService retrofitClient = RetrofitClient.getApiService();

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

        final String image = mData.get(position).getImage();
        final String title = mData.get(position).getImageTitle();
        final String menu = mData.get(position).getImageMenu();

        Uri printUri = Uri.parse("http://192.249.19.242:7980/api/files/download/"+image);

        String[] menu_split = menu.split("\n");
        String printmenu = "";
        for (String elt : menu_split){
            printmenu += elt + "\n";
        }
        Glide.with(mContext).load(printUri).into(holder.image);
        holder.title.setText(title + "");
        holder.content.setText(printmenu);

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        // RecylerView 항목 하나를 갖고 있는 ViewHolder
        ImageView image;
        TextView title;
        TextView content;

        public CustomViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cafe_image);
            title = itemView.findViewById(R.id.cafe_name);
            content = itemView.findViewById(R.id.cafe_detail);

            // 카드 하나를 클릭하면 무슨 일이 일어날지 정하기
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {

                        // 현재 표시 중인 목록 상의 인덱스를 얻는 데 성공했다면
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext); // 아이템 클릭 했을 때 띄울 대화상자 만들기
                        builder.setItems(R.array.card_selected_dialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    // "설명 편집" 눌렀을 때
                                    case 0:
                                        createEditionDialog(pos); // 편집용 대화상자 열기
                                        break;
                                    // "삭제" 눌렀을 때
                                    case 1:
                                        final String targetSaveFileName = mData.get(pos).image;
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    retrofitClient.deleteFile(targetSaveFileName).execute(); // DB 상에서 지우기

                                                    new Handler(getMainLooper()).post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            mData.remove(pos); // 클라이언트 상에서도 지우기
                                                            notifyDataSetChanged();
                                                        }
                                                    });
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();

                                        break;
                                }
                            }
                        }).show();

                    }

                }
            });
        }

        private void createEditionDialog(final int pos) {

            final ImageInfo target = mData.get(pos);

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            LayoutInflater inflater = LayoutInflater.from(mContext);

            // dialog_edit_imagefile 레이아웃을 기반으로 AlertDialog 띄우기
            View dialog_layout = inflater.inflate(R.layout.dialog_edit_imagefile, null);
            final EditText editText_description = dialog_layout.findViewById(R.id.description);
            editText_description.setText(target.getImageMenu());

            builder.setView(dialog_layout)
                    //확인 버튼 추가
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            final String targetSaveFileName = target.image;
                            final String inputDescription = editText_description.getText().toString();
                            final File updateContent = new File(inputDescription); // updateContent 에는 description 만 담겨있음

                            // 서버와 통신하는 부분 (update Description)
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        retrofitClient.updateDescription(targetSaveFileName, updateContent).execute(); // 서버에 보내기
                                        new Handler(getMainLooper()).post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mData.get(pos).setImageMenu(inputDescription); // 클라이언트 상에서도 고치기
                                                notifyDataSetChanged();
                                            }
                                        });

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // do nothing
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();

        }


    }

}
