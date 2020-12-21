package com.antoine.view;

import com.antoine.contracts.Direction;
import com.antoine.contracts.RenderedEntity;
import com.antoine.contracts.RenderedMotionEntity;
import com.antoine.helpers.Direction_helper;
import com.antoine.helpers.ImageReader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class RenderedDecor {

    protected int id;
    protected BufferedImage background_img;
    protected BufferedImage front_img;
    protected int width;
    protected int height;

    protected static double g_dim_coef;
    protected static HashMap<Direction, ArrayList<BufferedImage>> player_imgs;



    RenderedDecor( int id )
    {
        this.id = id;
    }

    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }
    public static void setDim_Coef( double _dim_coef ) { g_dim_coef = _dim_coef; }
    public static void setPlayerImgs( HashMap< Direction, ArrayList< BufferedImage >> _player_imgs)
    {
        player_imgs = _player_imgs;
    }
    public static int getPlayerWidth() { return (int) ( player_imgs.get( Direction.BOTTOM ).get( 0 ).getWidth() * g_dim_coef ); }
    public static int getPlayerHeight() { return (int) ( player_imgs.get( Direction.BOTTOM ).get( 0 ).getHeight() * g_dim_coef ); }

    public void loadImgs( String bg_img_name, String f_img_name )
    {
        background_img  = ImageReader.readImage( bg_img_name );
        front_img       = ImageReader.readImage( f_img_name );
        this.width      = (int) ( this.background_img.getWidth() * g_dim_coef );
        this.height     = (int) ( this.background_img.getHeight() * g_dim_coef );
    }

    public void render( Graphics g, RenderedEntity[] entities )
    {
        this.renderBackGround( g );
        this.drawEntities( g, entities );
        this.renderFront( g );
    }

    protected void renderBackGround(Graphics g)
    {
        this.drawDecorImg( g, this.background_img );
    }

    protected void renderFront(Graphics g)
    {
        this.drawDecorImg( g, this.front_img );
    }

    protected void drawDecorImg( Graphics g, BufferedImage img )
    {
        g.drawImage(
                img,
                0,
                0,
                this.width,
                this.height,
                null
        );
    }

    protected void drawEntities(Graphics g, RenderedEntity[] entities) {
        if ( entities != null ) {
            RenderedMotionEntity player = (RenderedMotionEntity) entities[0];
            g.drawImage(
                    player_imgs.get( player.getDirection() ).get( 0 ),
                    player.getX(),
                    player.getY(),
                    player.getWidth(),
                    player.getHeight(),
                    null
            );
        }
    }
}
