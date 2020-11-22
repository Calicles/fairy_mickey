package com.antoine.view;

import com.antoine.contracts.Decor;
import com.antoine.contracts.Direction;
import com.antoine.contracts.RenderedEntity;
import com.antoine.contracts.RenderedMotionEntity;
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
    private HashMap<Direction, ArrayList<BufferedImage>> player_imgs;
    private RenderedEntity[] entities;
    private final String FILE_NAME = "";
    private int player_sprite_index;
    private int anim_slower;
    private final int ANIM_SLOWER_MAX = 4;


    GamePane()
    {
        super( true );
        loadPlayerImages();
    }

    public int getPlayerWidth() { return player_imgs.get( Direction.LEFT).get( 0 ).getWidth(); }

    public int getPlayerHeight() { return player_imgs.get( Direction.LEFT).get( 0 ).getHeight(); }

    @Override
    public Dimension getPreferredSize() {
        return null;
    }

    /**
     * @see JPanel#paintComponent(Graphics)
     * @param g graphics chargé de l'affichage.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        rDecor.renderBackGround( g );

        // draw player and apply scale size
        RenderedMotionEntity player = (RenderedMotionEntity) this.entities[0];
        BufferedImage pl_img;
        if ( player.isMoving() ) {
            pl_img = this.player_imgs.get( player.getDirection() ).get( this.player_sprite_index );
            if ((++anim_slower % ANIM_SLOWER_MAX) == 0 ) this.player_sprite_index ++;
        }
        else {
            pl_img = this.player_imgs.get( player.getDirection() ).get( 0 );
        }

        int width = pl_img.getWidth(), height = pl_img.getHeight();
        g.drawImage( pl_img, player.getX(), player.getY(), width, height, null );

        rDecor.drawEntities( g, entities );

        rDecor.renderFront( g );

        g.dispose();
    }

    public void render( Decor decor )
    {
        this.entities = decor.getEntities();
        this.repaint();
    }

    public void load(int nextDecorId) {
        this.rDecor = RenderedDecorFactory.getRenderedDecor( nextDecorId );
    }

    private void loadPlayerImages() {
        JSONObject json = JsonHelper.strToJson( "/images/perso/perso.json" );
        String path_prefix = json.getString( "global_path" );
        JSONArray images_obj = json.getJSONArray( "paths" );
        for ( int i = 0; i < images_obj.length(); i++ ) {
            ArrayList< BufferedImage > list = new ArrayList<>(5 );
            JSONObject obj = images_obj.getJSONObject( i );
            Direction dir = Direction_helper.strToDir( obj.getString( "direction" ));
            JSONArray images = obj.getJSONArray( "images" );
            for ( int j = 0; j < images.length(); j++ ) {
                list.add(
                        ImageReader.readImage(
                                path_prefix + images.getString( i )));
            }
            this.player_imgs.put( dir, list );
        }
    }
}
