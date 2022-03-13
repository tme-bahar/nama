package ir.bahonar.nama;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class setting extends AppCompatActivity {
    public static long time = 500;
    public static int width = 240;
    public static int height = 360;
    public static double maxTop = 0.4;
    public static double bottomLineX = 0.8;
    public static double topLineX = 0.6;
    public static double maxHalf = 0.1;
    public static double maxRes = 0.05;
    public static double minRight = 0.9;
    public static double maxRight = 0.95;
    public static boolean signDet = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button save = findViewById(R.id.button);

        TextView timeTV = findViewById(R.id.editTextNumberDecimal);
        TextView widthTV = findViewById(R.id.editTextNumberDecimal2);
        TextView heightTV = findViewById(R.id.editTextNumberDecimal3);
        TextView maxTopTV = findViewById(R.id.editTextNumberDecimal4);
        TextView bottomLineXTV = findViewById(R.id.editTextNumberDecimal5);
        TextView topLineXTV = findViewById(R.id.editTextNumberDecimal6);
        TextView maxHalfTV = findViewById(R.id.editTextNumberDecimal7);
        TextView maxResTV = findViewById(R.id.editTextNumberDecimal8);
        TextView minRightTV = findViewById(R.id.editTextNumberDecimal9);
        TextView maxRightTV = findViewById(R.id.editTextNumberDecimal10);

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch sign = findViewById(R.id.switch1);


        timeTV.setText(String.valueOf(time));
        widthTV.setText(String.valueOf(width));
        heightTV.setText(String.valueOf(height));
        maxTopTV.setText(String.valueOf(maxTop));
        bottomLineXTV.setText(String.valueOf(bottomLineX));
        topLineXTV.setText(String.valueOf(topLineX));
        maxHalfTV.setText(String.valueOf(maxHalf));
        maxResTV.setText(String.valueOf(maxRes));
        minRightTV.setText(String.valueOf(minRight));
        maxRightTV.setText(String.valueOf(maxRight));

        sign.setChecked(signDet);


        save.setOnClickListener(v->{
            time = Long.parseLong(timeTV.getText().toString());
            width = Integer.parseInt(widthTV.getText().toString());
            height = Integer.parseInt(heightTV.getText().toString());
            maxTop = Double.parseDouble(maxTopTV.getText().toString());
            bottomLineX = Double.parseDouble(bottomLineXTV.getText().toString());
            topLineX = Double.parseDouble(topLineXTV.getText().toString());
            maxHalf = Double.parseDouble(maxHalfTV.getText().toString());
            maxRes = Double.parseDouble(maxResTV.getText().toString());
            minRight = Double.parseDouble(minRightTV.getText().toString());
            maxRight = Double.parseDouble(maxRightTV.getText().toString());
            signDet = sign.isChecked();

            startActivity(new Intent(this,MainActivity.class));
            finish();
        });

    }
}