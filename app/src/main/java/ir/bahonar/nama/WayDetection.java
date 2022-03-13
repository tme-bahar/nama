package ir.bahonar.nama;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.CalendarContract;

public class WayDetection {

    public String message = "";

    public WayDetection(Bitmap bm){

        int top = findTop(bm);
        int left1 = findLeft(bm,(int)(bm.getHeight()*setting.bottomLineX));
        int left2 = findLeft(bm,(int)(bm.getHeight()*setting.topLineX));
        int right1 = findRight(bm,(int)(bm.getHeight()*setting.bottomLineX));
        int right2 = findRight(bm,(int)(bm.getHeight()*setting.topLineX));
        message = "top:"+top +" \n bottom:("+left1+","+right1+") \n top:("+left2+","+right2+")";
    }
    private int findTop(Bitmap bm){
        int result = bm.getHeight()-1;
        while (result != 1)
            if(isBlack(bm.getPixel(bm.getWidth()/2,result)))
                return result;
            else{
                changeColor(bm,bm.getWidth()/2,result);
                result --;
            }
        return result;
    }
    private int findLeft(Bitmap bm,int y){
        int result = bm.getWidth()/2;
        while (result != 1)
            if(isBlack(bm.getPixel(result,y)))
                return (bm.getWidth()/2)-result;
            else{
                changeColor(bm,result,y);
                result --;
            }
        return (bm.getWidth()/2)-result;
    }
    private int findRight(Bitmap bm,int y){
        int result = bm.getWidth()/2;
        while (result != bm.getWidth()-1)
            if(isBlack(bm.getPixel(result,y)))
                return result - (bm.getWidth()/2);
            else {
                changeColor(bm,result,y);
                result++;
            }
        return result;
    }
    private boolean isBlack(int pixel){
        Col c = new Col(pixel);
        int res = Math.max(c.red,Math.max(c.blue,c.green)) - Math.min(c.red,Math.min(c.blue,c.green));
        return (c.red < 170) && (c.green < 170) && (c.blue < 170) && res < 35;
    }

    private void changeColor(Bitmap bm,int x, int y){
        bm.setPixel(x,y, Color.BLUE);
    }
}
