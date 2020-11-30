package com.antoine.model;

public class Point {
    private int x;
    private int y;

    public Point( int _x, int _y )
    {
        this.x = _x;
        this.y = _y;
    }

    public void translate( int dx, int dy )
    {
        x += dx;
        y += dy;
    }

    public void setPosition( int _x, int _y )
    {
        this.x = _x;
        this.y = _y;
    }

    public void setPosition( Point p ){
        this.x = p.x;
        this.y = p.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString()
    {
        StringBuffer buff = new StringBuffer();
        buff.append( "x: " + x );
        buff.append(", y: " + y );

        return buff.toString();
    }
}
