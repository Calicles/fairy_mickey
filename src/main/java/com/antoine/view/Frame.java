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
    private Jukebox jukebox;
    private Controler       controler;
    private SoundEffect     click_sound = new SoundEffect( "/musics/zemeckis_sound_higher.wav", 1f, this::onSoundend );

    private GamePane        gamePane;
    private InventoryPane   inventoryPane;

    private InternKeyListener keyListener;

    private final MenuPane  menuPane;
    private double          dim_coef;

    public Frame( Controler controler )
    {
        super("Fairy_Mickey" );

        this.jukebox = new Jukebox();
        this.keyListener = new InternKeyListener();

        JSONObject conf = JsonHelper.strToJson( "/jsons/conf.json");
        this.dim_coef = conf.getDouble( "dim_coef" );

        this.initControler( controler );

        Container container = this.getContentPane();

        container.setLayout( new BorderLayout() );

        this.menuPane = new MenuPane(
                this::onNewGame, (int) ( conf.getInt( "width" ) * dim_coef ),
                (int) ( conf.getInt( "height" ) * dim_coef ) );
        container.add( menuPane, BorderLayout.CENTER );

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initControler( Controler controler ) {
        this.controler = controler;
        this.controler.addView( this );
    }

    public void onSoundend() {
        this.jukebox.start();

        this.gamePane       = new GamePane( this.dim_coef );
        this.inventoryPane  = new InventoryPane();

        Container container = this.getContentPane();
        container.add( this.gamePane, BorderLayout.CENTER );
        //container.add( this.inventoryPane );

        controler.setPlayerWidth( this.gamePane.getPlayerWidth() );
        controler.setPlayerHeight( this.gamePane.getPlayerHeight() );

        this.controler.start();
        this.addKeyListener( this.keyListener );
        this.requestFocusInWindow();
        this.revalidate();
        this.repaint();
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

        this.removeKeyListener( this.keyListener );
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

    private class InternKeyListener implements KeyListener
    {
        @Override
        public void keyReleased(KeyEvent e) {
            controler.stopMotion();
        }

        @Override
        public void keyTyped(KeyEvent e) {
            controler.recordMove( getDirection( e ) );
        }

        @Override
        public void keyPressed(KeyEvent e) {
            controler.recordMove( getDirection( e ) );
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
