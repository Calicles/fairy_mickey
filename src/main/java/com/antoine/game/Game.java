package com.antoine.game;

import com.antoine.contracts.*;
import com.antoine.model.Player;

public class Game implements Controler {

    private boolean running = true;
    private Player player;
    private Decor decor;
    private View view;
    private Thread gameLoopThread;

    public Game()
    {
        this.player = new Player( 0, 0, 1 );
        this.player.setDirection( Direction.UP );
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
        if ( this.decor.isEnding() ) {
            this.view.fadeOut();
            if ( this.view.isFadeOutFinished() ) {
                this.onDecorEnded( this.decor.getNextDecorId() );
            }
        }else if ( this.decor.isStarting() ) {
            this.view.fadeIn();
            if ( this.view.isFadeInFinished() ) {
                this.decor.setStarted();
            }
        }
        this.view.render( this.decor );
    }

    private void sleep( long sleep )
    {
        try {
            if ( sleep < 0 ) sleep = 0;
            Thread.sleep( sleep );
        } catch ( InterruptedException ie ) {}
    }

    void run()
    {
        this.decor = DecorFactory.getDecor( 1 );
        this.decor.setEntringPlayerCoordinates();

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

    public void onDecorEnded( int nextDecorId ) {
        this.decor = DecorFactory.getDecor( nextDecorId );
        this.decor.setEntringPlayerCoordinates();
        this.view.loadDecor( nextDecorId );
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
    public void recordMove(Direction direction) {
        this.decor.setPlayerDirection( direction );
    }

    @Override
    public void stopMotion() {
        player.resetDelta();
    }
}
