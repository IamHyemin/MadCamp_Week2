package com.example.myapplication.Fragment3;

//import android.support.v4.app.FragmentActivity;
//import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Fragment1.Fragment1;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit.IMyService;
import com.example.myapplication.Retrofit.RetrofitClient;
import com.example.myapplication.Retrofit.User;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Response;

public class Fragment3 extends Fragment implements OnMapReadyCallback {

    GoogleMap mMap;
    final IMyService retrofitClient = RetrofitClient.getApiService();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        final String email = Objects.requireNonNull(intent.getExtras()).getString("email");

        //////////////////////////////////// action bar //////////////////////////////////
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(R.drawable.honbab_main);
        //////////////////////////////////////////////////////////////////////////////////

        final View v = inflater.inflate(R.layout.fragment3, null, false);

        ////////////////////////////////////////// for make map ////////////////////////////////////////////////////////
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        MyLocationMarker();
        //FriendMarker();
    }

    /////////////////////////////// 내 위치 찍기 //////////////////////////////////
    public void MyLocationMarker() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = Objects.requireNonNull(getActivity()).getIntent();
                    final String email = Objects.requireNonNull(intent.getExtras()).getString("email");
                    Response<User> loginUser_res = retrofitClient.getUser(email).execute();
                    final User loginUser = loginUser_res.body();
                    final Double[] position_get = loginUser.getPosition();
                    final String[] friendList = loginUser.getFriendsList();
                    final LatLng myLocation = new LatLng(position_get[0], position_get[1]);
                    final ArrayList<LatLng> friendLocation = new ArrayList<LatLng>();
                    final ArrayList<String> friendName = new ArrayList<String>();
                    final ArrayList<String> friendState = new ArrayList<String>();

                    for (String friend_email: friendList){
                        User friend = retrofitClient.getUser(friend_email).execute().body();
                        Double[] location  = friend.getPosition();
                        friendLocation.add(new LatLng(location[0], location[1]));
                        friendName.add(friend.getName());
                        friendState.add(friend.getState());
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            MarkerOptions makerOptions = new MarkerOptions();
                            makerOptions.position(myLocation).title("내 위치")
                                    .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                                    .alpha(0.5f);

                            for (int i = 0; i < friendList.length; i++){
                                MarkerOptions friendOptions = new MarkerOptions();
                                friendOptions.position(friendLocation.get(i)).title(friendName.get(i))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                        .alpha(0.5f)
                                        .snippet(friendState.get(i));
                                mMap.addMarker(friendOptions);
                            }

                            mMap.addMarker(makerOptions);
//                            mMap.setOnInfoWindowClickListener(infoWindowClickListener);
//                            mMap.setOnMarkerClickListener(markerClickListener);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    Toast.makeText(getActivity(), "업데이트를 원하시면 연락처를 수정해주세요", Toast.LENGTH_LONG);
                                    return false;
                                }
                            });
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        }

    ////////////////////////  친구 위치 찍기 //////////////////////////
    public void FriendMarker() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = Objects.requireNonNull(getActivity()).getIntent();
                    final String email = Objects.requireNonNull(intent.getExtras()).getString("email");
                    Response<User> loginUser_res = retrofitClient.getUser(email).execute();
                    final User loginUser = loginUser_res.body();
                    final String[] friendList = loginUser.getFriendsList();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    for (String friend_email : friendList) {
                                        User friend = null;
                                        try {
                                            friend = retrofitClient.getUser(friend_email).execute().body();
                                            MarkerOptions makerOptions = new MarkerOptions();
                                            makerOptions
                                                    .position(new LatLng(friend.getPosition()[0], friend.getPosition()[1]))
                                                    .title(friend.getName())
                                                    .snippet(friend.getState())
                                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                                    .alpha(0.5f);
                                            mMap.addMarker(makerOptions);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            });
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}