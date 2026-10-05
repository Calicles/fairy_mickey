package com.antoine.android;

import com.antoine.contracts.Controler;
import com.antoine.game.GameEngine;

/**
 * Moteur du jeu piloté par la boucle de {@link GameSurface}.
 * Le coefficient vaut 1 : le monde garde les coordonnées du json et c'est le rendu qui s'adapte à l'écran.
 */
final class AndroidGame extends GameEngine {

    private volatile boolean looping;

    AndroidGame()
    {
        super( 1.0 );
    }

    private AndroidGame( AndroidGame previous )
    {
        super( previous );
    }

    boolean isLooping() { return looping; }

    @Override
    protected void startLoop() { looping = true; }

    @Override
    protected void stopLoop() { looping = false; }

    @Override
    protected Controler createNextGame() { return new AndroidGame( this ); }
}
