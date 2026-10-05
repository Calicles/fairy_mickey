package com.antoine.game;

import com.antoine.contracts.*;
import com.antoine.model.Abstract_Decor;
import com.antoine.model.Player;

/**
 * Logique du jeu indépendante de l'interface : chaque plateforme (Swing, Android)
 * fournit sa boucle en appelant {@link #tick()} environ 60 fois par seconde.
 */
public abstract class GameEngine implements Controler, KeyFoundListener {

    public static final int FPS_TARGET = 60;

    private boolean running = true;
    protected final Player player;
    private Decor   decor;
    protected View  view;

    protected GameEngine( double dimCoef )
    {
        this.player = new Player( 0, 0, 1 );
        this.player.setDirection( Direction.UP );
        DecorFactory.clear();
        DecorFactory.setDimCoef( dimCoef );
        DecorFactory.setPlayer( this.player );
    }

    /** nouvelle partie reprenant le joueur (déjà mis à l'échelle) et la vue de la précédente */
    protected GameEngine( GameEngine previous )
    {
        this.player = previous.player;
        this.view   = previous.view;
    }

    /** démarre la boucle qui appelle tick() */
    protected abstract void startLoop();

    /** arrête la boucle */
    protected abstract void stopLoop();

    /** contrôleur à rendre à la vue quand la partie est finie */
    protected abstract Controler createNextGame();

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

    /** une image de jeu : mise à jour du modèle puis rendu */
    public void tick()
    {
        if ( ! running ) {
            this.stopLoop();
            this.view.onEndGame( createNextGame() );
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
        this.startLoop();
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
