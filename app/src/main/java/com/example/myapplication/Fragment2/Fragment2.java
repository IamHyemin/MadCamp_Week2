package com.example.myapplication.Fragment2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import com.example.myapplication.Retrofit.IMyService;
import com.example.myapplication.Retrofit.RetrofitClient;

import org.w3c.dom.Text;
import com.example.myapplication.Retrofit.MyImage;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

import static android.app.Activity.RESULT_OK;

public class Fragment2 extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    public static ArrayList<ImageInfo> mImages;
    public static ArrayList<ImageInfo> mImages_search;
    ImageAdapter galleryRecyclerAdapter;
    private List<Integer> count;
    private int i = 0;
    Context myContext ;
    Bitmap storeImage;

    final IMyService retrofitClient = RetrofitClient.getApiService();

    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<ImageInfo> getImagesFromStorage() {
        final ArrayList<ImageInfo> res = new ArrayList<>();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    ArrayList<ResponseBody> result = retrofitClient.getAllFile().execute().body();
//                    for (ResponseBody elt : result) {
//                        InputStream is = elt.byteStream();
//                        Bitmap bitmap = BitmapFactory.decodeStream(is);
//                        res.add(new ImageInfo(bitmap, "A", "B"));
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
        return res;
    }

    private ArrayList<MyImage> getImagesFromStorage_2() {
        ArrayList<MyImage> res = new ArrayList<>();
        res.add(new MyImage( "Sambuja 부대찌개", "부대찌개 : 7000원"));
        res.add(new MyImage("Hare", "등심돈까쓰 : 8000원 \n안심돈까쓰 : 9000원"));
        return res;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setLogo(R.drawable.honbab_main);
        View v = inflater.inflate(R.layout.fragment2, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        myContext = getContext();
        mImages = getImagesFromStorage();
        mImages_search = getImagesFromStorage();


        ////////////////////////////////////// 검색 //////////////////////////////////////////////
        final EditText editSearch = v.findViewById(R.id.editSearch);

        galleryRecyclerAdapter = new ImageAdapter(myContext, mImages);
        recyclerView.setAdapter(galleryRecyclerAdapter);

        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editSearch.getText().toString();
                search(text);
            }
        });


        //////////////////////////////////////// 식당 추가하기 ///////////////////////////////////////
        TextView txt_create_res = v.findViewById(R.id.add_res);
        txt_create_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View add_layout = LayoutInflater.from(getActivity()).inflate(R.layout.add_rest, null);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                final TextView nameView = add_layout.findViewById(R.id.res_name);
                final TextView menuView = add_layout.findViewById(R.id.res_menu);
                final ImageButton photoView = add_layout.findViewById(R.id.res_photo);
                alertDialog.setView(add_layout);

                photoView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 1);
                    }
                });

                alertDialog.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String res_name = nameView.getText().toString();
                        String res_menu = menuView.getText().toString();

                    }
                });

            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getActivity().getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    storeImage = img;
//                    imageView.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void search(String charText) {
        mImages.clear();
        if (charText.length() == 0) {
            mImages.addAll(mImages_search);
        } else {
            // 리스트의 모든 데이터를 검색한다.
            for (int i = 0; i < mImages_search.size(); i++) {
                if (mImages_search.get(i).getImageMenu().toLowerCase().contains(charText.toLowerCase())) {
                    mImages.add(mImages_search.get(i));
                }
                if (mImages_search.get(i).getImageTitle().toLowerCase().contains(charText.toLowerCase())) {
                    mImages.add(mImages_search.get(i));
                }
            }
        }
        galleryRecyclerAdapter.notifyDataSetChanged();
    }

}

