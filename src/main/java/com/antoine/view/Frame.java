package com.antoine.view;

import com.antoine.contracts.Controler;
import com.antoine.contracts.Decor;
import com.antoine.contracts.Direction;
import com.antoine.contracts.View;
import com.antoine.events.PlayerGrabKeyEvent;
import com.antoine.helpers.JsonHelper;
import com.antoine.sound.Jukebox;
import com.antoine.sound.SoundEffect;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Frame extends JFrame implements View
{
    private Jukebox         jukebox;
    private Controler       controler;
    private SoundEffect     click_sound = new SoundEffect( "/musics/zemeckis_sound_higher.wav", 1f, this::onSoundend );

    private GamePane        gamePane;
    private InventoryPane   inventoryPane;

    private final MenuPane  menuPane;
    private double          dim_coef;
    private Dimension       game_pane_dim;
    private boolean         in_game = false;

    public Frame( Controler controler )
    {
        super("Fairy_Mickey" );

        this.jukebox        = new Jukebox();

        JSONObject conf = JsonHelper.strToJson( "/jsons/conf.json");
        this.dim_coef   = conf.getDouble( "dim_coef" );
        Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println( "Dimension: width: " + screen_size.width + "  ; height: " + screen_size.height );
        //this.dim_coef   = adaptCoefToScreenSize( screen_size );
        int width = (int) ( conf.getInt( "width" ) * this.dim_coef );
        int height = (int) ( conf.getInt( "height" ) * this.dim_coef );
        this.game_pane_dim = new Dimension( width, height );
        int xDelta = (screen_size.width - width) / 2;
        int yDelta = (screen_size.height - height) / 2;
        this.initControler( controler );

        Container container = this.getContentPane();

        container.setLayout( new BorderLayout() );

        this.menuPane = new MenuPane( this::onNewGame, width, height, this.dim_coef );

        container.add( menuPane, BorderLayout.CENTER );

        this.addKeyListener( new InternKeyListener() );
        //this.fullScreen();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.requestFocus();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private double adaptCoefToScreenSize( Dimension screen_size )
    {
        final long small = 1024 * 780;
        final long size  = screen_size.width * screen_size.height;

        return size > small ? 2 : 3;
    }

    private void quitFullScreen() {
        System.exit( 0 );
    }

    private void fullScreen()
    {
        this.setExtendedState( JFrame.MAXIMIZED_BOTH );
        this.setUndecorated( true );
    }

    private void initControler( Controler controler ) {
        this.controler = controler;
        this.controler.addView( this );
    }

    public void onSoundend() {
        this.jukebox.start();

        this.gamePane       = new GamePane( this.dim_coef );
        this.inventoryPane  = new InventoryPane( this.game_pane_dim.width, this.dim_coef );
        Container container = this.getContentPane();

        container.add( this.gamePane, BorderLayout.CENTER );
        container.add( this.inventoryPane, BorderLayout.SOUTH );

        controler.setPlayerWidth( this.gamePane.getPlayerWidth() );
        controler.setPlayerHeight( this.gamePane.getPlayerHeight() );

        this.pack();
        this.controler.start();
        this.requestFocusInWindow();
        this.revalidate();
        this.repaint();
        in_game = true;
    }

    public void onNewGame()
    {
        this.getContentPane().setBackground( Color.BLACK );
        Container container = this.getContentPane();
        container.remove( this.menuPane );
        container.getLayout().removeLayoutComponent( this.menuPane );
        this.repaint();

        this.click_sound.play();
    }

    @Override
    public void render( Decor decor )
    {
        this.gamePane.render( decor );
    }

    @Override
    public void loadDecor(int nextDecorId) {
        this.gamePane.load( nextDecorId );
    }

    @Override
    public void onPlayerGrabKey(PlayerGrabKeyEvent event) {

    }

    @Override
    public void onEndGame( Controler controler ) {
        in_game = false;
        this.jukebox.stop();
        this.initControler( controler );
        Container container = this.getContentPane();
        if ( this.gamePane != null || this.inventoryPane != null ) {
            container.remove( this.gamePane );
            container.remove( this.inventoryPane );
            this.gamePane       = null;
            this.inventoryPane  = null;
        }
        container.add( this.menuPane );
        this.repaint();
    }

    @Override
    public void fadeOut() {
        this.gamePane.fadeOut();
    }

    @Override
    public boolean isFadeOutFinished() {
        return this.gamePane.isFadeOutFinished();
    }

    @Override
    public void fadeIn() {
        this.gamePane.fadeIn();
    }

    @Override
    public boolean isFadeInFinished() {
        return this.gamePane.isFadeInFinished();
    }

    @Override
    public void onKeyFound(int key_id) {
        this.gamePane.onKeyFound();
        this.inventoryPane.onKeyFound( key_id );
    }

    private class InternKeyListener implements KeyListener
    {
        @Override
        public void keyReleased(KeyEvent e) {
            if ( in_game ) controler.stopMotion();
        }

        @Override
        public void keyTyped(KeyEvent e) {
            if ( in_game )    controler.recordMove( getDirection( e ) );
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if ( e.getKeyCode() == KeyEvent.VK_ESCAPE )
            {
                System.out.println("escape pressed");
                quitFullScreen();
            }
            else if ( in_game) controler.recordMove( getDirection( e ) );
        }

        private Direction getDirection(KeyEvent e) {
            if ( isKeyLeft( e ) ) return Direction.LEFT;
            else if ( isKeyRight( e ) ) return Direction.RIGHT;
            else if ( isKeyUp( e ) ) return Direction.UP;
            else return Direction.BOTTOM;
        }


        private boolean isKeyLeft(KeyEvent e) {
            return e.getKeyCode() == KeyEvent.VK_LEFT;
        }

        private boolean isKeyRight(KeyEvent e) {
            return e.getKeyCode() == KeyEvent.VK_RIGHT;
        }

        private boolean isKeyUp(KeyEvent e) {
            return e.getKeyCode() == KeyEvent.VK_UP;
        }

        private boolean isKeyDown(KeyEvent e) {
            return e.getKeyCode() == KeyEvent.VK_DOWN;
        }
    }

}
