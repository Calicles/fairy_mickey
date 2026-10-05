package com.antoine.view;

import com.antoine.contracts.Controler;
import com.antoine.contracts.Decor;
import com.antoine.contracts.Direction;
import com.antoine.contracts.View;
import com.antoine.helpers.JsonHelper;
import com.antoine.sound.Jukebox;
import com.antoine.sound.SoundEffect;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayDeque;
import java.util.Deque;


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
        this.dim_coef   = DimensionHelper.getDimCoef();
        int width = (int) ( conf.getInt( "width" ) * this.dim_coef );
        int height = (int) ( conf.getInt( "height" ) * this.dim_coef );
        this.game_pane_dim = new Dimension( width, height );
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

    /** appelé par le thread audio à la fin du bruitage : la suite se fait sur l'EDT */
    public void onSoundend() {
        SwingUtilities.invokeLater( this::startGame );
    }

    private void startGame() {
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
        /** flèches actuellement enfoncées, la dernière pressée en fin de liste */
        private final Deque< Direction > pressed = new ArrayDeque<>( 4 );

        @Override
        public void keyReleased(KeyEvent e) {
            Direction direction = getDirection( e );
            if ( direction == null ) return;
            pressed.remove( direction );
            if ( ! in_game ) return;
            controler.stopMotion();
            if ( ! pressed.isEmpty() ) {
                // une autre flèche est toujours enfoncée : on reprend sa direction
                controler.recordMove( pressed.peekLast() );
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
            // getKeyCode() vaut toujours VK_UNDEFINED ici : rien à faire
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if ( e.getKeyCode() == KeyEvent.VK_ESCAPE )
            {
                quitFullScreen();
                return;
            }
            Direction direction = getDirection( e );
            if ( direction == null ) return;
            // la répétition automatique du clavier ne doit pas dupliquer la touche
            pressed.remove( direction );
            pressed.addLast( direction );
            if ( in_game ) controler.recordMove( direction );
        }

        /**
         * @return la direction associée à la flèche, ou null si la touche n'est pas une flèche
         */
        private Direction getDirection(KeyEvent e) {
            switch ( e.getKeyCode() ) {
                case KeyEvent.VK_LEFT:  return Direction.LEFT;
                case KeyEvent.VK_RIGHT: return Direction.RIGHT;
                case KeyEvent.VK_UP:    return Direction.UP;
                case KeyEvent.VK_DOWN:  return Direction.BOTTOM;
                default:                return null;
            }
        }
    }

}
