package ir.bahonar.nama;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CentralColorDetection {

    @SuppressLint("ClickableViewAccessibility")
    public CentralColorDetection(ImageView im, Bitmap bm, View v, TextView tv){

        final double WidthScale = (double) bm.getWidth()/1000d;
        final double HeightScale = (double) bm.getHeight()/1000d;
        Log.e("width", "," + WidthScale);
        Log.e("hei", "," + HeightScale);

        COLORS[][] matrix = makeMatrix(Tools.getResizedBitmap(bm,60,75));
        Pixel top = getTop(matrix);
        Pixel left = getLeft(matrix);
        Pixel right = getRight(matrix);
        Pixel bottom = getBottom(matrix);
        Pixel topLeft = new Pixel(matrix[(top.x+left.x)/2][(top.y+left.y)/2],(top.x+left.x)/2,(top.y+left.y)/2);
        Pixel topRight = new Pixel(matrix[(top.x+right.x)/2][(top.y+right.y)/2],(top.x+right.x)/2,(top.y+right.y)/2);
        Pixel bottomLeft = new Pixel(matrix[(bottom.x+left.x)/2][(bottom.y+left.y)/2],(bottom.x+left.x)/2,(bottom.y+left.y)/2);
        Pixel bottomRight = new Pixel(matrix[(bottom.x+right.x)/2][(bottom.y+right.y)/2],(bottom.x+right.x)/2,(bottom.y+right.y)/2);


        Log.e("left",left.toString());
        Log.e("Right",right.toString());
        Log.e("Top",top.toString());
        Log.e("Bottom",bottom.toString());
        Log.e("topLeft",topLeft.toString());
        Log.e("topRight",topRight.toString());
        Log.e("bottomLeft",bottomLeft.toString());
        Log.e("bottomRight",bottomRight.toString());

        im.setOnTouchListener((view, motionEvent) -> {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            int scaledX = (int)(x*WidthScale);
            int scaledY = (int)(y*HeightScale);
            Log.e("pos", scaledX + "," + scaledY);

            int pixelColor = bm.getPixel(scaledX,scaledY);

            String cohex = Integer.toHexString(pixelColor);
            Log.e("hex",cohex);
            v.setBackgroundColor(pixelColor);
            tv.setText("#"+cohex + "\n( " +scaledX+" , "+scaledY+" )\n( "+
                    ((double)x/(double)10) + "% , " + ((double)y/(double)10) + "% )\n"+colorDetection(cohex) );


            return false;
        });
    }

    Pixel getLeft(COLORS[][] colors){
        for(int i = 0 ; i  < colors.length;i++){
            for(int j = colors[i].length-2 ; j >= 0; j--) {
                //Log.e("color "+i+" , "+j,((colors[i][j] != COLORS.ELSE) && (colors[i][j] == colors[i][j + 1]))+"");
                if ((colors[i][j] != COLORS.ELSE) && (colors[i][j] == colors[i][j + 1])){
                    //Log.e("color "+i+" , "+j,colors[i][j]+" "+colors[i][j+1]);
                    return new Pixel(colors[i][j],i,j);

                }
            }
        }
        return new Pixel();
    }

    Pixel getRight(COLORS[][] colors){
        for(int i =  colors.length-1 ; i  >= 0 ;i--){
            for(int j = 0 ; j < colors[i].length-1; j++) {
                //Log.e("color "+i+" , "+j,((colors[i][j] != COLORS.ELSE) && (colors[i][j] == colors[i][j + 1]))+"");
                if ((colors[i][j] != COLORS.ELSE) && (colors[i][j] == colors[i][j + 1])){
                    //Log.e("color "+i+" , "+j,colors[i][j]+" "+colors[i][j+1]);
                    return new Pixel(colors[i][j],i,j);

                }
            }
        }
        return new Pixel();
    }

    Pixel getTop(COLORS[][] colors){
        for(int j = 0 ; j < colors[0].length-1; j++) {
            for(int i =  0 ; i  < colors.length-1 ;i++){
                //Log.e("color "+i+" , "+j,((colors[i][j] != COLORS.ELSE) && (colors[i][j] == colors[i][j + 1]))+"");
                if ((colors[i][j] != COLORS.ELSE) && (colors[i][j] == colors[i+1][j])){
                    //Log.e("color "+i+" , "+j,colors[i][j]+" "+colors[i][j+1]);
                    return new Pixel(colors[i][j],i,j);

                }
            }
        }
        return new Pixel();
    }

    Pixel getBottom(COLORS[][] colors){
        for(int j = colors[0].length-1 ; j >= 0; j--) {
            for(int i =  colors.length-1 ; i  >= 0 ;i--){
                //Log.e("color "+i+" , "+j,((colors[i][j] != COLORS.ELSE) && (colors[i][j] == colors[i][j + 1]))+"");
                if ((colors[i][j] != COLORS.ELSE) && (colors[i][j] == colors[i+1][j])){
                    //Log.e("color "+i+" , "+j,colors[i][j]+" "+colors[i][j+1]);
                    return new Pixel(colors[i][j],i,j);

                }
            }
        }
        return new Pixel();
    }



    COLORS[][] makeMatrix(Bitmap bitmap){
        COLORS[][] result = new COLORS[bitmap.getWidth()][bitmap.getHeight()];
        for (int i = 0 ; i < bitmap.getWidth();i++)
            for (int j = 0 ; j < bitmap.getHeight();j++)
            {
                int pixelColor = bitmap.getPixel(i,j);
                String cohex = Integer.toHexString(pixelColor);
                result[i][j] = colorDetection(cohex);
            }
        return result;
    }

    void print(COLORS[][] colors){
        StringBuilder sb = new StringBuilder();
        for (COLORS[] a:colors) {
            for (COLORS b:a) {
                sb.append(b.toString().charAt(0));
            }
            sb.append("\n");
        }
        Log.e("all","\n"+sb.toString());
    }

    COLORS colorDetection(String color){
        String blue = String.valueOf(color.charAt(6)) + color.charAt(7);
        String green = String.valueOf(color.charAt(4)) + color.charAt(5);
        String red = String.valueOf(color.charAt(2)) + color.charAt(3);


        long longBlue = Long.parseLong(blue,16);
        long longGreen = Long.parseLong(green,16);
        long longRed = Long.parseLong(red,16);

        Log.e("RED",longRed+"");
        Log.e("GREEN",longGreen+"");
        Log.e("Blue",longBlue+"");

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

enum COLORS{ BLUE,RED,YELLOW,GREEN,ELSE,DARKBLUE}