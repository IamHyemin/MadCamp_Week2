package com.example.myapplication.Fragment1;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Retrofit.IMyService;
import com.example.myapplication.Retrofit.RetrofitClient;
import com.example.myapplication.Retrofit.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class Fragment1 extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        ////////////////////////////////// action bar /////////////////////////////////////////
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(R.drawable.honbab_main);
        ////////////////////////////////////////////////////////////////////////////////////////

        final View view = inflater.inflate(R.layout.fragment1, container, false);
        Intent intent = getActivity().getIntent();

        final String email = Objects.requireNonNull(intent.getExtras()).getString("email");
        final IMyService retrofitClient = RetrofitClient.getApiService();
        final LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


        ////////////////////////////////////// 사용자 정보 받아오기 //////////////////////////////////
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<User> loginUser_res = retrofitClient.getUser(email).execute();
                    final User loginUser = loginUser_res.body();
                    final String[] friendList = loginUser.getFriendsList();
                    final String name = loginUser.getName();
                    final String phoneNum = loginUser.getPhoneNum();
                    final String email = loginUser.getEmail();
                    final String password = loginUser.getPassword();
                    final String state = loginUser.getState();
                    final String[] likeList = loginUser.getLikeList();
                    final Double[] position_get = loginUser.getPosition();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            final ArrayList userInfo = new ArrayList();
                            userInfo.add(loginUser);
                            final PhoneAdapter user_adapter = new PhoneAdapter(userInfo, getContext());
                            ListView listView = view.findViewById(R.id.listView_user);
                            listView.setAdapter(user_adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView parent, View v, final int position, long id) {
                                    View User = inflater.inflate(R.layout.user_detail, container, false);
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                    ;
                                    TextView nameView = User.findViewById(R.id.user_name);
                                    TextView emailView = User.findViewById(R.id.user_email);
                                    final EditText passwordView = User.findViewById(R.id.user_password);
                                    final EditText phoneView = User.findViewById(R.id.user_phone);
                                    final EditText stateView = User.findViewById(R.id.user_state);
                                    final EditText likeView = User.findViewById(R.id.user_likeList);

                                    alertDialog.setView(User);
                                    nameView.setText(name);
                                    emailView.setText(email);
                                    passwordView.setText(password);
                                    phoneView.setText(phoneNum);
                                    stateView.setText(state);
                                    String likeprint1 = "";
                                    for (String elt : likeList) {
                                        likeprint1 = elt + " ";
                                    }
                                    likeView.setText(likeprint1);

                                    alertDialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    final String password = passwordView.getText().toString();
                                                    final String phoneNum = phoneView.getText().toString();
                                                    final String state = stateView.getText().toString();
                                                    final String likeList_bfr = likeView.getText().toString();

                                                    String[] likeList = likeList_bfr.split(", ");

                                                    if (Build.VERSION.SDK_INT >= 23 &&
                                                            ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                                                0);
                                                    } else {
                                                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                                        String provider = location.getProvider();
                                                        double longitude = location.getLongitude();
                                                        double latitude = location.getLatitude();
                                                        position_get[0] = latitude;
                                                        position_get[1] = longitude;
                                                    }
                                                    final User updateUser = new User(name, email, password, position_get, phoneNum, state, likeList, friendList);
                                                    try {
                                                        retrofitClient.updateUser(email, updateUser).execute();
                                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                userInfo.remove(0);
                                                                userInfo.add(updateUser);
                                                                user_adapter.notifyDataSetChanged();
                                                            }
                                                        });
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }).start();
                                        }
                                    });
                                    alertDialog.show();
                                }
                            });
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        //////////////////////////////////////////FAB로 update -> 수정 예정////////////////////////////////////////////////////////

        FloatingActionButton phone_fab = view.findViewById(R.id.phone_fab);

        class PhoneFABClickListener implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                View addphoneView = inflater.inflate(R.layout.update_user, container, false);
                alertDialog.setView(addphoneView);
                final EditText email_get = addphoneView.findViewById(R.id.update_friend);
                alertDialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String email_friend = email_get.getText().toString();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Response<User> newfriend = retrofitClient.updateUserFriend(email, email_friend).execute();
                                    if (newfriend.code() == 400){
                                        Toast.makeText(getContext(), "You cannot be your friend", Toast.LENGTH_SHORT).show();
                                    }else if (newfriend.code() == 404){
                                        Toast.makeText(getContext(), "There is no user who has the email", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });
                alertDialog.show();
            }
        }
        phone_fab.setOnClickListener(new PhoneFABClickListener());
        ///////////////////////////////////////////////////////////////////////////////////////////////////////

        //////////////////////////////////////// listview 누르면 친구 정보 더 보기 //////////////////////////////////////
        //for test array
        final ArrayList<User> userList = new ArrayList();
