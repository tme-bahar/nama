package ir.bahonar.nama;

public class Col {
    public int blue;
    public int green;
    public int red;
    public Col(String ARGB){
            String blue = String.valueOf(ARGB.charAt(6)) + ARGB.charAt(7);
            String green = String.valueOf(ARGB.charAt(4)) + ARGB.charAt(5);
            String red = String.valueOf(ARGB.charAt(2)) + ARGB.charAt(3);
            this.blue  = (int) Long.parseLong(blue,16);
            this.green  = (int) Long.parseLong(green,16);
            this.red = (int) Long.parseLong(red,16);
    }
    public Col(int IntARGB){
        this(Integer.toHexString(IntARGB));
    }
}
