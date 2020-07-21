package com.example.myapplication.Fragment2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.Retrofit.IMyService;
import com.example.myapplication.Retrofit.RetrofitClient;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;


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

    private String imgPath;

    final IMyService retrofitClient = RetrofitClient.getApiService();

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
                                getContent();
                                break;
                            // "카메라" 눌렀을 때
                            case 1:
                                // TODO : 카메라 권한 요청 해야한다.
                                takePicture();
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

                final String titleString = nameView.getText().toString();
                final String descriptionString = menuView.getText().toString();

                RequestBody title = RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), titleString);
                RequestBody description = RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), descriptionString);

                final File file = new File(imgPath);

                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part img = MultipartBody.Part.createFormData("imageFile", file.getName(), requestFile);

                retrofitClient.uploadFile(img, title, description);

                UploadActivity.this.finish();
            }
        });

    }

    private void getContent() {
        Intent getContentIntent = new Intent();
        getContentIntent.setType("image/*");
        getContentIntent.setAction(Intent.ACTION_GET_CONTENT);

        // [개발자 문서]
        // 이 확인 절차가 중요한 이유는 앱이 처리할 수 없는 인텐트를 사용하여 startActivityForResult()를 호출하면 앱이 비정상 종료되기 때문입니다.
        // 따라서 결과가 null이 아닌 한 안심하고 인텐트를 사용할 수 있습니다.
        if (getContentIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(getContentIntent, REQUEST_GET_CONTENT);
            // 위 활동의 결과는 onActivityResult() 에서 처리
        }
    }

    private void takePicture() {
        // TODO : 사진 찍기 Activity를 시작
        /*
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            }

            startActivityForResult(takePictureIntent, REQUEST_TAKE_PICTURE);
            // 위 활동의 결과는 onActivityResult() 에서 처리
        }

         */
    }


    // '갤러리에서 가져오기' or '카메라' 의 결과로 가져온 (이미지) 파일로 뭘 할지 정하기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // TODO : 받아온 사진으로 (image) File 만들기
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_TAKE_PICTURE:
                if (resultCode == RESULT_OK) {

                    /*
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        photoView.setImageBitmap(imageBitmap);
                   */
                }
                break;

            case REQUEST_GET_CONTENT:
                if (resultCode == RESULT_OK) {
                    // 선택한 사진의 경로(Uri) 객체 얻어오기기
                    Uri uri = data.getData();
                    if (uri != null) {
                        //갤러리앱에서 관리하는 DB정보가 있는데, 그것이 나온다 [실제 파일 경로가 아님!!]
                        //얻어온 Uri는 Gallery앱의 DB번호임. (content://-----/2854)
                        //업로드를 하려면 이미지의 절대경로(실제 경로: file:// -------/aaa.png 이런식)가 필요함
                        //Uri -->절대경로(String)로 변환
                        imgPath = getRealPathFromUri(uri);
                    } else {
                        Toast.makeText(this, "이미지 선택을 하지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    // Uri -- > 절대경로로 바꿔서 리턴시켜주는 메소드
    private String getRealPathFromUri(Uri uri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


}