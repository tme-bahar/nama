package ir.bahonar.nama;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SignDetection {
    private Bitmap bm ;
    public SIGN sign = SIGN.ELSE;
    public Area area;
    public SignDetection(Bitmap bm,boolean complete){
        this.bm = bm;
        List<Area> areas = getAreas(bm,complete);
        if(areas.size() != 0) {
            area = findBiggest(areas);
            Log.e("areas", areas.size() + "");
            Log.e("biggest", area.points.size() + "");
            Log.e("height", area.height + "");
                sign = area.sign;
        }
    }
    public Bitmap replace(){
        if(area != null && area.points!=null && area.points.size() != 0)
            for (Pixel p : area.points)
                bm.setPixel(p.x,p.y, Color.parseColor("#000000"));
        return bm;
    }
    private Area findBiggest(List<Area> areas){
        Area biggest = areas.get(0);
        for (Area area:areas)
            if(area.points.size() > biggest.points.size())
                biggest = area;
        return biggest;
    }
    private boolean[][] getReds(Bitmap bm){
        boolean[][] result = new boolean[bm.getWidth()][bm.getHeight()];
        try {
            for(int i = 0; i < bm.getWidth();i++)
                for(int j = 0; j < bm.getHeight(); j++)
                {
                    Col c = new Col(bm.getPixel(i,j));
                    result[i][j] = (c.red > 150) &&
                            (c.blue-c.green < 50) && (c.blue-c.green > -50)
                            && c.blue < 130 && c.green < 130;
                }
        }catch (Exception e){}

        return result;
    }
    private List<Area> getAreas(Bitmap bm ,boolean complete){
        List<Area> result = new ArrayList<>();
        for(int i = 0; i < bm.getWidth();i++)
            for(int j = 0; j < bm.getHeight(); j++)
                if(isRed(bm.getPixel(i,j)))
                {
                    List<Pixel> area = new ArrayList<>();
                    area.add(new Pixel(COLORS.RED,i,j));
                    bm.setPixel(i,j,Color.parseColor("#00ff00"));
                    getArea(bm,area,i,j);
                    result.add(new Area(area,bm,complete));
                }
        return result;
    }
    private void getArea(Bitmap bm,List<Pixel> area,int x,int y){
        int nextX = 0;
        int nextY = 0;
            if ((x!=0) && (y!=0) && isRed(bm.getPixel(x-1,y-1))) {
                nextX = -1;
                nextY = -1;
                area.add(new Pixel(COLORS.RED, x + nextX, y + nextY));
                getArea(bm, area, x + nextX, y + nextY);
            }
            if ((x!=0) && isRed(bm.getPixel(x-1,y))) {
                nextX = -1;
                nextY = 0;
                area.add(new Pixel(COLORS.RED, x + nextX, y + nextY));
                bm.setPixel(x + nextX,y + nextY,Color.parseColor("#00ff00"));
                getArea(bm, area, x + nextX, y + nextY);
            }
            if ((x!=0) && (y+1 != bm.getHeight()) && isRed(bm.getPixel(x-1,y+1))) {
                nextX = -1;
                nextY = 1;
                area.add(new Pixel(COLORS.RED, x + nextX, y + nextY));
                bm.setPixel(x + nextX,y + nextY,Color.parseColor("#00ff00"));
                getArea(bm, area, x + nextX, y + nextY);
            }
            if ((y!=0) && isRed(bm.getPixel(x,y-1))) {
                nextX = 0;
                nextY = -1;
                area.add(new Pixel(COLORS.RED, x + nextX, y + nextY));
                bm.setPixel(x + nextX,y + nextY,Color.parseColor("#00ff00"));
                getArea(bm, area, x + nextX, y + nextY);
            }
            if ((y+1 != bm.getHeight()) && isRed(bm.getPixel(x,y+1))) {
                nextX = 0;
                nextY = 1;
                area.add(new Pixel(COLORS.RED, x + nextX, y + nextY));
                bm.setPixel(x + nextX,y + nextY,Color.parseColor("#00ff00"));
                getArea(bm, area, x + nextX, y + nextY);
            }
            if ((y!=0) && (x+1 != bm.getWidth()) && isRed(bm.getPixel(x+1,y-1))) {
                nextX = 1;
                nextY = -1;
                area.add(new Pixel(COLORS.RED, x + nextX, y + nextY));
                bm.setPixel(x + nextX,y + nextY,Color.parseColor("#00ff00"));
                getArea(bm, area, x + nextX, y + nextY);
            }
            if ((x+1 != bm.getWidth()) && isRed(bm.getPixel(x+1,y))) {
                nextX = 1;
                nextY = 0;
                area.add(new Pixel(COLORS.RED, x + nextX, y + nextY));
                bm.setPixel(x + nextX,y + nextY,Color.parseColor("#00ff00"));
                getArea(bm, area, x + nextX, y + nextY);
            }
            if ((x+1 != bm.getWidth())&& (y+1 != bm.getHeight()) &&isRed(bm.getPixel(x+1,y+1))) {
                nextX = 1;
                nextY = 1;
                area.add(new Pixel(COLORS.RED, x + nextX, y + nextY));
                bm.setPixel(x + nextX,y + nextY,Color.parseColor("#00ff00"));
                getArea(bm, area, x + nextX, y + nextY);
            }
    }
    void log(boolean[][] array){
        StringBuilder sb = new StringBuilder('\n');
        for(int i = 0; i < array.length;i++) {
            for (int j = 0; j < array[i].length; j++)
                sb.append(array[i][j] ? 'T' : 'F');
            sb.append('\n');
        }
        Log.e("array",sb.toString());
    }
    private Boolean[][] toObjectiveArray(final boolean[][] bool) {
        Boolean[][] result = new Boolean[bool.length][bool[0].length];
        for (int i = 0 ;i < bool.length; i ++)
            for (int j = 0 ;j < bool[i].length; j ++)
                result[i][j] = bool[i][j];
        return result;
    }
    private boolean isRed(int pixel){
        Col c = new Col(pixel);
        return (c.red > 150) &&
                (c.blue-c.green < 50) && (c.blue-c.green > -50)
                && c.blue < 130 && c.green < 130;
    }
}
enum SIGN {STRIGHT,RIGHT,LEFT,TOP,RED,ELSE,B1,B2,A1,A2}
