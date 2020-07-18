//package com.example.myapplication;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//public class MainActivity extends AppCompatActivity {
//
//    MyHandler mHandler;
//    static TextView sResultTextView;
//    Button mNormalButton;
//    Button mErrorButton1;
//    Button mErrorButton2;
//    Button mPostButton;
//    Button mSendMessageButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        sResultTextView = findViewById(R.id.resultTextView);
//        mNormalButton = findViewById(R.id.normalButton);
//        mErrorButton1 = findViewById(R.id.errorButton1);
//        mErrorButton2 = findViewById(R.id.errorButton2);
//        mPostButton = findViewById(R.id.postButton);
//        mSendMessageButton = findViewById(R.id.messageButton);
//        mHandler = new MyHandler();
//
//        mNormalButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // UI 작업 수행 불가능
//                        mHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                // UI 작업 수행 가능
//                                sResultTextView.setText("WorkerThread에서 Handler를 이용한 UI 변경");
//                            }
//                        });
//                        // mHandler.sendEmptyMessage(1000);
//                    }
//                }).start();
//            }
//        });
//
//        mErrorButton2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // UI 작업 수행 불가능
//                        Looper.prepare();
//                        MyHandler handlerInWorkerThread; // Worker Thread와 연결된 Handler
//                        handlerInWorkerThread = new MyHandler();
//                        handlerInWorkerThread.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                // UI 작업 수행 불가능
//                                sResultTextView.setText("Worker Thread와 연결된 Handler");
//                            }
//                        });
//                        Looper.loop();
//                    }
//                }).start();
//            }
//        });
//
//        mErrorButton1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // UI 작업 수행 불가능
//                        sResultTextView.setText("Thread에서 Handler를 이용한 UI 변경");
//                        mHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                // UI 작업 수행 가능
//                            }
//                        });
//                        // mHandler.sendEmptyMessage(1000);
//                    }
//                }).start();
//            }
//        });
//
//        mPostButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        // UI 작업 수행 가능
//                        sResultTextView.setText("Handler post()를 통한 run() 실행");
//                    }
//                });
//            }
//        });
//
//        mSendMessageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mHandler.sendEmptyMessage(1000);
//            }
//        });
//
//    }
//
//    public static class MyHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            // 다른 Thread에서 전달받은 Message 처리
//            if (msg.what == 1000) {
//                sResultTextView.setText("Handler sendEmptyMessage()를 통한 handleMessage() 실행");
//            }
//        }
//    }
//}