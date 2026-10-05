package com.antoine.contracts;

public interface Decor {

    double getDistanceRatio();

    RenderedEntity[] getEntities();

    int getNextDecorId();
    int getId();

    void setPlayerLastDirection( Direction direction );
    void setEntringPlayerCoordinates();
    void setEntringPlayerCoordinates( int previous_decor_id );
    void update();

    boolean isEnding();

    void setStarted();

    boolean isStarting();

    void setPlayerDirection( Direction direction );

}
