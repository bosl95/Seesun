package com.akhil.cameraxjavademo;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

import java.util.ArrayList;

public class CrossThread extends Thread {
    ArrayList<Long> DateArr;
    boolean cw_mode;
    boolean tf_mode;
    long tf_mode_on;

    public CrossThread(ArrayList<Long> DateArr){
        this.DateArr = DateArr;
        cw_mode = true;
        tf_mode = false;
    }


    public void run(){  // cw_mode가 작동 될 수 있는지 계속 감시
        while(true) {
            while (cw_mode) {
                System.out.println("****************cross mode*****************");
//            mode_tts.speak("croos walk mode", TextToSpeech.QUEUE_FLUSH,null,"id1");
//            System.out.println("DateArr 사이즈 : "+ Integer.toString(DateArr.size()));
                if (this.DateArr.size() == 3) {
                    DateArr.clear();
                    cw_mode = false;    // cw_mode off
                    tf_mode = true;
                    tf_mode_on = System.currentTimeMillis();
                }
            }
            while (tf_mode) { // 신호등 모드 시작
                System.out.println("***************traffic mode*****************");
                if ((System.currentTimeMillis() - tf_mode_on) / 1000.0 >= 15) { // 신호등 모드 15초 이상 경과하면
                    cw_mode = true;
                    tf_mode = false;
                }
            }
        }
    }
}
