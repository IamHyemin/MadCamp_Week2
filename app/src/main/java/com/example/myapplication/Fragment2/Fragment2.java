package com.example.myapplication.Fragment2;

import android.content.Context;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;
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
    public static ArrayList<ImageInfo> mImages_search;
    ImageAdapter galleryRecyclerAdapter;
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
        res.add(new ImageInfo(R.drawable.sambuja, "Sambuja 부대찌개", "부대찌개 : 7000원"));
        res.add(new ImageInfo(R.drawable.hare, "Hare", "등심돈까쓰 : 8000원 \n안심돈까쓰 : 9000원"));
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

        return v;
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

