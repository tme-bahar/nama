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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
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
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.telephony.TelephonyManager;
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
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    ProcessCameraProvider cameraProvider;
    public static CardView cv;
    ImageView im;
    Bitmap bm;
    Preview preview;
    CameraSelector cameraSelector;
    ImageCapture imageCapture;
    boolean configured = false;
    int REQUEST_ENABLE_BT = 0;
    String strTV = "";
    ImageView signViewer;
    public static TextView tv;
    int start = 0;
    private SIGN[] sign = {SIGN.ELSE,SIGN.ELSE,SIGN.ELSE};
    public static TextView blackLimit;
    OutputStream ops;
    private SIGN lastSign = SIGN.ELSE;
    private boolean processingSwitch = true;
    private int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blackLimit = findViewById(R.id.textView3);
        im = findViewById(R.id.imageView);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        this.signViewer = findViewById(R.id.imageView2);



        //int picture = R.drawable.test;
        //Bitmap bm = BitmapFactory.decodeResource(getResources(),picture);
        //Bitmap bit = getResizedBitmap(bm,120,180);
        //ColorDetection cd = new ColorDetection(bit);
        //CentralColorDetection ccd = new CentralColorDetection(bit);
        //im.setImageBitmap(bit);

        cv = findViewById(R.id.cardView);
        tv = findViewById(R.id.textView);
        //tv.setText(cd.message);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
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
            processingSwitch =true;
            capt();
            //analyst(cameraSelector,preview,this);
            try {
                send("J");
                send("J");
            } catch (IOException e) {
                e.printStackTrace();
            }
            new CountDownTimer(setting.minutes * 60000L, setting.minutes * 60000L) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    try {
                        send("s");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        });
        im.setOnClickListener(v->{

            im.setImageBitmap(bm);
            tv.setText(strTV);

        });

        findViewById(R.id.button2).setOnClickListener(v->{
            finish();
            startActivity(new Intent(this,setting.class));
        });

        findViewById(R.id.button4).setOnClickListener(v->{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("choose");
                alertDialog.setPositiveButton("page", (dialogInterface, i) -> {
                    finish();
                    startActivity(new Intent(this,Bluetooth.class));
                });
                alertDialog.setNegativeButton("activate",(dialogInterface, i) -> {
                    try {
                        bluetoothConnector();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                });
                alertDialog.show();

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




    void capt(){
        start = (int)Calendar.getInstance().getTimeInMillis();

        Activity activity = this;
        CountDownTimer cdt = new CountDownTimer(setting.time, setting.time) {

            public void onTick(long millisUntilFinished) {
            }

            @SuppressLint("SetTextI18n")
            public void onFinish() {
                signViewer.setImageResource(
                        (lastSign == SIGN.RED ? R.drawable.untitled3 :
                                (lastSign == SIGN.RIGHT ? R.drawable.untitled :
                                        (lastSign == SIGN.LEFT ? R.drawable.untitled2 : R.drawable.ic_launcher_foreground))));
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
                    @SuppressLint({"RestrictedApi", "SetTextI18n"})
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {

                        byte[] bitmapData = fOut.toByteArray();
                        bm = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
                        Bitmap bit = getResizedBitmap(bm,setting.width,setting.height);
                        Bitmap rBit = RotateBitmap(bit,90);
                        try{
                            //if(processingSwitch){
                                activity.runOnUiThread(()->{

                                    //blackLimit.setText(String.valueOf(setting.blackLimit));
                                    //blackLimit.setText(sign[0].toString()+sign[1]+sign[2]);
                                });
                                signDetection(activity,rBit);
                                wayDetection(activity,rBit);
                            //}
                            //SimpleColorSelecting scs = new SimpleColorSelecting(rBit);
                            //TextView tv = findViewById(R.id.textView);
                            //ImageView im = findViewById(R.id.imageView);
                            //im.setImageBitmap(rBit);
                            //tv.setText(scs.array);
                            setting.signDet = !setting.signDet;
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
            WayDetection wd = new WayDetection(bit);

            try {
                if(lastSign == SIGN.A1)
                    send("a");
                else if(lastSign == SIGN.A2)
                    send("b");
                else if(lastSign == SIGN.B1)
                    send("c");
                else if(lastSign == SIGN.B2)
                    send("d");
                else if(wd.way == WayDetection.Way.KR)
                    keepRight(3);
                else if(wd.way == WayDetection.Way.KL)
                    keepLeft(3);
                else if(wd.way == WayDetection.Way.TL)
                    keepLeft(10);
                else if(wd.way == WayDetection.Way.TR)
                    keepRight(10);
                else if((lastSign == SIGN.LEFT) &&
                        (wd.way == WayDetection.Way.CL
                        || wd.way == WayDetection.Way.CC
                        || wd.way == WayDetection.Way.CRL))
                {
                    turnLeft();
                    lastSign = SIGN.ELSE;
                }
                else if((lastSign == SIGN.RIGHT)&&
                        (wd.way == WayDetection.Way.CR
                        || wd.way == WayDetection.Way.CC
                        || wd.way == WayDetection.Way.CRL))
                {
                    turnRight();
                    lastSign = SIGN.ELSE;
                }
                else if (lastSign == SIGN.RED){
                    tempStop();
                }

            }catch (Exception e){
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }


            try{
                //Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                ImageView im = findViewById(R.id.imageView);
                tv = findViewById(R.id.textView);
                int time = (int)Calendar.getInstance().getTimeInMillis();
                strTV = wd.message+" , "+(time - start);
                if(tv.getText().toString().split("\n").length < 7)
                    tv.setText(tv.getText().toString()+"\n"+strTV);
                else
                    tv.setText("\n"+strTV);
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
            addSign(sd.sign);
            if(AreSignsSame() && lastSign == SIGN.ELSE && sd.sign != SIGN.STRIGHT)
                lastSign = sd.sign;
            blackLimit.setText("E "+lastSign + "\n" + sign[0] + sign[1] + sign[2]);
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



    void analyst(CameraSelector cameraSelector,Preview preview,Activity activity){
        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();
        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), imageProxy -> {
            Log.e("analys", counter+"");
            if(counter%3 == 0) {
                @SuppressLint("UnsafeOptInUsageError")
                Image i = imageProxy.getImage();
                ByteBuffer buffer = i.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.capacity()];
                buffer.get(bytes);
                Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
                activity.runOnUiThread(() -> {
                    ImageView im = findViewById(R.id.imageView);
                    im.setImageBitmap(bitmapImage);
                });
                //im.setImageBitmap(getResizedBitmap(bitmapImage,50,50));
                //im = findViewById(R.id.imageView);
                //im.setImageBitmap(bitmapImage);
                // insert your code here.
                Log.e("getPixelStride", "" + imageProxy.getPlanes()[0].getPixelStride());
                //Log.e("planse", "" + imageProxy.getPlanes()[0].getBuffer().get());
                Log.e("H", "" + i.getPlanes().length);
                Log.e("analys", "");
                // after done, release the ImageProxy object
                imageProxy.close();
            }
            counter++;
        });

        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);
    }

    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.setRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private boolean AreSignsSame(){
        return sign[0] == sign[1] && sign[1] == sign[2];
    }
    private void addSign(SIGN sign){
        this.sign[0] = this.sign[1];
        this.sign[1] = this.sign[2];
        this.sign[2] = sign;
    }

    private void bluetoothConnector() throws IOException {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {
                        BluetoothConnector bc = new BluetoothConnector(device,true,bluetoothAdapter,null);
                        BluetoothConnector.BluetoothSocketWrapper bsw = bc.connect();
                        ops = bsw.getOutputStream();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "zero", Toast.LENGTH_SHORT).show();
                }


            }
        }
    }

    private void send(String data) throws IOException {
        if(ops == null)
            return;
        byte[] result = new byte[data.length()];
        for (int i = 0 ; i < data.length() ; i++)
            result[i] = (byte) data.charAt(i);
        ops.write(result);
    }

    private void keepLeft(int num) throws IOException {
        counter++;
        //if(counter != 2)
        //    return;
        counter = 0;
        send("L");
        send(String.valueOf((char)num));

    }

    private void keepRight(int num) throws IOException {
        counter++;
        //if(counter != 2)
        //    return;
        counter = 0;
        send("R");
        send(String.valueOf((char)num));
    }
    private void turnLeft() throws IOException {
        //if(!processingSwitch)
        //    return;
//        //processingSwitch =false;
//        send("L");
//        send(String.valueOf((char)90));
        if(processingSwitch) {
            processingSwitch = false;
            new CountDownTimer(6500, 6500) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                processingSwitch = true;
                }
            }.start();
            new CountDownTimer(5000, 5000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    try {
                        send("L");
                        send(String.valueOf((char) 70));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();


        }
    }
    private void turnRight() throws IOException {
        if(processingSwitch) {
            processingSwitch = false;
            new CountDownTimer(6500, 6500) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    processingSwitch = true;
                }
            }.start();
            new CountDownTimer(5000, 5000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    try {
                        send("R");
                        send(String.valueOf((char) 70));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
    private void tempStop() throws IOException {
        if(processingSwitch) {
            processingSwitch = false;
            send("s");
            new CountDownTimer(10000, 10000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    try {
                        send("f");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            new CountDownTimer(15000, 15000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    processingSwitch = false;
                    lastSign = SIGN.ELSE;
                }
            }.start();
        }
    }


/*
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
*/
}