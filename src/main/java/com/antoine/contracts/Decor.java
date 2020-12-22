package com.antoine.contracts;

import com.antoine.model.Point;
import com.antoine.model.Shape;

import java.util.ArrayList;

public interface Decor {

    double getDistanceRatio();

    RenderedEntity[] getEntities();

    ArrayList<Point>[] getExit();
    ArrayList< Point >[] getLines();
    Shape getCollision();
    int getNextDecorId();

    void setPlayerLastDirection( Direction direction );
    void setEntringPlayerCoordinates();
    void update();

    boolean isEnding();

    void setStarted();

    boolean isStarting();
}
