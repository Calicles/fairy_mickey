package com.antoine.game;

import com.antoine.contracts.*;
import com.antoine.model.Abstract_Decor;
import com.antoine.model.Player;

import javax.swing.*;

public class Game implements Controler, KeyFoundListener {

    private static final int FPS_TARGET = 60;

    private boolean running = true;
    private final   Player player;
    private Decor   decor;
    private View    view;
    /** la boucle de jeu tourne sur l'EDT, comme le rendu et les événements clavier */
    private final Timer gameLoop;

    public Game()
    {
        this.player = new Player( 0, 0, 1 );
        this.player.setDirection( Direction.UP );
        DecorFactory.setPlayer( this.player );

        gameLoop = new Timer( 1000 / FPS_TARGET, e -> this.tick() );
        gameLoop.setCoalesce( true );
    }

    public Game( Game game ) {
        this.player = game.player;
        this.view   = game.view;
        gameLoop = new Timer( 1000 / FPS_TARGET, e -> this.tick() );
        gameLoop.setCoalesce( true );
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

    private void init()
    {
        this.decor = DecorFactory.getDecor( 1 );
        this.decor.setEntringPlayerCoordinates();
        Abstract_Decor.setKeyFoundListener( this );
    }

    private void tick()
    {
        if ( ! running ) {
            this.gameLoop.stop();
            this.view.onEndGame( new Game( this ) );
            return;
        }
        this.update();
        this.render();
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
        int previousDecorId = this.decor.getId();
        this.decor = DecorFactory.getDecor( nextDecorId );
        this.decor.setEntringPlayerCoordinates( previousDecorId );
        this.view.loadDecor( nextDecorId );
    }

    @Override
    public void start() {
        this.init();
        this.gameLoop.start();
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

    @Override
    public void onKeyFound(int key_id) {
        this.view.onKeyFound( key_id );
    }
}