//        userList.add(new User("Banana", "banana@naver.com", "1234"));
//        userList.add(new User("Apple", "apple@naver.com", "12345"));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<User> loginUser_res = retrofitClient.getUser(email).execute();
                    final User loginUser = loginUser_res.body();
                    final String[] friendList = loginUser.getFriendsList();
                    System.out.println("the number of friend is "+friendList.length);
                    for (String email_friend : friendList) {
                        User friend = retrofitClient.getUser(email_friend).execute().body();
                        System.out.println("friend is "+ friend.getName());
                        userList.add(friend);
                    }

                    final PhoneAdapter adapter = new PhoneAdapter(userList, getContext());

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {

                            ListView listView = view.findViewById(R.id.listView);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView parent, View v, int position, long id) {
                                    // get item
                                    User user = (User) parent.getItemAtPosition(position);

                                    final String num = user.getPhoneNum();
                                    String userName = user.getName();
                                    String state = user.getState();
                                    String[] food = user.getLikeList();

                                    View friend = inflater.inflate(R.layout.friends_detail, container, false);
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                    ;
                                    TextView nameView = friend.findViewById(R.id.friend_name);
                                    TextView phoneView = friend.findViewById(R.id.friend_phone);
                                    TextView stateView = friend.findViewById(R.id.friend_state);
                                    TextView likeView = friend.findViewById(R.id.friend_likeList);

                                    alertDialog.setView(friend);
                                    nameView.setText(userName);
                                    phoneView.setText(num);
                                    stateView.setText(state);

                                    String printfood = "";
                                    for (String elt : food) {
                                        printfood += elt + ", ";
                                    }
                                    likeView.setText(printfood);

                                    ImageButton btn = friend.findViewById(R.id.button);
                                    btn.setOnClickListener(new View.OnClickListener() {
                                        String callnum = "tel:" + num;

                                        @Override
                                        public void onClick(View view) {
                                            Intent myIntent = new Intent(Intent.ACTION_VIEW);
                                            myIntent.setData(Uri.parse(callnum));
                                            try {
                                                startActivity(myIntent);
                                            } catch (ActivityNotFoundException e) {
                                                Intent callIntent = new Intent(Intent.ACTION_VIEW);
                                                callIntent.setData(Uri.parse(callnum));
                                                if (callIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                                    startActivity(callIntent);
                                                }
                                            }
                                        }
                                    });

                                    ImageButton btn2 = friend.findViewById(R.id.button2);
                                    btn2.setOnClickListener(new View.OnClickListener() {
                                        String smsnum = "sms:" + num;

                                        @Override
                                        public void onClick(View view) {
                                            Intent myIntent = new Intent(Intent.ACTION_VIEW);
                                            myIntent.setData(Uri.parse(smsnum));
                                            try {
                                                startActivity(myIntent);
                                            } catch (ActivityNotFoundException e) {
                                                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                                                smsIntent.setData(Uri.parse(smsnum));
                                                if (smsIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                                                    startActivity(smsIntent);
                                                }
                                            }
                                        }
                                    });
                                    alertDialog.show();
                                }
                            });
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        return view;
    }



}
