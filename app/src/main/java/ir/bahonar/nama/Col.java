package ir.bahonar.nama;

import android.util.Log;

public class Col {
    public boolean isZero = false;
    public int blue;
    public int green;
    public int red;
    public Col(String ARGB){
        if(ARGB.equals("0"))
            isZero = true;
        else
            try {
                String blue = String.valueOf(ARGB.charAt(6)) + ARGB.charAt(7);
                String green = String.valueOf(ARGB.charAt(4)) + ARGB.charAt(5);
                String red = String.valueOf(ARGB.charAt(2)) + ARGB.charAt(3);
                this.blue = (int) Long.parseLong(blue, 16);
                this.green = (int) Long.parseLong(green, 16);
                this.red = (int) Long.parseLong(red, 16);
            }catch (Exception e){
                Log.e("ColException for "+ARGB,e.toString());
            }
    }
    public Col(int IntARGB){
        this(Integer.toHexString(IntARGB));
    }
}
