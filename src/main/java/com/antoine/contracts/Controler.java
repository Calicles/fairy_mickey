package com.antoine.contracts;

public interface Controler {

    void start();

    void addView( View view );

    void recordMove(Direction translate);

    void stopMotion();
}
