package ir.bahonar.nama;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ColorDetection {

    public String message;
    private final COLORS[] availableColors = {COLORS.BLUE,COLORS.RED,COLORS.GREEN,COLORS.YELLOW};
    public ColorDetection(Bitmap bm){
        List<ColoredArea> list = getAreas(bm);

        message = detectAndSort(list);
    }
    private String detectAndSort(List<ColoredArea> list){
        List<ColoredArea> reds = new ArrayList<>();
        List<ColoredArea> blues = new ArrayList<>();
        List<ColoredArea> greens = new ArrayList<>();
        List<ColoredArea> yellows = new ArrayList<>();
        
        for (ColoredArea ca:list)
            if(ca.average.color == COLORS.BLUE)
                blues.add(ca);
            else if(ca.average.color == COLORS.RED)
                reds.add(ca);
            else if(ca.average.color == COLORS.GREEN)
                greens.add(ca);
            else if(ca.average.color == COLORS.YELLOW)
                yellows.add(ca);
            
        reds = getTowMaxes(reds);
        blues = getTowMaxes(blues);
        greens = getTowMaxes(greens);
        yellows = getTowMaxes(yellows);
        Log.e("reds0x",reds.get(0).average.x+"");
        Log.e("reds0y",reds.get(0).average.y+"");
        Log.e("reds1x",reds.get(1).average.x+"");
        Log.e("reds1y",reds.get(1).average.y+"");
        Log.e("green0x",greens.get(0).average.x+"");
        Log.e("green0y",greens.get(0).average.y+"");
        Log.e("green1x",greens.get(1).average.x+"");
        Log.e("green1y",greens.get(1).average.y+"");
        Log.e("blue0x",blues.get(0).average.x+"");
        Log.e("blue0y",blues.get(0).average.y+"");
        Log.e("blue1x",blues.get(1).average.x+"");
        Log.e("blue1y",blues.get(1).average.y+"");


        ArrayList<ColoredArea> result = new ArrayList<>();
        if(reds != null)
            result.addAll(reds);
        if(blues != null)
            result.addAll(blues);
        if(greens != null)
            result.addAll(greens);
        if(yellows != null)
            result.addAll(yellows);
        
        ColoredArea top = top(list);
        ColoredArea bottom = bottom(list);
        ColoredArea left = left(list);
        ColoredArea right = right(list);
        ColoredArea topRight = topRight(list,top,right);
        ColoredArea topLeft = topLeft(list,top,left);
        ColoredArea bottomRight = bottomRight(list,bottom,right);
        ColoredArea bottomLeft = bottomRight(list,bottom,left);

        if((top != null) && (bottom != null) && (right != null) && (left != null) &&
           (topLeft != null) && (topRight != null) && (bottomRight != null) && (bottomLeft != null))
        return makeString(top,bottom,right,left,topRight,topLeft,bottomRight,bottomLeft);
        else
            return "";
    }
    
    private String makeString(ColoredArea top,
                              ColoredArea bottom,
                              ColoredArea right,
                              ColoredArea left,
                              ColoredArea topRight,
                              ColoredArea topLeft,
                              ColoredArea bottomRight,
                              ColoredArea bottomLeft){
        return  new String(new char[]{
                        bottom.average.color.toString().charAt(0) ,
                        bottomRight.average.color.toString().charAt(0) ,
                        right.average.color.toString().charAt(0) ,
                        topRight.average.color.toString().charAt(0) ,
                        top.average.color.toString().charAt(0) ,
                        topLeft.average.color.toString().charAt(0) ,
                        left.average.color.toString().charAt(0) ,
                        bottomLeft.average.color.toString().charAt(0)
        });
    }
    
    private ColoredArea topLeft (List<ColoredArea> list,ColoredArea top,ColoredArea left){
        if(list.size() == 0 )
            return null;

        ColoredArea topLeft = list.get(0);
        for (ColoredArea ca:list)
            if(ca.average.y > top.average.y && ca.average.y < left.average.y
            && ca.average.x > left.average.x && ca.average.x < top.average.x)
                topLeft = ca;

        return topLeft;
    }

    private ColoredArea topRight (List<ColoredArea> list,ColoredArea top,ColoredArea right){
        if(list.size() == 0 )
            return null;

        ColoredArea topRight = list.get(0);
        for (ColoredArea ca:list)
            if(ca.average.y > top.average.y && ca.average.y < right.average.y
                    && ca.average.x < right.average.x && ca.average.x > top.average.x)
                topRight = ca;

        return topRight;
    }
    
    private ColoredArea bottomLeft (List<ColoredArea> list,ColoredArea bottom,ColoredArea left){
        if(list.size() == 0 )
            return null;

        ColoredArea bottomLeft = list.get(0);
        for (ColoredArea ca:list)
            if(ca.average.y < bottom.average.y && ca.average.y > left.average.y
                    && ca.average.x > left.average.x && ca.average.x < bottom.average.x)
                bottomLeft = ca;

        return bottomLeft;
    }

    private ColoredArea bottomRight (List<ColoredArea> list,ColoredArea bottom,ColoredArea right){
        if(list.size() == 0 )
            return null;

        ColoredArea bottomRight = list.get(0);
        for (ColoredArea ca:list)
            if(ca.average.y < bottom.average.y && ca.average.y > right.average.y
                    && ca.average.x < right.average.x && ca.average.x > bottom.average.x)
                bottomRight = ca;

        return bottomRight;
    }

    private ColoredArea right(List<ColoredArea> list){
        if(list.size() == 0 )
            return null;

        ColoredArea right = list.get(0);
        for (ColoredArea ca:list)
            if(right.average.x < ca.average.x)
                right = ca;

        return right;
    }
    
    private ColoredArea left(List<ColoredArea> list){
        if(list.size() == 0 )
            return null;

        ColoredArea left = list.get(0);
        for (ColoredArea ca:list)
            if(left.average.x > ca.average.x)
                left = ca;

        return left;
    }
    
    private ColoredArea top(List<ColoredArea> list){
        if(list.size() == 0 )
            return null;

        ColoredArea top = list.get(0);
        for (ColoredArea ca:list)
            if(top.average.y > ca.average.y)
                top = ca;

        return top;
    }
    
    private ColoredArea bottom(List<ColoredArea> list){
        if(list.size() == 0 )
            return null;
        
        ColoredArea bottom = list.get(0);
        for (ColoredArea ca:list) 
            if(bottom.average.y < ca.average.y)
                bottom = ca;
            
        return bottom;
    }
    
    private List<ColoredArea> getTowMaxes(List<ColoredArea> list){
        if (list.size() == 0)
            return null;
        if(list.size() == 1)
            return list;
        ColoredArea veryMax = list.get(0);
        ColoredArea max = list.get(0);
        for (ColoredArea ca:list) 
            if(ca.size > veryMax.size){
                max = veryMax;
                veryMax = ca;
            }else if(ca.size > max.size)
                max = ca;
        ArrayList<ColoredArea> result = new ArrayList<>();
        result.add(veryMax);
        result.add(max);
        return result;
    }
    private List<ColoredArea> getAreas(Bitmap bm){
        List<ColoredArea> result = new ArrayList<>();
        for(int i = 0; i < bm.getWidth();i++)
            for(int j = 0; j < bm.getHeight(); j++)
                for (COLORS c:availableColors) 
                    if(isColor(bm.getPixel(i,j),c))
                    {
                        List<Pixel> area = new ArrayList<>();
                        area.add(new Pixel(c,i,j));
                        bm.setPixel(i,j, Color.parseColor("#000000"));
                        getArea(bm,area,i,j,c);
                        result.add(new ColoredArea(area,bm,c));
                    }
        return result;
    }
    private void getArea(Bitmap bm,List<Pixel> area,int x,int y,COLORS colors){
        int nextX ;
        int nextY ;
        if ((x!=0) && (y!=0) && isColor(bm.getPixel(x-1,y-1),colors)) {
            nextX = -1;
            nextY = -1;
            area.add(new Pixel(colors, x + nextX, y + nextY));
            getArea(bm, area, x + nextX, y + nextY,colors);
        }
        if ((x!=0) && isColor(bm.getPixel(x-1,y),colors)) {
            nextX = -1;
            nextY = 0;
            area.add(new Pixel(colors, x + nextX, y + nextY));
            bm.setPixel(x + nextX,y + nextY,Color.parseColor("#000000"));
            getArea(bm, area, x + nextX, y + nextY,colors);
        }
        if ((x!=0) && (y+1 != bm.getHeight()) && isColor(bm.getPixel(x-1,y+1),colors)) {
            nextX = -1;
            nextY = 1;
            area.add(new Pixel(colors, x + nextX, y + nextY));
            bm.setPixel(x + nextX,y + nextY,Color.parseColor("#000000"));
            getArea(bm, area, x + nextX, y + nextY,colors);
        }
        if ((y!=0) && isColor(bm.getPixel(x,y-1),colors)) {
            nextX = 0;
            nextY = -1;
            area.add(new Pixel(colors, x + nextX, y + nextY));
            bm.setPixel(x + nextX,y + nextY,Color.parseColor("#000000"));
            getArea(bm, area, x + nextX, y + nextY,colors);
        }
        if ((y+1 != bm.getHeight()) && isColor(bm.getPixel(x,y+1),colors)) {
            nextX = 0;
            nextY = 1;
            area.add(new Pixel(colors, x + nextX, y + nextY));
            bm.setPixel(x + nextX,y + nextY,Color.parseColor("#000000"));
            getArea(bm, area, x + nextX, y + nextY,colors);
        }
        if ((y!=0) && (x+1 != bm.getWidth()) && isColor(bm.getPixel(x+1,y-1),colors)) {
            nextX = 1;
            nextY = -1;
            area.add(new Pixel(colors, x + nextX, y + nextY));
            bm.setPixel(x + nextX,y + nextY,Color.parseColor("#000000"));
            getArea(bm, area, x + nextX, y + nextY,colors);
        }
        if ((x+1 != bm.getWidth()) && isColor(bm.getPixel(x+1,y),colors)) {
            nextX = 1;
            nextY = 0;
            area.add(new Pixel(colors, x + nextX, y + nextY));
            bm.setPixel(x + nextX,y + nextY,Color.parseColor("#000000"));
            getArea(bm, area, x + nextX, y + nextY,colors);
        }
        if ((x+1 != bm.getWidth())&& (y+1 != bm.getHeight()) &&isColor(bm.getPixel(x+1,y+1),colors)) {
            nextX = 1;
            nextY = 1;
            area.add(new Pixel(colors, x + nextX, y + nextY));
            bm.setPixel(x + nextX,y + nextY,Color.parseColor("#000000"));
            getArea(bm, area, x + nextX, y + nextY,colors);
        }
    }
    private boolean isColor(int pixel,COLORS colors){
        switch (colors){
            case BLUE:
                return isBlue(pixel);
            case RED:
                return isRed(pixel);
            case GREEN:
                return isGreen(pixel);
            case YELLOW:
                return isYellow(pixel);
        }
        return false;
    }
    private boolean isYellow(int pixel){
        Col c = new Col(pixel);
        int min = Math.min(c.blue,Math.min(c.red,c.green));
        int max = Math.max(c.blue,Math.max(c.red,c.green));
        return (c.green > 180) && (c.red > 180) && (c.blue < 70) && (c.blue > 25) && (max - min > 20);
    }
    private boolean isGreen(int pixel){
        Col c = new Col(pixel);
        int min = Math.min(c.blue,Math.min(c.red,c.green));
        int max = Math.max(c.blue,Math.max(c.red,c.green));
        return (c.green > 140) && (c.red < 130) && (c.blue < 130)&& (max - min > 20);
    }

    private boolean isRed(int pixel){
        Col c = new Col(pixel);
        int min = Math.min(c.blue,Math.min(c.red,c.green));
        int max = Math.max(c.blue,Math.max(c.red,c.green));
        return ((c.blue - c.green) < 30)
                && ((c.blue - c.green) > -30) && (c.red > 200) && (c.blue < 200) && (c.green < 200)&& (max - min > 20);
    }

    private boolean isBlue(int pixel){
        Col c = new Col(pixel);
        int min = Math.min(c.blue,Math.min(c.red,c.green));
        int max = Math.max(c.blue,Math.max(c.red,c.green));
        return  (c.blue > 190) && (c.red < 190) && (c.green < 190)&& (max - min > 20);
    }
}
