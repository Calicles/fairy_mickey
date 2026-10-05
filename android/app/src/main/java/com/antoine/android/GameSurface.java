package com.antoine.android;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.antoine.contracts.Controler;
import com.antoine.contracts.Direction;
import com.antoine.game.GameEngine;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

/**
 * Surface du jeu : porte la boucle (un thread dédié), le menu et les contrôles.
 * Le moteur n'est manipulé que depuis le thread de la boucle ; le thread principal
 * lui transmet ses demandes par la file {@link #tasks}.
 */
final class GameSurface extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private static final String TAG = "FairyMickey";
    private static final long FRAME_NANOS = 1_000_000_000L / GameEngine.FPS_TARGET;

    private enum State { MENU, LAUNCHING, PLAYING }

    private final Renderer renderer;
    private final Music    music;
    private final DPad     dpad = new DPad();
    private final ConcurrentLinkedQueue< Runnable > tasks = new ConcurrentLinkedQueue<>();
    /** flèches d'un clavier physique enfoncées, la dernière en fin de liste */
    private final Deque< Direction > keys = new ArrayDeque<>( 4 );

    private volatile State   state = State.MENU;
    private volatile boolean surfaceReady;
    private volatile boolean resumed;
    private volatile boolean looping;
    private volatile float   scale = 1, offsetX, offsetY;
    private Thread thread;

    // utilisés uniquement par le thread de la boucle
    private AndroidGame game;
    private Direction   applied;

    GameSurface( Context context )
    {
        super( context );
        getHolder().addCallback( this );
        setFocusable( true );
        setFocusableInTouchMode( true );
        renderer = new Renderer( this );
        music    = new Music();
    }

    // ------------------------------------------------------------------ cycle de vie

    void resume()
    {
        resumed = true;
        music.resume();
        startLoopIfReady();
    }

    void pause()
    {
        resumed = false;
        stopLoop();
        music.pause();
    }

    void release()
    {
        stopLoop();
        music.release();
    }

    @Override
    public void surfaceCreated( SurfaceHolder holder )
    {
        surfaceReady = true;
        startLoopIfReady();
    }

    @Override
    public void surfaceChanged( SurfaceHolder holder, int format, int width, int height )
    {
        scale   = Math.min( (float) width / Renderer.WORLD_WIDTH, (float) height / Renderer.SCREEN_HEIGHT );
        offsetX = ( width  - Renderer.WORLD_WIDTH   * scale ) / 2f;
        offsetY = ( height - Renderer.SCREEN_HEIGHT * scale ) / 2f;
        dpad.layout( width, height, offsetX );
    }

    @Override
    public void surfaceDestroyed( SurfaceHolder holder )
    {
        surfaceReady = false;
        stopLoop();
    }

    private synchronized void startLoopIfReady()
    {
        if ( ! surfaceReady || ! resumed || thread != null ) return;
        looping = true;
        thread  = new Thread( this, "game-loop" );
        thread.start();
    }

    private void stopLoop()
    {
        Thread t;
        synchronized ( this ) {
            looping = false;
            t = thread;
            thread = null;
        }
        if ( t == null || t == Thread.currentThread() ) return;
        try {
            t.join( 1000 );
        } catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
        }
    }

    // ------------------------------------------------------------------ boucle

    @Override
    public void run()
    {
        while ( looping ) {
            long start = System.nanoTime();
            try {
                Runnable task;
                while ( ( task = tasks.poll() ) != null ) task.run();

                if ( state == State.PLAYING && game != null && game.isLooping() ) {
                    applyInput();
                    game.tick();
                } else {
                    paintFrame( renderer::drawMenu );
                }
            } catch ( RuntimeException e ) {
                Log.e( TAG, "erreur dans la boucle de jeu", e );
                throw e;
            }
            long wait = ( FRAME_NANOS - ( System.nanoTime() - start )) / 1_000_000L;
            try {
                Thread.sleep( Math.max( 2, wait ));
            } catch ( InterruptedException e ) {
                return;
            }
        }
    }

    private void startGame()
    {
        renderer.reset();
        game = new AndroidGame();
        game.addView( renderer );
        game.setPlayerWidth( renderer.playerWidth() );
        game.setPlayerHeight( renderer.playerHeight() );
        applied = null;
        game.start();
        music.start();
        state = State.PLAYING;
    }

    /** appelé par le moteur en fin de partie : retour au menu */
    void onEndGame( Controler next )
    {
        music.stop();
        game  = null;
        state = State.MENU;
    }

    /** transmet au moteur la direction tenue (croix tactile ou clavier), comme la répétition d'une touche */
    private void applyInput()
    {
        Direction d = dpad.current();
        if ( d == null ) d = currentKey();
        if ( d == null ) {
            if ( applied != null ) game.stopMotion();
        } else {
            if ( d != applied ) game.stopMotion();
            game.recordMove( d );
        }
        applied = d;
    }

    // ------------------------------------------------------------------ dessin

    void paintFrame( Consumer< Canvas > painter )
    {
        if ( ! surfaceReady ) return;
        SurfaceHolder holder = getHolder();
        Canvas canvas = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? holder.lockHardwareCanvas()
                : holder.lockCanvas();
        if ( canvas == null ) return;
        try {
            painter.accept( canvas );
        } finally {
            holder.unlockCanvasAndPost( canvas );
        }
    }

    /** passe du repère de l'écran à celui du jeu (700 x 500) ; à refermer par canvas.restore() */
    void applyWorldTransform( Canvas canvas )
    {
        canvas.save();
        canvas.translate( offsetX, offsetY );
        canvas.scale( scale, scale );
    }

    void drawControls( Canvas canvas )
    {
        dpad.draw( canvas );
    }

    // ------------------------------------------------------------------ entrées

    @Override
    public boolean onTouchEvent( MotionEvent event )
    {
        if ( state == State.MENU ) {
            if ( event.getActionMasked() == MotionEvent.ACTION_UP ) launch();
        } else {
            dpad.onTouch( event );
        }
        return true;
    }

    private void launch()
    {
        if ( state != State.MENU ) return;
        state = State.LAUNCHING;
        music.playClick( () -> tasks.add( this::startGame ));
    }

    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )
    {
        Direction d = keyDirection( keyCode );
        if ( d != null ) {
            synchronized ( keys ) {
                keys.remove( d );
                keys.addLast( d );
            }
            return true;
        }
        if ( state == State.MENU && ( keyCode == KeyEvent.KEYCODE_ENTER
                || keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_SPACE )) {
            launch();
            return true;
        }
        return super.onKeyDown( keyCode, event );
    }

    @Override
    public boolean onKeyUp( int keyCode, KeyEvent event )
    {
        Direction d = keyDirection( keyCode );
        if ( d != null ) {
            synchronized ( keys ) {
                keys.remove( d );
            }
            return true;
        }
        return super.onKeyUp( keyCode, event );
    }

    private Direction currentKey()
    {
        synchronized ( keys ) {
            return keys.peekLast();
        }
    }

    private static Direction keyDirection( int keyCode )
    {
        switch ( keyCode ) {
            case KeyEvent.KEYCODE_DPAD_UP:    return Direction.UP;
            case KeyEvent.KEYCODE_DPAD_DOWN:  return Direction.BOTTOM;
            case KeyEvent.KEYCODE_DPAD_LEFT:  return Direction.LEFT;
            case KeyEvent.KEYCODE_DPAD_RIGHT: return Direction.RIGHT;
            default:                          return null;
        }
    }
}
