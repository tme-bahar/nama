package ir.bahonar.nama;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class setting extends AppCompatActivity {

    public static long time = 500;
    public static int width = 240;
    public static int height = 360;
    public static double maxTop = 0.64;
    public static double bottomLineY = 0.92;
    public static double topLineY = 0.74;
    public static double minLeftTop = 0.01;
    public static double maxLeftTop = 0.2;
    public static double minRightTop = 0.6;
    public static double maxRightTop = 0.99;
    public static double minLeftBottom = 0.01;
    public static double maxLeftBottom = 0.1;
    public static double minRightBottom = 0.65;
    public static double maxRightBottom = 0.99;
    public static boolean signDet = false;
    public static int blackLimit = 120;

    public static int minutes = 4;

    public static boolean colorSelection = false;

    public static float[] colorsX = {0.5f,0.6f,0.65f,0.6f,0.5f,0.4f,0.35f,0.4f};
    public static float[] colorsY = {0.5f,0.55f,0.65f,0.55f,0.5f,0.45f,0.35f,0.45f};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button save = findViewById(R.id.button);

        TextView timeTV = findViewById(R.id.editTextNumberDecimal);
        TextView widthTV = findViewById(R.id.editTextNumberDecimal2);
        TextView heightTV = findViewById(R.id.editTextNumberDecimal3);
        TextView maxTopTV = findViewById(R.id.editTextNumberDecimal4);
        TextView bottomLineYTV = findViewById(R.id.editTextNumberDecimal5);
        TextView topLineYTV = findViewById(R.id.editTextNumberDecimal6);
        TextView minLeftTopTV = findViewById(R.id.editTextNumberDecimal7);
        TextView maxLeftTopTV = findViewById(R.id.editTextNumberDecimal8);
        TextView minRightTopTV = findViewById(R.id.editTextNumberDecimal9);
        TextView maxRightTopTV = findViewById(R.id.editTextNumberDecimal10);
        TextView minLeftBottomTV = findViewById(R.id.editTextNumberDecimal11);
        TextView maxLeftBottomTV = findViewById(R.id.editTextNumberDecimal12);
        TextView minRightBottomTV = findViewById(R.id.editTextNumberDecimal13);
        TextView maxRightBottomTV = findViewById(R.id.editTextNumberDecimal14);
        EditText blackLimitET = findViewById(R.id.editTextPhone);
        EditText courseTimeET = findViewById(R.id.editTextPhone2);

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch sign = findViewById(R.id.switch1);


        timeTV.setText(String.valueOf(time));
        widthTV.setText(String.valueOf(width));
        heightTV.setText(String.valueOf(height));
        maxTopTV.setText(String.valueOf(maxTop));
        bottomLineYTV.setText(String.valueOf(bottomLineY));
        topLineYTV.setText(String.valueOf(topLineY));
        minLeftTopTV.setText(String.valueOf(minLeftTop));
        maxLeftTopTV.setText(String.valueOf(maxLeftTop));
        minRightTopTV.setText(String.valueOf(minRightTop));
        maxRightTopTV.setText(String.valueOf(maxRightTop));
        minLeftBottomTV.setText(String.valueOf(minLeftBottom));
        maxLeftBottomTV.setText(String.valueOf(maxLeftBottom));
        minRightBottomTV.setText(String.valueOf(minRightBottom));
        maxRightBottomTV.setText(String.valueOf(maxRightBottom));
        blackLimitET.setText(String.valueOf(blackLimit));
        courseTimeET.setText(String.valueOf(minutes));

        sign.setChecked(signDet);


        save.setOnClickListener(v->{


            time = Long.parseLong(timeTV.getText().toString());
            width = Integer.parseInt(widthTV.getText().toString());
            height = Integer.parseInt(heightTV.getText().toString());
            maxTop = Double.parseDouble(maxTopTV.getText().toString());
            bottomLineY = Double.parseDouble(bottomLineYTV.getText().toString());
            topLineY = Double.parseDouble(topLineYTV.getText().toString());
            minLeftTop = Double.parseDouble(minLeftTopTV.getText().toString());
            maxLeftTop = Double.parseDouble(maxLeftTopTV.getText().toString());
            minRightTop = Double.parseDouble(minRightTopTV.getText().toString());
            maxRightTop = Double.parseDouble(maxRightTopTV.getText().toString());
            minLeftBottom = Double.parseDouble(minLeftBottomTV.getText().toString());
            maxLeftBottom = Double.parseDouble(maxLeftBottomTV.getText().toString());
            minRightBottom = Double.parseDouble(minRightBottomTV.getText().toString());
            maxRightBottom = Double.parseDouble(maxRightBottomTV.getText().toString());
            blackLimit = Integer.parseInt(blackLimitET.getText().toString());
            minutes= Integer.parseInt(courseTimeET.getText().toString());
            signDet = sign.isChecked();


            startActivity(new Intent(this,MainActivity.class));
            finish();
        });

    }
}