package com.example.myapplication.Fragment2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.R;


// 사진 업로드용 서브액티비티
public class UploadActivity extends AppCompatActivity {

    // Request Code
    private static final int REQUEST_GET_CONTENT = 111;
    private static final int REQUEST_TAKE_PICTURE = 222;
    private static final int REQUEST_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_PERMISSION_READ_EXTERNAL = 2222;


    private EditText nameView;
    private EditText menuView;
    private ImageButton photoView;
    private Button cancel_btn;
    private Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.add_rest);

        nameView = findViewById(R.id.res_name);
        menuView = findViewById(R.id.res_menu);
        photoView = findViewById(R.id.res_photo);
        cancel_btn = findViewById(R.id.upload_cancel_btn);
        submit_btn = findViewById(R.id.upload_submit_btn);

        // 카메라 아이콘 눌렀을 때 할 일
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // AlertDialog 띄우기
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this); // 아이템 클릭 했을 때 띄울 대화상자 만들기
                builder.setItems(R.array.photo_upload_dialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            // "갤러리에서 불러오기" 눌렀을 때
                            case 0:
                                // TODO : 외부 저장소 읽기 권한 요청 해야한다.
                                Intent getContentIntent = new Intent();
                                getContentIntent.setType("image/*");
                                getContentIntent.setAction(Intent.ACTION_GET_CONTENT);

                                // [개발자 문서]
                                // 이 확인 절차가 중요한 이유는 앱이 처리할 수 없는 인텐트를 사용하여 startActivityForResult()를 호출하면 앱이 비정상 종료되기 때문입니다.
                                // 따라서 결과가 null이 아닌 한 안심하고 인텐트를 사용할 수 있습니다.
                                if (getContentIntent.resolveActivity(getPackageManager()) != null){
                                    startActivityForResult(getContentIntent, REQUEST_GET_CONTENT);
                                    // 위 활동의 결과는 onActivityResult() 에서 처리
                                }
                                break;
                            // "카메라" 눌렀을 때
                            case 1:
                                // TODO : 카메라 권한 요청 해야한다.
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
                                    // 위 활동의 결과는 onActivityResult() 에서 처리
                                }
                                break;
                        }
                    }
                }).show();
            }
        });

        // 취소 버튼 눌렀을 때 => 그냥 종료
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadActivity.this.finish();
            }
        });

        // 전송 버튼 눌렀을 때 => Bitmap 과 title 과 description 을 합쳐서 서버에 Multipart POST 요청하기
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : Bitmap 과 title 과 description 을 합쳐서 서버에 Multipart POST 요청하기
                UploadActivity.this.finish();
            }
        });

    }

    // '갤러리에서 가져오기' or '카메라' 의 결과로 가져온 (이미지) 파일로 뭘 할지 정하기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // TODO : 받아온 사진으로 Bitmap 만들기?
        super.onActivityResult(requestCode, resultCode, data);
    }
}