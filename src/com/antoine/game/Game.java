package com.antoine.game;

import com.antoine.contracts.Decor;
import com.antoine.contracts.DecorListener;
import com.antoine.model.Player;

public class Game implements DecorListener {

    private boolean running;
    private Player player;
    private Decor decor;
    //private Jukebox jukebox;
    private Thread gameLoopThread;

    Game()
    {
        gameLoopThread = new Thread( this::run );
        gameLoopThread.start();
    }

    private void update()
    {

    }

    private void render()
    {

    }

    private void sleep( long sleep )
    {
        try {
            Thread.sleep( sleep );
        } catch ( InterruptedException ie ) {}
    }

    void run()
    {
        // Time gestioner
        final long FPS_TARGET = 60;
        final long OPTIMAL_TIME = 1000000000 / FPS_TARGET;

        long update_time;
        long wait;
        long now;
        // ===========

        while ( running ) {
            now = System.nanoTime();

            this.update();
            this.render();

            update_time = System.nanoTime() - now;
            wait = (OPTIMAL_TIME - update_time) / 1000000;

            sleep( wait );
        }
    }
}
