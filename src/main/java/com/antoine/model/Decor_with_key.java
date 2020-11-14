package com.antoine.model;

import com.antoine.contracts.RenderedEntity;
import com.antoine.contracts.View;

public class Decor_with_key extends Decor_1D
{
    Entity key;


    public Decor_with_key( int _id )
    {
        super( _id );
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

    }
}
