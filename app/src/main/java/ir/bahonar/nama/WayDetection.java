package ir.bahonar.nama;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.util.Log;

public class WayDetection {

    public String message = "";
    public String stu = "str";
    public Way way = Way.STR;
    public WayDetection(Bitmap bm){
        setBlackLimit(bm,0.78f);
        int top = findTop(bm);
        int left1 = findLeft(bm,(int)(bm.getHeight()*setting.bottomLineY));
        int right1 = findRight(bm,(int)(bm.getHeight()*setting.bottomLineY));

        int left2 = bm.getWidth()/2;
        int right2 = bm.getWidth()/2;
        if(top < (setting.maxTop + 0.1)* bm.getHeight()){
            left2 = findLeft(bm,(int)(bm.getHeight()*setting.topLineY));
            right2 = findRight(bm,(int)(bm.getHeight()*setting.topLineY));
        }

        setWay(bm,getRegion(bm,left2,true)
                ,getRegion(bm,right2,true)
                ,getRegion(bm,left1,false)
                ,getRegion(bm,right1,false)
                ,top);

        colorLineX(bm,(int)(bm.getWidth()*setting.minLeftBottom),(Color.MAGENTA),0.4f,1);
        colorLineX(bm,(int)(bm.getWidth()*setting.maxLeftBottom),(Color.WHITE),0.4f,1);
        colorLineX(bm,(int)(bm.getWidth()*setting.maxRightBottom),(Color.MAGENTA),0.4f,1);
        colorLineX(bm,(int)(bm.getWidth()*setting.minRightBottom),(Color.WHITE),0.4f,1);
        colorLineX(bm,(int)(bm.getWidth()*setting.minLeftTop),(Color.CYAN),0.2f,0.8f);
        colorLineX(bm,(int)(bm.getWidth()*setting.maxLeftTop),(Color.YELLOW),0.2f,0.8f);
        colorLineX(bm,(int)(bm.getWidth()*setting.maxRightTop),(Color.CYAN),0.2f,0.8f);
        colorLineX(bm,(int)(bm.getWidth()*setting.minRightTop),(Color.YELLOW),0.2f,0.8f);
        colorLineY(bm,(int)(bm.getHeight()*setting.maxTop),(Color.RED),0,1);

/*
        if(left2 < (bm.getWidth())*(setting.maxHalf-setting.maxRes)){
            stu = "Keep Right";
            way = Way.KR;
        }
        else if(right2 < (bm.getWidth())*(setting.maxHalf-setting.maxRes)){
            stu = "Keep left";
            way = Way.KL;
        }
        else if(right2 > (bm.getWidth())*(setting.maxHalf+setting.maxRes)){
            if(top < (bm.getHeight())*(setting.maxTop)){
                stu = "turn right";
                way = Way.TR;
            }else{
                stu = "cross right";
                way = Way.CR;
            }
        }else if(left1 > (bm.getWidth())*(setting.maxHalf+setting.maxRes)){
            if(top < setting.maxTop){
                stu = "turn left";
                way = Way.TL;
            }else{
                stu = "cross left";
                way = Way.CL;
            }
        }
  */
        //message = way.toString() + "\ntop:"+top +" \n bottom:("+left1+","+right1+") \n top:("+left2+","+right2+")";

    }

    private void setWay(Bitmap bm,int topLeft,int topRight,int bottomLeft,int bottomRight,int mid){
        int topType = getType(topLeft,topRight);
        int bottomType = getType(bottomLeft,bottomRight);
        //
        boolean top = mid < setting.maxTop * bm.getHeight() ;
        if(mid < setting.topLineY * bm.getHeight()) {
            if (topType == 2 && bottomType == 2 && top)
                way = Way.CC;
            else if (topType == 2 && bottomType == 2 && !top)
                way = Way.CRL;
            else if ((topType == 1 || topType == 0) && (bottomType == 1 || bottomType == 0 || bottomType == 2) && !top)
                way = Way.TL;
            else if ((topType == 5 || topType == 8) && (bottomType == 5 || bottomType == 8 || bottomType == 2) && !top)
                way = Way.TR;
            else if ((topType == 7 || topType == 8) && ( bottomType == 7 ||bottomType == 8 ) && top)
                way = Way.KR;
            else if ((topType == 0 || topType == 3) && ( bottomType == 0 ||bottomType == 3 ) && top)
                way = Way.KL;
            else if ((topType == 1 || topType == 0) && (bottomType == 1 || bottomType == 0 || bottomType == 2) && top)
                way = Way.CL;
            else if ((topType == 5 || topType == 8) && (bottomType == 5 || bottomType == 8 || bottomType == 2) && top)
                way = Way.CR;
            else
                way = Way.STR;
        }else{
            if (bottomType == 2)
                way = Way.CRL;
            else if ((bottomType == 1 || bottomType == 0) && !top)
                way = Way.TL;
            else if ((bottomType == 5 || bottomType == 8) && !top)
                way = Way.TR;
            else
                way = Way.STR;
        }
        message = way.toString() + "\ntop:"+top +" \n bottom:("+bottomType+") \n top:("+topType+")";
    }

