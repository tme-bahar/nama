package ir.bahonar.nama;

import androidx.annotation.NonNull;

class Pixel{
    public COLORS color = COLORS.ELSE;
    public int x = -1;
    public int y = -1;

    public Pixel(COLORS color, int x , int y){
        this.color = color;
        this.x = x;
        this.y = y;
    }
    public Pixel(){}
    @NonNull
    public String toString(){
        return "color : "+color+"\tx : "+x + "\ty : "+y;
    }
}