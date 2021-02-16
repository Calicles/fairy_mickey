package com.antoine.model;

import com.antoine.contracts.Decor_key;
import com.antoine.contracts.RenderedEntity;
import com.antoine.physics.AABB;
import com.antoine.physics.ColliderChecker;

import java.util.ArrayList;

public class Decor_with_key_2d extends Decor_2D implements Decor_key
{
    private Entity key;
    private int key_id;

    public Decor_with_key_2d(int _id) {
        super(_id);
    }

    public void setKeyId( int key_id )
    {
        this.key_id = key_id;
    }

    public void setKeyCoordinates( int x, int y ){ this.key = new Entity( x, y ); }

    public void setKeyDimension( int witdh, int height ) {this.key.setSize( witdh, height );}

    @Override
    public RenderedEntity[] getEntities()
    {
        RenderedEntity[] tab = { player, key };
        return tab;
    }

    @Override
    protected void checkOtherCollides()
    {
        if ( key != null )
        {// Key had been found
            AABB aabb = new AABB( key.getX(), key.getY(), key.getWidth(), key.getHeight() );
            if ( ColliderChecker.hasKey( player, aabb ))
            {// player collides with key
                kListener.onKeyFound( key_id );
                this.key = null;
            }
        }
    }
}
