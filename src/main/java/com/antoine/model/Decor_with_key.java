package com.antoine.model;

import com.antoine.contracts.Decor_key;
import com.antoine.contracts.RenderedEntity;
import com.antoine.contracts.View;

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
    public void update()
    {
        super.update();
    }
}
