package com.akhil.cameraxjavademo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

public class DialogActivity extends AppCompatActivity {
    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        // 다이얼로그의 배경을 투명으로 만든다.
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        Button yesButton = findViewById(R.id.btn1);
        Button noButton = findViewById(R.id.btn2);

        // 버튼 리스너 설정
        yesButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //예 버튼 클릭시
                //튜토리얼 액티비티로 이동
                String str1 = "튜토리얼로 이동";
                Toast.makeText(getApplicationContext(), str1 , Toast.LENGTH_SHORT).show();
                //튜토리얼로 이동
                Intent intent = new Intent(getApplicationContext(),TutorialActivity.class);
                startActivity(intent);
            }
        });
        noButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //아니오 버튼 클릭시
                //메인 액티비티로 이동
                String str2 = "횡단보도 보행모드로 이동";
                Toast.makeText(getApplicationContext(), str2 , Toast.LENGTH_SHORT).show();
                //메인으로 이동
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        if (allPermissionsGranted()) {
//            startCamera(); //start camera if permission has been granted by user
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private boolean allPermissionsGranted(){

        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
//                startCamera();
            } else{
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }
}