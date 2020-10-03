package com.akhil.cameraxjavademo;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private Executor executor = Executors.newSingleThreadExecutor();
//    private int REQUEST_CODE_PERMISSIONS = 1001;
//    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private CrossThread crossTh; // check cross walk mode
    ArrayList<Long> DateArr;

    PreviewView mPreviewView;
    ImageView captureImage;
    private TextToSpeech tts;
    SoundPool sound;
    int SOUND_BEEP;

    private Button tutoBtn;
    private Button exitBtn;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPreviewView = findViewById(R.id.previewView);
//        captureImage = findViewById(R.id.captureImg);
        tutoBtn = findViewById(R.id.btn_tutorial);//튜토리얼 버튼
        exitBtn = findViewById(R.id.btn_exit);//종료 버튼
        tutoBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg = "튜토리얼로 이동";
                Toast.makeText(getApplicationContext(), msg , Toast.LENGTH_SHORT).show();
                //튜토리얼로 이동
                Intent intent = new Intent(getApplicationContext(),TutorialActivity.class);
                startActivity(intent);
            }
        });
        exitBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //어플리케이션 종료
                ActivityCompat.finishAffinity(MainActivity.this);//액티비티 종료
                System.exit(0);//프로세스 종료
            }
        });

//        if (allPermissionsGranted()) {
            startCamera(); //start camera if permission has been granted by user
//        } else {
//            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
//        }

        tts = new TextToSpeech(this, this);

        DateArr = new ArrayList<Long>();
        crossTh = new CrossThread(DateArr);
        crossTh.start();    // 횡단보도 감시 스레드 시작

        sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        SOUND_BEEP = sound.load(this, R.raw.ding, 0);
    }

    private void startCamera() {

        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {

                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();

        ImageCapture.Builder builder = new ImageCapture.Builder();

        //Vendor-Extensions (The CameraX extensions dependency in build.gradle)
        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

        // Query if extension is available (optional).
        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
            // Enable the extension if available.
            hdrImageCaptureExtender.enableExtension(cameraSelector);
        }

        final ImageCapture imageCapture = builder
                .setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
                .build();

        preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageAnalysis, imageCapture);



        /* 캡쳐 버튼 누르면 사진 찍기 */
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREA);
                    File file = new File(getBatchDirectoryName(), mDateFormat.format(new Date()) + ".jpg");

                    ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
                    imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    /* 테스트 용 */
//                            Log.d("test", "file:" + file);
//                            Log.d("test", "option:" + outputFileOptions.toString());
//                            Log.d("test", "output:" + outputFileResults.getSavedUri());

//                                    Toast.makeText(MainActivity.this, "Image Saved successfully", Toast.LENGTH_SHORT).show();

                                    /* 파일 압축시키기 */
                                    try {
                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                        Bitmap originalBm = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

                                        /* 사진 회전 */
//                                        originalBm = GetRotatedBitmap(originalBm, 90);
                                        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));

                                        /* 크기 압축 */
                                    int height=originalBm.getHeight();
                                    int width=originalBm.getWidth();
                                    originalBm = Bitmap.createScaledBitmap(originalBm, (width=width/2), (height=height/2), true);

                                        /* 퀄리티 압축 */
                                        originalBm.compress(Bitmap.CompressFormat.JPEG, 80, os);
                                        os.close();

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    /* 파일 전송 */
                                    send2Server(file);
                                }
                            });
                        }

                        @Override
                        public void onError(@NonNull ImageCaptureException error) {
                            error.printStackTrace();
                        }
                    });
                }
            }
        }).start();

    }

    /* 신호등 모드를 위한 횡단보드 탐지 확인 함수 */
    public void findCrosswalk(Long time){
            int n = DateArr.size();

            /* 횡단보도 객체 시간 배열 DateArr 출력 */
            for (Long arr : DateArr) {
                System.out.println("print" + Double.toString(arr));
            }
            if (n>0 && n<3) {
                System.out.println("시간 차이 : "+Double.toString((time-DateArr.get(n-1))/1000.0));
                if ((time-DateArr.get(n-1))/1000.0>3) {    // 시간이 3초 이상 지나면
                    DateArr.clear(); // 배열 전체 비우기
                }
            }
            DateArr.add(time);

    }

    public synchronized static Bitmap GetRotatedBitmap(Bitmap bitmap, int degrees)
    {
        if ( degrees != 0 && bitmap != null )
        {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2 );
            try
            {
                Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != b2)
                {
                    bitmap.recycle();
                    bitmap = b2;
                }
            }
            catch (OutOfMemoryError ex)
            {
                // We have no memory to rotate. Return the original bitmap.
            }
        }

        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speakOut(CharSequence text){
        tts.setPitch((float) 1.0);
//        tts.setSpeechRate((float)0.1);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH,null,"id1");
    }

    public String getBatchDirectoryName() {
        Log.d("test", isExternalStorageWritable() + "");
        String app_folder_path = "";
        app_folder_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/images11/";
        File dir = new File(app_folder_path);

        File imgFile = null;

        if (!dir.exists()) {
            dir.mkdirs();
//            Log.d("test", "make directory :" + app_folder_path);
        } else {
            /* 디렉토리안에 파일 리스트 얻기 - 테스트용 */
            File files[] = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
//                Log.d("test", files[i].toString());
                //Log.d("test", "hidden: " + files[i].isHidden() + "");
                imgFile = files[i];
            }
        }

        /* 테스트용, 신경 안써두댐 */
