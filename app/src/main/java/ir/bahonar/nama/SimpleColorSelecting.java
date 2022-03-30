package ir.bahonar.nama;

import android.graphics.Bitmap;
import android.graphics.Color;

public class SimpleColorSelecting {
    public String array;
    public SimpleColorSelecting(Bitmap bm){
        array = "";
        array += getSymbol(colorDetection(Long.toHexString(bm.getPixel(getX(bm,0),getY(bm,0)))));
        array += getSymbol(colorDetection(Long.toHexString(bm.getPixel(getX(bm,1),getY(bm,1)))));
        array += getSymbol(colorDetection(Long.toHexString(bm.getPixel(getX(bm,2),getY(bm,2)))));
        array += getSymbol(colorDetection(Long.toHexString(bm.getPixel(getX(bm,3),getY(bm,3)))));
        array += getSymbol(colorDetection(Long.toHexString(bm.getPixel(getX(bm,4),getY(bm,4)))));
        array += getSymbol(colorDetection(Long.toHexString(bm.getPixel(getX(bm,5),getY(bm,5)))));
        array += getSymbol(colorDetection(Long.toHexString(bm.getPixel(getX(bm,6),getY(bm,6)))));
        array += getSymbol(colorDetection(Long.toHexString(bm.getPixel(getX(bm,7),getY(bm,7)))));
        markColors(bm);
    }
    private void markColors(Bitmap bm){
        bm.setPixel(getX(bm,0),getY(bm,0),Color.WHITE);
        bm.setPixel(getX(bm,1),getY(bm,1),Color.WHITE);
        bm.setPixel(getX(bm,2),getY(bm,2),Color.WHITE);
        bm.setPixel(getX(bm,3),getY(bm,3),Color.WHITE);
        bm.setPixel(getX(bm,4),getY(bm,4),Color.WHITE);
        bm.setPixel(getX(bm,5),getY(bm,5),Color.WHITE);
        bm.setPixel(getX(bm,6),getY(bm,6),Color.WHITE);
        bm.setPixel(getX(bm,7),getY(bm,7),Color.WHITE);
    }
    private int getX(Bitmap bm,int x){
        return (int)(bm.getWidth()*setting.colorsX[x]);
    }
    private int getY(Bitmap bm,int y){
        return (int)(bm.getHeight()*setting.colorsY[y]);
    }

    private String getSymbol(COLORS colors){
        return new String(new char[]{colors.toString().charAt(0)});
    }
    private COLORS colorDetection(String color){
        String blue = String.valueOf(color.charAt(6)) + color.charAt(7);
        String green = String.valueOf(color.charAt(4)) + color.charAt(5);
        String red = String.valueOf(color.charAt(2)) + color.charAt(3);


        long longBlue = Long.parseLong(blue,16);
        long longGreen = Long.parseLong(green,16);
        long longRed = Long.parseLong(red,16);

        double max = Math.max(Math.max(longBlue,longGreen),longRed);
        double min = Math.min(Math.min(longBlue,longGreen),longRed);

        if(max - min < 25)
            return COLORS.ELSE;
        if((longRed-longGreen)<30 && longRed > 200 && longGreen > 180)
            return COLORS.YELLOW;
        if(max == longBlue)
            return COLORS.BLUE;
        if(max == longRed)
            return COLORS.RED;
        if(max == longGreen)
            return COLORS.GREEN;
        return COLORS.ELSE;

    }
}
