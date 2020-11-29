package com.antoine.view;

import com.antoine.contracts.Direction;
import com.antoine.contracts.RenderedEntity;
import com.antoine.contracts.RenderedMotionEntity;
import com.antoine.helpers.ImageReader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class RenderedDecor {

    protected int id;
    protected BufferedImage background_img;
    protected BufferedImage front_img;


    RenderedDecor( int id )
    {
        this.id = id;
    }

    public int getWidth() { return background_img.getWidth(); }
    public int getHeight() { return background_img.getHeight(); }

    public void loadImgs( String bg_img_name, String f_img_name )
    {
        background_img  = ImageReader.readImage( bg_img_name );
        front_img       = ImageReader.readImage( f_img_name );
    }

    public void renderBackGround(Graphics g)
    {
        g.drawImage(this.background_img, 0, 0, 1000, 800, null );
    }

    public void renderFront(Graphics g)
    {
        g.drawImage( front_img, 1000, 800, null );
    }

    public void drawEntities(Graphics g, RenderedEntity[] entities) {
        RenderedMotionEntity player = (RenderedMotionEntity) entities[0];

    }
}
