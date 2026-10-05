package com.antoine.view;

import com.antoine.contracts.Controler;
import com.antoine.game.GameEngine;

import javax.swing.*;

/**
 * Version Swing : la boucle de jeu tourne sur l'EDT, comme le rendu et les événements clavier.
 */
public class SwingGame extends GameEngine {

    private final Timer gameLoop;

    public SwingGame()
    {
        super( DimensionHelper.getDimCoef() );
        gameLoop = createLoop();
    }

    private SwingGame( SwingGame game ) {
        super( game );
        gameLoop = createLoop();
    }

    private Timer createLoop()
    {
        Timer timer = new Timer( 1000 / FPS_TARGET, e -> this.tick() );
        timer.setCoalesce( true );
        return timer;
    }

    @Override
    protected void startLoop() {
        this.gameLoop.start();
    }

    @Override
    protected void stopLoop() {
        this.gameLoop.stop();
    }

    @Override
    protected Controler createNextGame() {
        return new SwingGame( this );
    }
}
