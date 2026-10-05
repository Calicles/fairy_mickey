package com.antoine.view;

import com.antoine.contracts.Decor;
import com.antoine.contracts.Direction;
import com.antoine.contracts.RenderedEntity;
import com.antoine.helpers.Direction_helper;
import com.antoine.helpers.ImageReader;
import com.antoine.helpers.JsonHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class GamePane extends JPanel
{
    private RenderedDecor rDecor;
    private RenderedEntity[] entities;

    private int         ALPHA = 0;
    private final int   ALPHA_MAX = 255;
    private final int   ALPHA_STEP = 2;
    private boolean inTransition = false;


    GamePane( double _dim_coef )
    {
        super( true );
        RenderedDecor.setDim_Coef( _dim_coef );
        this.rDecor = RenderedDecorFactory.getRenderedDecor( 1 );
        loadPlayerImages();
    }

    public int getPlayerWidth() { return RenderedDecor.getPlayerWidth(); }

    public int getPlayerHeight() { return RenderedDecor.getPlayerHeight(); }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension( this.rDecor.getWidth(), this.rDecor.getHeight() );
    }

    /**
     * @see JPanel#paintComponent(Graphics)
     * @param g1 graphics chargé de l'affichage.
     */
    @Override
    public void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = ( Graphics2D ) g1;
        this.rDecor.render( g, this.entities, inTransition );

        Color oldColor = g.getColor();
        if ( inTransition ) {
            g.setColor( new Color( 0, 0, 0, ALPHA) );
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
        g.setColor( oldColor );
    }

    public void render( Decor decor )
    {
        this.entities = decor.getEntities();
        this.inTransition = decor.isEnding() || decor.isStarting();
        this.repaint();
    }

    public void load(int nextDecorId) {
        this.rDecor = RenderedDecorFactory.getRenderedDecor( nextDecorId );
    }

    private void loadPlayerImages() {
        JSONObject json = JsonHelper.strToJson("/jsons/perso.json");
        String path_prefix = json.getString( "global_path" );
        JSONArray images_obj = json.getJSONArray( "paths" );
        HashMap< Direction, ArrayList< BufferedImage >> player_imgs = new HashMap<>( 4 );
        for ( int i = 0; i < images_obj.length(); i++ ) {
            ArrayList< BufferedImage > list = new ArrayList<>(5 );
            JSONObject obj = images_obj.getJSONObject( i );
            Direction dir = Direction_helper.strToDir( obj.getString( "direction" ));
            JSONArray images = obj.getJSONArray( "images" );
            for ( int j = 0; j < images.length(); j++ ) {
                list.add(
                        ImageReader.readImage(
                                path_prefix + images.getString( j )));
            }
            player_imgs.put( dir, list );
        }

        RenderedDecor.setPlayerImgs( player_imgs );
    }

    public void fadeOut() {
        ALPHA += ALPHA_STEP;
        if ( ALPHA > 255 ) ALPHA = ALPHA_MAX;
    }

    public boolean isFadeOutFinished() {
        return ALPHA == ALPHA_MAX;
    }

    public void fadeIn() {
        ALPHA -= ALPHA_STEP;
        if ( ALPHA < 0 ) ALPHA = 0;
    }

    public boolean isFadeInFinished() {
        return ALPHA == 0;
    }

    public void onKeyFound() {

    }
}
