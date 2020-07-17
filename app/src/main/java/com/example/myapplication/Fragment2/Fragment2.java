package com.example.myapplication.Fragment2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Fragment2 extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    public static ArrayList<ImageInfo> mImages;
    private List<Integer> count;
    private int i = 0;
    Context myContext ;

    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<ImageInfo> getImagesFromStorage() {
        ArrayList<ImageInfo> res = new ArrayList<>();
        res.add(new ImageInfo(R.drawable.sambuja, "삼부자 부대찌개", "부대찌개 : 7000원"));
        res.add(new ImageInfo(R.drawable.hare, "하레", "등심돈까쓰 : 8000원 \n안심돈까쓰 : 9000원"));
        return res;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment2, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);
        myContext = getContext();
        mImages = getImagesFromStorage();

        ImageAdapter galleryRecyclerAdapter = new ImageAdapter(myContext, mImages);
        recyclerView.setAdapter(galleryRecyclerAdapter);

        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        return v;
    }


}

