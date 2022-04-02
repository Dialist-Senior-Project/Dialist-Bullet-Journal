package com.example.dialist;

public class DB {
    public int x;
    public int y;
    public int xx;
    public int yy;
    public String value;

    public DB(){ }

    public DB(int x, int y, int xx, int yy, String value){
        this.x = x;
        this.y = y;
        this.xx = xx;
        this.yy = yy;
        this.value = value;
    }

    public int getx(){ return x; }

    public int setx(int x){ return x; }

    public int gety(){ return y; }

    public int sety(int y){ return y; }

    public int getxx(){ return xx; }

    public int setxx(int xx){ return xx; }

    public int getyy(){ return yy; }

    public int setyy(int yy){ return yy; }

    public String getvalue(){ return value; }

    public String setvalue(String value){ return value; }
}
