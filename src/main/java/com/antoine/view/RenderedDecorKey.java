package com.antoine.view;

import com.antoine.contracts.RenderedEntity;
import com.antoine.contracts.RenderedMotionEntity;
import com.antoine.helpers.ImageReader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class RenderedDecorKey extends RenderedDecor {

    private ArrayList< BufferedImage > key;
    private int key_sprite_index = 0;
    private int key_anim_slower  = 0;
    private boolean anim_loop_forward = true;
    private final int KEY_ANIM_SLOWER_MAX = 15;

    RenderedDecorKey(int id)
    {
        super( id );
        key = new ArrayList<>( 4 );
    }

    public void loadKeyImg( String filename )
    {
        this.key.add( ImageReader.readImage( filename ));
    }

    @Override
    protected void drawEntities(Graphics g, RenderedEntity[] entities, boolean inTransition )
    {
        super.drawEntities( g, entities, inTransition );
        if ( entities[ 1 ] != null ) { // if key not grabbed
            ++key_anim_slower;
            if ( key_anim_slower % KEY_ANIM_SLOWER_MAX == 0 ) {
                key_anim_slower = 0;
                if ( anim_loop_forward ) {
                    ++key_sprite_index;
                    if ( key_sprite_index == key.size() - 1){
                        anim_loop_forward = false;
                    }
                } else {
                    --key_sprite_index;
                    if ( key_sprite_index == 0 ) {
                        anim_loop_forward = true;
                    }
                }
                System.out.println(key_sprite_index);
            }
           g.drawImage( key.get(key_sprite_index), entities[1].getX(), entities[1].getY(), entities[1].getWidth(), entities[1].getHeight(), null );
        }
    }
}
