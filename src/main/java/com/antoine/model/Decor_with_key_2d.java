package com.antoine.model;

import com.antoine.contracts.Decor_key;
import com.antoine.contracts.RenderedEntity;

public class Decor_with_key_2d extends Decor_2D implements Decor_key
{
    private final DecorKey key = new DecorKey();

    public Decor_with_key_2d( int _id )
    {
        super( _id );
    }

    @Override
    public void setKeyId( int key_id ) { key.setKeyId( key_id ); }

    @Override
    public void setKeyCoordinates( int x, int y ) { key.setKeyCoordinates( x, y ); }

    @Override
    public void setKeyDimension( int width, int height ) { key.setKeyDimension( width, height ); }

    @Override
    public RenderedEntity[] getEntities() { return key.getEntities( player ); }

    @Override
    protected void checkOtherCollides() { key.checkGrab( player, kListener ); }
}
