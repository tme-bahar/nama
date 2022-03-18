package ir.bahonar.nama;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.CalendarContract;

public class WayDetection {

    public String message = "";
    public String stu = "str";
    public Way way = Way.STR;
    public WayDetection(Bitmap bm){

        int top = findTop(bm);
        int left1 = findLeft(bm,(int)(bm.getHeight()*setting.bottomLineY));
        int right1 = findRight(bm,(int)(bm.getHeight()*setting.bottomLineY));

        int left2 = bm.getWidth()/2;
        int right2 = bm.getWidth()/2;
        if(top > setting.maxTop * bm.getHeight()){
            left2 = findLeft(bm,(int)(bm.getHeight()*setting.topLineY));
            right2 = findRight(bm,(int)(bm.getHeight()*setting.topLineY));
        }

        setWay(bm,getRegion(bm,left2,true)
                ,getRegion(bm,right2,true)
                ,getRegion(bm,left1,false)
                ,getRegion(bm,right1,false)
                ,top);
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
        message = way.toString() + "\ntop:"+top +" \n bottom:("+left1+","+right1+") \n top:("+left2+","+right2+")";

    }

    private void setWay(Bitmap bm,int topLeft,int topRight,int bottomLeft,int bottomRight,int mid){
        int topType = getType(topLeft,topRight);
        int bottomType = getType(bottomLeft,bottomRight);
        //
        boolean top = mid > setting.maxTop * bm.getHeight() ;
        if(topType == 2 && bottomType == 2 && top)
            way = Way.CC;
        else if(topType == 2 && bottomType == 2 && !top)
            way = Way.CRL;
        else if((topType == 1 || topType == 0 ) && (bottomType == 1 || bottomType == 0 ) && top)
            way = Way.CL;
        else if((topType == 5 || topType == 8 ) && (bottomType == 5 || bottomType == 8 ) && top)
            way = Way.CR;
        else if((topType == 1 || topType == 0 ) && (bottomType == 1 || bottomType == 0 ) && !top)
            way = Way.TL;
        else if((topType == 5 || topType == 8 ) && (bottomType == 5 || bottomType == 8 ) && !top)
            way = Way.TR;
        else if(topType == 7 && bottomType == 7 && top)
            way = Way.KR;
        else if(topType ==3 && bottomType == 3 && top)
            way = Way.KL;
        else
            way = Way.STR;
        message = stu + "\ntop:"+top +" \n bottom:("+bottomType+") \n top:("+topType+")";
    }

    private int getType(int left,int right){
        int k = left%5;
        int t = right%5;
        int result = -1;
        result = left*3;
        result += (right*3)+1;
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
        return (c.red < 170) && (c.green < 170) && (c.blue < 170) && res < 35;
    }

    private void changeColor(Bitmap bm,int x, int y){
        bm.setPixel(x,y, Color.BLUE);
    }

    enum Way {KR,KL,TR,TL,CR,CL,CC,STR,CRL }
}
