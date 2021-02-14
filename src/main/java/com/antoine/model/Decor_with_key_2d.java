package com.antoine.model;

import com.antoine.contracts.Decor_key;
import com.antoine.contracts.RenderedEntity;
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
        {
            ArrayList< Shape > tmp = new ArrayList<>();
            Shape s = new Shape();
            tmp.add( s );
            s.addPoint( key.getX(), key.getY() );
            if ( ColliderChecker.seekCollide( player, tmp ) != null )
            {
                System.out.println("collides with key");

                key = null;
            }
        }
    }

}
