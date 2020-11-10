package com.antoine.contracts;

public interface Mover {

    Direction getDirection();

    int getX();

    int getY();

    int getDx();

    int getDy();

    int getWidth();

    int getHeight();

    void setVector( int dx, int dy );

    int getSpeed();
}
