package com.antoine.view;

import com.antoine.contracts.Decor;
import com.antoine.contracts.Direction;
import com.antoine.contracts.RenderedEntity;
import com.antoine.helpers.Direction_helper;
import com.antoine.helpers.ImageReader;
import com.antoine.helpers.JsonHelper;
import com.antoine.model.Shape;
import com.antoine.physics.ColliderChecker;
import org.json.JSONArray;
import org.json.JSONObject;

//TODO rmove
import com.antoine.model.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class GamePane extends JPanel
{
    private RenderedDecor rDecor;
    private RenderedEntity[] entities;
    private final String FILE_NAME = "";
    private int player_sprite_index;
    private int anim_slower;
    private double dim_coef;
    private final int ANIM_SLOWER_MAX = 4;

    // TODO remode
    private ArrayList< Point >[] boxes;
    private ArrayList< Point >[] solids;
    private Shape collision;


    GamePane( double _dim_coef )
    {
        super( true );
        this.dim_coef = _dim_coef;
        RenderedDecor.setDim_Coef( this.dim_coef );
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
     * @param g graphics chargé de l'affichage.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.rDecor.render( g, this.entities );

        Color oldColor = g.getColor();

        g.setColor( Color.BLACK );

        for ( int i = 0; i < boxes.length; i++ )
        {
            for ( int j = 0; j < boxes[i].size(); j++ )
            {
                int x2, y2;
                if ( j == boxes[i].size() - 1 ){
                    x2 = boxes[i].get(0).getX();
                    y2 = boxes[i].get(0).getY();
                } else
                {
                    x2 = boxes[i].get( j+1).getX();
                    y2 = boxes[i].get(j+1).getY();
                }
                g.drawLine( boxes[i].get(j).getX(), boxes[i].get(j).getY(), x2, y2);
            }
        }
        g.setColor( Color.CYAN );
        for ( int i = 0; i < solids.length; i++ )
        {
            for ( int j = 0; j < solids[i].size(); j++ )
            {
                int x2, y2;
                if ( j == solids[i].size() - 1 ){
                    x2 = solids[i].get(0).getX();
                    y2 = solids[i].get(0).getY();
                } else
                {
                    x2 = solids[i].get( j+1).getX();
                    y2 = solids[i].get(j+1).getY();
                }
                g.drawLine( solids[i].get(j).getX(), solids[i].get(j).getY(), x2, y2);
            }
        }

        g.setColor( Color.MAGENTA );

        if ( ColliderChecker.collide != null )
        {
            Point p1 = ColliderChecker.collide.p1;
            Point p2 = ColliderChecker.collide.p2;
                g.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                RenderedEntity p = (RenderedEntity) entities[0];
                g.drawLine( p.getX(), p.getY() + p.getHeight() - 10, p.getX() + p.getWidth(), p.getY() + p.getHeight() - 10);
        }

        g.setColor( oldColor );
        g.dispose();
    }

    public void render( Decor decor )
    {
        this.entities = decor.getEntities();
        // TODO REMOVE TWICE
        this.boxes = decor.getExit();
        this.solids = decor.getLines();
        this.collision = decor.getCollision();
        revalidate();
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
                                path_prefix + images.getString( i )));
            }
            player_imgs.put( dir, list );
        }

        RenderedDecor.setPlayerImgs( player_imgs );
    }
}
