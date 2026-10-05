package com.antoine.physics;

import com.antoine.model.Point;

public class AABB {

    private Point   coor;
    private int     width;
    private int     height;

    public AABB( int _x, int _y, int _width, int _height )
    {
        this.coor   = new Point( _x, _y );
        this.width  = _width;
        this.height = _height;
    }

    public void translate( int dx, int dy ) {
        this.coor.translate( dx, dy );
    }

    public int getMinX()
    {
        return this.coor.getX();
    }

    public int getMinY()
    {
        return this.coor.getY();
    }

    public int getMaxX()
    {
        return this.coor.getX() + width;
    }

    public int getMaxY()
    {
        return this.coor.getY() + height;
    }
}
