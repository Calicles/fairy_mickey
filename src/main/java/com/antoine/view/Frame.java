package com.antoine.view;

import com.antoine.contracts.Controler;
import com.antoine.contracts.Decor;
import com.antoine.contracts.Direction;
import com.antoine.contracts.View;
import com.antoine.events.PlayerGrabKeyEvent;
import com.antoine.helpers.JsonHelper;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Frame extends JFrame implements View
{
    private GamePane        gamePane;
    private InventoryPane   inventoryPane;
    private final MenuPane  menuPane;
    private Controler       controler;

    public Frame( Controler controler )
    {
        super("Fairy_Mickey" );

        JSONObject conf = JsonHelper.strToJson( "/jsons/conf.json");

        this.initControler( controler );

        Container container = this.getContentPane();

        container.setLayout( new BorderLayout() );

        this.menuPane = new MenuPane( this::onNewGame, conf.getInt( "width" ), conf.getInt( "height" ));
        container.add( menuPane, BorderLayout.CENTER );

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener( new InternKeyListener() );
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initControler( Controler controler ) {
        this.controler = controler;
        this.controler.addView( this );
    }

    public void onNewGame()
    {
        Container container = this.getContentPane();

        container.remove( this.menuPane );
        container.getLayout().removeLayoutComponent( this.menuPane );

        this.gamePane       = new GamePane();
        this.inventoryPane  = new InventoryPane();

        container.add( this.gamePane, BorderLayout.CENTER );
        //container.add( this.inventoryPane );

        controler.setPlayerWidth( this.gamePane.getPlayerWidth() );
        controler.setPlayerHeight( this.gamePane.getPlayerHeight() );

        this.controler.start();
        revalidate();
        this.repaint();
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

    private class InternKeyListener implements KeyListener {


        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            controler.recordMove( translate( e ) );
        }

        private Direction translate(KeyEvent e) {
            if ( isKeyLeft( e ) ) return Direction.LEFT;
            else if ( isKeyRight( e ) ) return Direction.RIGHT;
            else if ( isKeyUp( e ) ) return Direction.UP;
            else return Direction.BOTTOM;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            controler.stopMotion();
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
