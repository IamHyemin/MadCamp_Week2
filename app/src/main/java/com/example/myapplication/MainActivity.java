package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.myapplication.Retrofit.IMyService;
import com.example.myapplication.Retrofit.RetrofitClient;
import com.example.myapplication.Retrofit.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    EditText edt_login_email, edt_login_password;
    Button btn_login;
    TextView txt_create_account;

    //////////////////////////////// for action bar //////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        com.facebook.login.LoginManager.getInstance().logOut();
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);

        ////////////////////// action bar /////////////////////
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.honbab_main);
        ///////////////////////////////////////////////////////

        //////////////////////// Init Service ///////////////////////
        final IMyService retrofitClient = RetrofitClient.getApiService();

        ////////////////////////////// facebook login ///////////////////////////////////////
        callbackManager = CallbackManager.Factory.create();

        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("result", object.toString());
                                Intent i = new Intent(getApplicationContext(), LoginAfter.class);
                                try {
                                    ////////////////////// facebook login information throw to intent and db ////////////////////////
                                    final String name = object.getString("name");
                                    final String email = object.getString("email");
                                    i.putExtra("name", name);
                                    i.putExtra("email", email);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ////////////////////////////// db에 없으면 데이터 추가 : 이미 존재하는 데이터면 안 들어갈 것이라 가정 ///////////////////////////
                                            Call<User> createFaceBook = retrofitClient.createUser(new User(name, email, "0000"));
                                            try {
                                                createFaceBook.execute();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(i);
                                finish();
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException error) {
                Log.e("LoginErr", error.toString());
            }
        });

        /////////////////////////////////////////////////// log-in //////////////////////////////////////////
        edt_login_email = findViewById(R.id.edt_email);
        edt_login_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edt_login_email.getText().toString())) {
                    return;
                }
                if (TextUtils.isEmpty(edt_login_password.getText().toString())) {
                    return;
                }
                final String email = edt_login_email.getText().toString();
                String password = edt_login_password.getText().toString();
                final String[] realpassword = {""};
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Call<User> tryUser = retrofitClient.getUser(email);
                        try {
                            realpassword[0] = Objects.requireNonNull(tryUser.execute().body()).getPassword();
                            System.out.println(">>>>>>>>>>>>>>>>>>"+realpassword[0]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                ///////////////////////////////로그인 시 1234=1234여도 false 반환 이유 모름////////////////////////

                ///////일단 로그인을 위해/////
                Intent i = new Intent(getApplicationContext(), LoginAfter.class);
                i.putExtra("email", email);
                startActivity(i);
                finish();
//                if (realpassword[0].equals(password)) {
//                    System.out.println("혜민4");
//                    Intent i = new Intent(getApplicationContext(), LoginAfter.class);
//                    i.putExtra("email", email);
//                    startActivity(i);
//                    finish();
//                }
            }
        //////////////////////////////////////////////////////////////
    });

        txt_create_account = findViewById(R.id.txt_create_account);
        txt_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View register_layout = LayoutInflater.from(MainActivity.this).inflate(R.layout.register_layout, null);
                new MaterialStyledDialog.Builder(MainActivity.this)
                        .setTitle("Registeration")
                        .setDescription("Please fill all fields")
                        .setCustomView(register_layout)
                        .setNegativeText("CANCLE")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("REGISTER")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                final EditText edt_register_email = register_layout.findViewById(R.id.edt_email);
                                final EditText edt_register_name = register_layout.findViewById(R.id.edt_name);
                                final EditText edt_register_password = register_layout.findViewById(R.id.edt_password);

                                if (TextUtils.isEmpty(edt_register_email.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (TextUtils.isEmpty(edt_register_name.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "Name cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (TextUtils.isEmpty(edt_register_password.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "Password cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Call<User> createFaceBook = retrofitClient.createUser(new User(edt_register_email.getText().toString(), edt_register_name.getText().toString(), edt_register_password.getText().toString()));
                                        try {
                                            createFaceBook.execute();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        }).show();
            }

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////

}
