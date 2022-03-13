package ir.bahonar.nama;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    ProcessCameraProvider cameraProvider;
    CardView cv;
    ImageView im;
    Bitmap bm;
    Preview preview;
    CameraSelector cameraSelector;
    ImageCapture imageCapture;
    boolean configured = false;
    String strTV = "";
    int start = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        im = findViewById(R.id.imageView);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        cv = findViewById(R.id.cardView);
        TextView tv = findViewById(R.id.textView);

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            Log.d("checkCameraPermissions", "No Camera Permissions");
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.CAMERA },
                    100);
        }

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "error = " + e.toString(), Toast.LENGTH_LONG).show();
            }
        }, ContextCompat.getMainExecutor(this));

        cv.setOnClickListener(view->{
            onClick();
        });
        im.setOnClickListener(v->{
            im.setImageBitmap(bm);
            tv.setText(strTV);
        });

        findViewById(R.id.button2).setOnClickListener(v->{
            startActivity(new Intent(this,setting.class));
            finish();
        });
        findViewById(R.id.button4).setOnClickListener(v->{
            startActivity(new Intent(this,Bluetooth.class));
            finish();
        });

    }
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        PreviewView previewView = findViewById(R.id.previewView);
        preview = new Preview.Builder().build();

        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview);
        //analyst(cameraSelector,preview);

    }

    void pals(View v){
        v.setVisibility(View.GONE);
        int x = (int) (Math.random()*900)+200;
        int y = (int) (Math.random()*1800)+200;
        v.setX(x);
        v.setY(y);
        v.setVisibility(View.VISIBLE);
        new CountDownTimer(200, 200) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                pals(v);
            }

        }.start();

    }

    @SuppressLint("SetTextI18n")
    void color(CardView cv, TextView tv){
        int x = (int) ((Math.random()-0.5d)*Integer.MAX_VALUE);
        String color = Integer.toHexString(x);
        try {
            color = "#"+ color.charAt(2) + color.charAt(3) +color.charAt(4)+color.charAt(5)+color.charAt(6)+color.charAt(7);
            cv.setBackgroundColor(Color.parseColor(color));
            tv.setText(color);
        }catch (Exception e){}
        new CountDownTimer(500, 500) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                color(cv,tv);
            }

        }.start();
    }

    public void onClick() {
        capt();

    }
    void capt(){
        start = (int)Calendar.getInstance().getTimeInMillis();

        Activity activity = this;
        CountDownTimer cdt = new CountDownTimer(setting.time, setting.time) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                ImageAnalysis imageAnalysis =
                        new ImageAnalysis.Builder()
                                .setTargetResolution(new Size(1280, 720))
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build();
                if(!configured)
                {
                    imageCapture =
                            new ImageCapture.Builder()
                                    .setTargetRotation(im.getDisplay().getRotation())
                                    .build();
                    cameraProvider.bindToLifecycle((LifecycleOwner) activity, cameraSelector, imageCapture, imageAnalysis, preview);
                    configured =true;
                }
                ByteArrayOutputStream fOut = new ByteArrayOutputStream();

                ImageCapture.OutputFileOptions outputFileOptions =
                        new ImageCapture.OutputFileOptions.Builder(fOut).build();
                ExecutorService cameraExecutor = Executors.newSingleThreadExecutor();
                ImageCapture.OnImageSavedCallback oicc = new ImageCapture.OnImageSavedCallback() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        // insert your code here.
                        //Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                        Log.e("onImageSaved","onImageSaved");
                        byte[] bitmapdata = fOut.toByteArray();
                        Log.e("bitmapdata",""+bitmapdata.length);
                        Log.e("fOut",""+fOut.size());
                        bm = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                        Log.e("fOut","afterr"+fOut.size());
                        //try {
                        Bitmap bit = getResizedBitmap(bm,setting.width,setting.height);
                        Log.e("fOut","commp"+fOut.size());
                        try{
                            if(setting.signDet)
                                signDetection(activity,bit);
                            else
                                wayDetection(activity,bit);
                        }catch (Exception e){
                            activity.runOnUiThread(() -> {
                                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                            });
                        }
                    }
                    @Override
                    public void onError(@NonNull ImageCaptureException error) {
                        // insert your code here.
                    }
                };
                imageCapture.takePicture(outputFileOptions, cameraExecutor,oicc);
                capt();
            }

        };
        cdt.start();
    }
    private void wayDetection(Activity activity,Bitmap bit){
        activity.runOnUiThread(() -> {
            int now = (int)Calendar.getInstance().getTimeInMillis();
            //Toast.makeText(getApplicationContext(), "cam delay : "+(now-start), Toast.LENGTH_SHORT).show();
            WayDetection wd = new WayDetection(bit);


            try{
                //Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                ImageView im = findViewById(R.id.imageView);
                TextView tv = findViewById(R.id.textView);
                int time = (int)Calendar.getInstance().getTimeInMillis();
                strTV = wd.message+" , "+(time - start);
                tv.setText(strTV);
                im.setImageBitmap(bit);
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void signDetection(Activity activity,Bitmap bit){
        activity.runOnUiThread(() -> {
            int now = (int)Calendar.getInstance().getTimeInMillis();
            //Toast.makeText(getApplicationContext(), "cam delay : "+(now-start), Toast.LENGTH_SHORT).show();
            SignDetection sd = new SignDetection(bit,false);


            try{
                //Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                ImageView im = findViewById(R.id.imageView);
                TextView tv = findViewById(R.id.textView);
                int time = (int)Calendar.getInstance().getTimeInMillis();
                strTV = sd.area.message+" , "+(time - start);
                tv.setText(strTV);
                im.setImageBitmap(sd.replace());
            }catch (Exception e){
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    void analyst(CameraSelector cameraSelector,Preview preview){
        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();
        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), imageProxy -> {
            int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();
            @SuppressLint("UnsafeOptInUsageError")
            Image i = imageProxy.getImage();

            //im.setImageBitmap(getResizedBitmap(bitmapImage,50,50));
            //im = findViewById(R.id.imageView);
            //im.setImageBitmap(bitmapImage);
            // insert your code here.
            Log.e("getPixelStride",""+imageProxy.getPlanes()[0].getPixelStride());
            Log.e("planse",""+imageProxy.getPlanes()[0].getBuffer().get());
            Log.e("H",""+i.getPlanes().length);
            Log.e("analys","");
            // after done, release the ImageProxy object
            imageProxy.close();
        });

        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);
    }
    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }
}