    private int getType(int left,int right){
        //Log.e("left",left+"");
        //Log.e("right",right+"");

        int k = left%5;
        int t = right%5;
        int result ;
        result = k*3;
        result += (t-2);
        return result;
    }
    private int getRegion(Bitmap bm,int i,boolean isTop){
        if(isTop){
            if(i < bm.getWidth()*setting.minLeftTop)
                return 0;
            if(i > bm.getWidth()*setting.minLeftTop && i < bm.getWidth()*setting.maxLeftTop)
                return 1;
            if(i > bm.getWidth()*setting.maxLeftTop && i < bm.getWidth()*setting.minRightTop)
                return 2;
            if(i > bm.getWidth()*setting.minRightTop && i < bm.getWidth()*setting.maxRightTop)
                return 3;
            if(i > bm.getWidth()*setting.maxRightTop)
                return 4;
        }else {
            if(i < bm.getWidth()*setting.minLeftBottom)
                return 5;
            if(i > bm.getWidth()*setting.minLeftBottom && i < bm.getWidth()*setting.maxLeftBottom)
                return 6;
            if(i > bm.getWidth()*setting.maxLeftBottom && i < bm.getWidth()*setting.minRightBottom)
                return 7;
            if(i > bm.getWidth()*setting.minRightBottom && i < bm.getWidth()*setting.maxRightBottom)
                return 8;
            if(i > bm.getWidth()*setting.maxRightBottom)
                return 9;
        }
        return 10;
    }
    
    private int findTop(Bitmap bm){
        int result = (int)(bm.getHeight()*(setting.bottomLineY));
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
                return result;
            else{
                changeColor(bm,result,y);
                result --;
            }
        return result;
    }
    private int findRight(Bitmap bm,int y){
        int result = bm.getWidth()/2;
        while (result != bm.getWidth()-1)
            if(isBlack(bm.getPixel(result,y)))
                return result;
            else {
                changeColor(bm,result,y);
                result++;
            }
        return result;
    }
    private boolean isBlack(int pixel){
        Col c = new Col(pixel);
        int res = Math.max(c.red,Math.max(c.blue,c.green)) - Math.min(c.red,Math.min(c.blue,c.green));
        return (c.red < setting.blackLimit*1.6) && (c.green < setting.blackLimit*1.6) && (c.blue < setting.blackLimit*1.6) && res < 35;
    }

    private void changeColor(Bitmap bm,int x, int y){
        bm.setPixel(x,y, Color.BLUE);
    }
    private void changeToBlack(Bitmap bm,int x, int y){
        bm.setPixel(x,y, Color.BLACK);
    }
    private void changeToWhite(Bitmap bm,int x, int y){
        bm.setPixel(x,y, Color.WHITE);
    }

    enum Way {KR,KL,TR,TL,CR,CL,CC,STR,CRL }


    private void colorLineX(Bitmap bm,int x,int color,float top,float bottom){
        for (int y = (int)(bm.getHeight()*top); y < (int)(bm.getHeight()*bottom);y++)
            bm.setPixel(x,y,color);
    }
    private void colorLineY(Bitmap bm,int y,int color,float left,float right){
        for (int x = (int)(bm.getWidth()*left); x < (int)(bm.getWidth()*right);x++)
            bm.setPixel(x,y,color);
    }

    private void setBlackLimit(Bitmap bm,float height){
        int[] averages = new int[bm.getWidth()];
        for (int i = (int)(bm.getWidth()*0.3) ; i < (int)(bm.getWidth()*0.7) ;i++){
            averages[i] = (int)getColorAverage(bm.getPixel(i,(int)(bm.getHeight()*height)));
            //changeToWhite(bm,i,(int)(bm.getHeight()*height));
        }
        setting.blackLimit = (int)getAverage(new float[]{ getMax(averages),getMin(averages)});
    }

    private float getColorAverage(int color){
        Col c = new Col(color);
        return getAverage(new float[]{c.blue,c.green,c.red});
    }

    private float getAverage(float[] numbers){
        float sum = 0;
        for (float f:numbers)
            sum += f;
        return sum/numbers.length;
    }
    private int getMax(int[] numbers){
        int max = Integer.MIN_VALUE;
        for (int i:numbers)
            if(i > max)
                max = i;
        return max;
    }
    private int getMin(int[] numbers){
        int min = Integer.MAX_VALUE;
        for (int i:numbers)
            if(i < min)
                min = i;
        return min;
    }

}