//        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//        ImageView myImage = (ImageView) findViewById(R.id.image);
//        myImage.setImageBitmap(myBitmap);

        return app_folder_path;
    }

    /* 서버로 사진 보내기 */
    public void send2Server(File file) {
        final ArrayList<JSONObject> detectedObjs = new ArrayList<>();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MultipartBody.FORM, file))
                .build();

        Request request = new Request.Builder()
                .url("https://yesor.ngrok.io/file_upload")
                .post(requestBody).build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String content = response.body().string();
                    Log.d("GET: ", content);
                    if(content == null){return;}
                    if(content.contains("<!doctype html5>")){return;}
                    JSONObject jsonObject = new JSONObject(content);
                    JSONArray jsonArray = jsonObject.getJSONArray("obj");
//                    JSONArray jsonArray = new JSONArray(response.body().string());
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        Log.d("detected info", String.valueOf(jsonObject2));
                        detectedObjs.add(jsonObject2);
                    }

                    for(int i=0;i<detectedObjs.size();i++){
                        try {
                            String object = detectedObjs.get(i).get("object").toString();
                            float accuracy = Float.parseFloat(detectedObjs.get(i).get("accuracy").toString());

//                            if (!crossTh.tf_mode) {
//                                if (object.equals("cross walk") && accuracy >= 50.0) {
//                                    speakOut("cross walk");
//                                    findCrosswalk(System.currentTimeMillis());
//                                }
//                            } else {
//                                if (!object.equals("cross walk")){
//                                    speakOut(object);
//                                }
//                            }

                            if (crossTh.tf_mode){
                                sound.play(SOUND_BEEP, 1f, 1f,0, 0, 1f);  // 횡단보도 신호음 알림 삽입
                                if (object.equals("cross walk")) continue;
                                speakOut(object);
                            } else { // 신호등 모드가 아니라면 횡단보도 탐지 확인
                                if (!object.equals("cross walk") || accuracy < 50) continue; // 횡단보도 이면서 50퍼이상의 정확도
                                speakOut("cross walk"); // tts 대신 신호기 쓰기 ( cross th 에서)
                                findCrosswalk(System.currentTimeMillis());
                            }

//                            float[] location = new float[4];
//                            String tmp = detectedObjs.get(i).get("location").toString();
//                            for(int j = 0; j < 4 ; j++){
//                                location[j] = Float.parseFloat(tmp.substring(1,tmp.length()-1).split(",")[j]);
//                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    Log.d("test", detectedObjs.toString()); /* 최종 결과 */
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

//    private boolean allPermissionsGranted(){
//
//        for(String permission : REQUIRED_PERMISSIONS){
//            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
//                return false;
//            }
//        }
//        return true;
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        if(requestCode == REQUEST_CODE_PERMISSIONS){
//            if(allPermissionsGranted()){
//                startCamera();
//            } else{
//                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
//                this.finish();
//            }
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS)  {
            int result = tts.setLanguage(Locale.ENGLISH);
//            int result = tts.setLanguage(Locale.KOREA);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

}
