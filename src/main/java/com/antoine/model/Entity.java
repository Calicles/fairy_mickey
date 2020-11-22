package com.antoine.model;

import com.antoine.contracts.RenderedEntity;

public class Entity implements RenderedEntity {
    protected Point coordinates;
    protected int width;
    protected int height;

    public Entity( int _x, int _y )
    {
        this.coordinates = new Point( _x, _y );
    }

    public void setSize( int _width, int _height )
    {
        this.width  = _width;
        this.height = _height;
    }

    public void translate( int dx, int dy )
    {
        coordinates.translate( dx, dy );
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void setWidth( int width ) { this.width = width; }
    public void setHeight( int height ) { this.height = height; }

    @Override
    public int getX() {
        return coordinates.getX();
    }

    @Override
    public int getY() {
        return coordinates.getY();
    }
}
