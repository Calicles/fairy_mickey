package com.antoine.game;

import com.antoine.contracts.Decor;
import com.antoine.contracts.DecorListener;
import com.antoine.contracts.View;
import com.antoine.events.DecorChangeEvent;
import com.antoine.model.Player;

public class Game implements DecorListener {

    private boolean running;
    private Player player;
    private Decor decor;
    //private Jukebox jukebox;
    private View view;
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
        // recuperer list de string/quotien
        // set player rate and position direction
        // set if key position
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

    @Override
    public void onDecorEnded(DecorChangeEvent event) {
        this.decor = DecorFactory.getDecor( event.getNextDecorId() );
        this.view.loadDecor( event.getNextDecorId() );
    }
}
