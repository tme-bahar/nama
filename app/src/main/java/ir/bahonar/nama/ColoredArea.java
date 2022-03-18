package ir.bahonar.nama;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

public class ColoredArea {

    public Pixel average ;
    public int size;
    public ColoredArea(List<Pixel> points, Bitmap bm,COLORS colors){
        this.size = points.size();
        average = new Pixel(colors,(int)getAverageX(points),(int)getAverageY(points));

    }

    public float getAverageX(List<Pixel> points){
        int sum = 0;
        for (Pixel pixel:points)
            sum += pixel.x;
        return sum/(float)points.size();
    }
    public float getAverageY(List<Pixel> points){
        int sum = 0;
        for (Pixel pixel:points)
            sum += pixel.y;
        return sum/(float)points.size();
    }

    private void change(Bitmap bm,int x, int y){
        bm.setPixel(x,y, Color.parseColor("#ffff00"));
    }


}
