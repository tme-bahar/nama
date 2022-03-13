package ir.bahonar.nama;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Area {

    public List<Pixel> points ;
    public SIGN sign = SIGN.ELSE;
    public Pixel top;
    public Pixel right;
    public Pixel left;
    public Pixel bottom;
    public int height = 0;
    public int width = 0;
    public int base = -1;
    public String message = "";
    public Area(List<Pixel> points,Bitmap bm,boolean complete){
        this.points = points;
        message = sign.toString();
        if(!checkSize())
            return;
        setPoints();
        setHeightWidth();
        message = sign.toString()+"1";
        if(!checkRadius() && complete)
            return;
        setBase(bm);
        message = sign.toString()+"2";
        if(complete)
        {
            if((bm.getHeight() - base) > 10)
                if(base-bottom.y < height)
                    return;
            if(base < bm.getHeight()*0.8 )
                return;
        };
        message = sign.toString()+"3";

        int hw = height * width;
/*
        Log.e("size",points.size()+"");
        Log.e("H",hw+"");
        Log.e("/",((double)hw/(double) points.size())+"");
*/
        /*
            Log.e("sign",bm.getHeight()+"");
            Log.e("size",points.size()+"");
            Log.e("blacks",getBlackCount(bm)+"");
            Log.e("cube",height*width+"");
            */
        /*
            Log.e("Density",blackDensity+" s");
            Log.e("blacks",blackLines(bm,(int)((left.x)+((width*0.4))),
                    ((int)((top.y)+(height*0.3))),((int)((top.y)+(height*0.7))))+"");

            Log.e("max blacks",getMaxBlacks(bm,1,
                    (int)((left.x)+((width*0.35))),(int)((left.x)+((width*0.45))),
                    ((int)((top.y)+(height*0.3))),((int)((top.y)+(height*0.7))))+"");
            */
        Log.e("hw",hw+"f");
        message = sign.toString()+"( hw:" + hw + " ,points: "+points.size() + " )";
        if(((double)hw/(double) points.size()) > 3)
        {
/*
            int leftBlackLines = getMoodBlacks(bm,1,
                    (int)((left.x)+((width*0.35))),(int)((left.x)+((width*0.4))),
                    (int)((top.y)+(height*0.3)),(int)((top.y)+(height*0.7)));

            int rightBlackLines = getMoodBlacks(bm,1,
                    (int)((left.x)+((width*0.6))),(int)((left.x)+((width*0.65))),
                    ((int)((top.y)+(height*0.3))),((int)((top.y)+(height*0.7))));
*/
            int leftOne  = getMoodOneBlacks(bm,1,
                    (int)((left.x)+((width*0.35))),(int)((left.x)+((width*0.45))),
                    (int)((top.y)+(height*0.3)),(int)((top.y)+(height*0.7)));

            int  rightOne = getMoodOneBlacks(bm,1,
                    (int)((left.x)+((width*0.6))),(int)((left.x)+((width*0.65))),
                    ((int)((top.y)+(height*0.3))),((int)((top.y)+(height*0.7))));

            message = sign.toString() + "\nE0 (left : "  + " , right : "
                    + " ) ( l:"+leftOne+" , r:"+rightOne+")";

            if(leftOne == 3)
            {
                if(rightOne == 3)
                {
                    sign = SIGN.B2;
                }else if(rightOne < 3)
                {
                    sign = SIGN.B1;
                }
                message = sign.toString() + "\nE3 (left : " + " ) ( l:"+leftOne+" , r:"+rightOne+")";
            }else if(leftOne == 2)
            {
                if(rightOne == 3)
                {
                    sign = SIGN.A2;
                }else if(rightOne < 3)
                {
                    sign = SIGN.A1;
                }
                message = sign.toString() + "\nE3 (left : " + " ) ( l:"+leftOne+" , r:"+rightOne+")";
            }else{
                int leftBlack = getBlackCount(bm,(int)((left.x)+(width*0.2)),(int)((left.x)+(width*0.3))
                        ,(int)((top.y)+(height*0.35)),(int)((top.y)+(height*0.55)));

                int rightBlack = getBlackCount(bm,(int)((right.x)-(width*0.3)),(int)((right.x)-(width*0.2))
                        ,(int)((top.y)+(height*0.35)),(int)((top.y)+(height*0.55)));

                int centerBlack = getBlackCount(bm,(int)((right.x)-(width*0.55)),(int)((right.x)-(width*0.45))
                        ,(int)((top.y)+(height*0.35)),(int)((top.y)+(height*0.55)));

                message = sign.toString() + "\nE3 (left : " +leftBlack + " , right : "+rightBlack+" , center :"+centerBlack
                        + " ) ( l:"+leftOne+" , r:"+rightOne+")";
                if((rightBlack + leftBlack) < (centerBlack*1.2))
                    sign = SIGN.STRIGHT;
                else if(rightOne == 1 && leftOne == 1)
                {

                    if(isBlack(bm.getPixel(left.x+(width/2),(int)(bottom.y-(height*0.1)))))
                        sign = SIGN.STRIGHT;
                    else{
                        leftBlack = getBlackCount(bm,(int)((left.x)+(width*0.3)),(int)((left.x)+(width*0.4))
                                ,(int)((top.y)+(height*0.35)),(int)((top.y)+(height*0.55)));

                        rightBlack = getBlackCount(bm,(int)((right.x)-(width*0.4)),(int)((right.x)-(width*0.3))
                                ,(int)((top.y)+(height*0.35)),(int)((top.y)+(height*0.55)));

                        sign = leftBlack > rightBlack ? SIGN.LEFT : SIGN.RIGHT;
                    }

                }
                message = sign.toString() + "\nE1 (left : " +leftBlack + " , right : "+rightBlack+" , center :"+centerBlack
                        + " ) ( l:"+leftOne+" , r:"+rightOne+")";
            }

        }
        else{
            sign = SIGN.RED;

            message = sign.toString();
        }
/*
        Log.e("top x",top.x+"");
        Log.e("top y",top.y+"");
        Log.e("right x",right.x+"");
        Log.e("right y",right.y+"");
        Log.e("left x",left.x+"");
        Log.e("left y",left.y+"");
        Log.e("bottom y",bottom.y+"");
        Log.e("bottom x",bottom.x+"");
        Log.e("height",height+"");
        Log.e("width",width+"");

        Log.e("cube",height*width+"");
        Log.e("all",points.size()+"");
*/
    }

    private int getMoodBlacks(Bitmap bm,double step,int minX,int maxX,int minY,int maxY){
        int[] all = new int[6];
        for (int i = minX; i < maxX ; i+=step) {
            int num  = blackLines(bm, i, minY, maxY);
            if (num > 4)
                all[5]++;
            else
                all[num]++;
        }
        all[0] = 0;
        int max = Math.max(Math.max(all[0],Math.max(all[1],all[2])),Math.max(all[3],Math.max(all[4],all[5])));
        for (int i = 0; i < 6; i++)
            if(max == all[i])
                return i;
        return max;
    }
    private int getMoodOneBlacks(Bitmap bm,double step,int minX,int maxX,int minY,int maxY){
        int[] all = new int[6];
        for (int i = minX; i < maxX ; i+=step) {
            int num  = blackOneLines(bm, i, minY, maxY);
            if (num > 4)
                all[5]++;
            else
                all[num]++;
        }
        all[0] = 0;
        int max = Math.max(Math.max(all[0],Math.max(all[1],all[2])),Math.max(all[3],Math.max(all[4],all[5])));
        for (int i = 0; i < 6; i++)
            if(max == all[i])
                return i;
        return max;
    }
    private int blackOneLines(Bitmap bm,int x,int minY,int maxY){
        int result = 0;
        boolean inLine = false;
        for(int i = minY; i < maxY;i += 2)
            if(inLine)
            {
                if(!isBlack(bm.getPixel(x,i)))
                    inLine = false;
                else
                    changeBlacks(bm,x,i);
            }
            else {
                if(isBlack(bm.getPixel(x,i))) {
                    result++;
                    inLine = true;
                }
            }
        return result;
    }

    private int blackLines(Bitmap bm,int x,int minY,int maxY){
        int result = 0;
        boolean inLine = false;
        for(int i = minY; i < maxY;i += 2)
            if(inLine)
            {
                if(!isBlackPoint(x,i,bm))
                    inLine = false;
            }
            else {
                if(isBlackPoint(x,i,bm)) {
                    result++;
                    inLine = true;
                }
            }
            return result;
    }

    private int getBlackCount(Bitmap bm){
        return getBlackCount(bm,left.x,right.x,top.y,bottom.y);
    }
    private int getBlackCount(Bitmap bm,int minX,int maxX,int minY,int maxY){
        int result = 0;
        for (int i = minX; i < maxX; i++)
            for(int j = minY;j < maxY ; j++)
                if (isBlack(bm.getPixel(i,j)))
                    result++;
        return result;
    }

    private void setBase(Bitmap bm){
        Pixel p = new Pixel(COLORS.DARKBLUE,bottom.x,bottom.y+3);
        while (p.color == COLORS.DARKBLUE){
            try {
                p = new Pixel(isDarkBlue(bm.getPixel(p.x, p.y + 3)) ? COLORS.DARKBLUE : COLORS.ELSE, p.x, p.y + 3);
            }catch (Exception e){
                break;
            }
        }
        base = p.y;
    }

    private boolean checkRadius(){
        return height > width * 0.8;
    }
    private void setHeightWidth(){
        height = bottom.y - top.y;
        width = right.x - left.x;
    }
    private boolean checkSize(){
        return points.size() > 60;
    }

    private void setPoints(){
        getBottom();
        getTop();
        getLeft();
        getRight();
    }
    private void getBottom(){
        Pixel bottom = new Pixel(null,0,0);
        for (Pixel p: points)
            if(p.y > bottom.y)
                bottom = p;
        this.bottom = bottom;
    }
    private void getTop(){
        Pixel top = new Pixel(null,Integer.MAX_VALUE,Integer.MAX_VALUE);
        for (Pixel p: points)
            if(p.y < top.y)
                top = p;
        this.top = top;
    }
    private void getLeft(){
        Pixel left = new Pixel(null,Integer.MAX_VALUE,Integer.MAX_VALUE);
        for (Pixel p: points)
            if(p.x < left.x)
                left = p;
        this.left = left;
    }
    private void getRight(){
        Pixel right = new Pixel(null,0,0);
        for (Pixel p: points)
            if(p.x > right.x)
                right = p;
        this.right = right;
    }
    private boolean isDarkBlue(int pixel){
        Col c = new Col(pixel);
        return (c.red < 50) && (c.green < 50);
    }
    private boolean isBlack(int pixel){
        Col c = new Col(pixel);
        int res = Math.max(c.red,Math.max(c.blue,c.green)) - Math.min(c.red,Math.min(c.blue,c.green));
        return (c.red < 170) && (c.green < 170) && (c.blue < 170) && res < 35;
    }
    private boolean areBlack(int ... pixels){
        boolean result = true;
        for (int p:pixels)
            result &= isBlack(p);
        return result;
    }
    private boolean isBlackPoint(int x,int y,Bitmap bm){
        if(areBlack(bm.getPixel(x-1,y-1),bm.getPixel(x-1,y),bm.getPixel(x-1,y+1),
                bm.getPixel(x,y-1),bm.getPixel(x,y),bm.getPixel(x,y+1),
                bm.getPixel(x+1,y-1),bm.getPixel(x+1,y),bm.getPixel(x+1,y+1)))
        {
            //changeBlacks(bm,x,y);
            return true;
        }
        return false;
    }
    private void changeBlacks(Bitmap bm,int x, int y){
        bm.setPixel(x,y, Color.parseColor("#ffff00"));
        /*
        bm.setPixel(x-1,y-1, Color.parseColor("#ffff00"));
        bm.setPixel(x-1,y, Color.parseColor("#ffff00"));
        bm.setPixel(x,y+1, Color.parseColor("#ffff00"));
        bm.setPixel(x,y-1, Color.parseColor("#ffff00"));
        bm.setPixel(x,y+1, Color.parseColor("#ffff00"));
        bm.setPixel(x-1,y-1, Color.parseColor("#ffff00"));
        bm.setPixel(x-1,y, Color.parseColor("#ffff00"));
        bm.setPixel(x-1,y+1, Color.parseColor("#ffff00"));
         */
    }

}
