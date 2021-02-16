package com.antoine.model;

import com.antoine.contracts.Decor_key;
import com.antoine.contracts.RenderedEntity;
import com.antoine.contracts.View;
import com.antoine.physics.AABB;
import com.antoine.physics.ColliderChecker;

public class Decor_with_key extends Decor_1D implements Decor_key
{
    private Entity key;
    private int key_id;


    public Decor_with_key( int _id )
    {
        super( _id );
    }

    public void setKeyId( int key_id )
    {
        this.key_id = key_id;
    }

    public void setKeyCoordinates( int x, int y ) {
        this.key = new Entity( x, y );
    }

    public void setKeyDimension( int width, int height ) {
        this.key.setSize( width, height );
    }

    @Override
    public RenderedEntity[] getEntities()
    {
        RenderedEntity[] tab = new RenderedEntity[2];
        tab[0] = player;
        tab[1] = key;
        return tab;
    }

    @Override
    protected void checkOtherCollides() {
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
