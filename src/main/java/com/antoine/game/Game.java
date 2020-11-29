package com.antoine.game;

import com.antoine.contracts.*;
import com.antoine.events.DecorChangeEvent;
import com.antoine.model.Player;
import com.antoine.sound.Jukebox;

public class Game implements DecorListener, Controler {

    private boolean running = true;
    private Player player;
    private Decor decor;
    private Jukebox jukebox;
    private View view;
    private Thread gameLoopThread;

    public Game()
    {
        this.player = new Player( 0, 0, 4 );
        DecorFactory.setPlayer( this.player );

        gameLoopThread = new Thread( this::run );
    }
    public Game( Game game ) {
        this.player = game.player;
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
        this.decor = DecorFactory.getDecor( 1 );

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
        this.view.onEndGame( new Game( this ) );
    }

    @Override
    public void setPlayerWidth( int playerWidth )
    {
        this.player.setWidth( playerWidth );
    }

    @Override
    public void setPlayerHeight( int playerHeight )
    {
        this.player.setHeight( playerHeight );
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
