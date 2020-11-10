package com.antoine.game;

import com.antoine.contracts.*;
import com.antoine.events.DecorChangeEvent;
import com.antoine.model.Player;

public class Game implements DecorListener, Controler {

    private boolean running;
    private Player player;
    private Decor decor;
    //private Jukebox jukebox;
    private View view;
    private Thread gameLoopThread;

    public Game()
    {
        gameLoopThread = new Thread( this::run );
    }



    private void update()
    {
        decor.update();
    }

    private void render()
    {
        this.view.render( this.decor );
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
        this.view.onEndGame( new Game() );
    }

    @Override
    public void onDecorEnded(DecorChangeEvent event) {
        this.decor = DecorFactory.getDecor( event.getNextDecorId() );
        this.view.loadDecor( event.getNextDecorId() );
    }

    @Override
    public void start() {
        this.gameLoopThread.start();
    }

    @Override
    public void addView(View view) {
        this.view = view;
    }

    @Override
    public void recordMove(Direction translate) {
        player.Move( translate );
    }

    @Override
    public void stopMotion() {
        player.resetDelta();
    }
}
