package com.antoine.contracts;

public interface Drawer {

    void drawPlayer( int x, int y, int direction, int distance_ratio );

    void drawKey( int x, int y );

    boolean fadeIn();

    boolean fadeOut();
}
