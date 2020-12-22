package com.antoine.view;

import com.antoine.contracts.RenderedEntity;
import com.antoine.contracts.RenderedMotionEntity;
import com.antoine.helpers.ImageReader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RenderedDecorKey extends RenderedDecor {

    private BufferedImage key;
    private int key_id;
    RenderedDecorKey(int id, int key_id)
    {
        super( id );
        this.key_id = key_id;
    }

    public void loadKeyImg( String filename )
    {
        this.key = ImageReader.readImage( filename );
    }

    @Override
    protected void drawEntities(Graphics g, RenderedEntity[] entities, boolean inTransition )
    {
        super.drawEntities( g, entities, inTransition );
        RenderedMotionEntity player = (RenderedMotionEntity) entities[0];
        if ( ! player.hasKey( key_id )) {
            // render key
        }
    }
}
