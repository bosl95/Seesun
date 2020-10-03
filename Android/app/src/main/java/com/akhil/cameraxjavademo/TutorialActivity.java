package com.akhil.cameraxjavademo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TutorialActivity extends AppCompatActivity {
    private Button playBtn;
    private Button endBtn;
    private MediaPlayer m;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        playBtn = findViewById(R.id.btn3);//튜토리얼듣기 버튼
        endBtn = findViewById(R.id.btn4);//시작하기 버튼

        //듣기
        playBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
        //시작하기
        endBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (m != null && m.isPlaying()) {
                    m.stop();
                }
                msg = "횡단보도 보행모드로 이동";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                //mainActivity로 이동
                Intent intent = new Intent(TutorialActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void play(){
        m = MediaPlayer.create(TutorialActivity.this,R.raw.mix2);
//        m = MediaPlayer.create(TutorialActivity.this,R.raw.tutorial);
        m.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mp3자원 해제
        m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                mp.stop();
                mp.release();
            }
        });
    }
}