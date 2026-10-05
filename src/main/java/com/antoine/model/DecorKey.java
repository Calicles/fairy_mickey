package com.antoine.model;

import com.antoine.contracts.Decor_key;
import com.antoine.contracts.KeyFoundListener;
import com.antoine.contracts.RenderedEntity;
import com.antoine.physics.AABB;
import com.antoine.physics.ColliderChecker;

/**
 * Clé posée dans un décor : partagée par les décors 1D et 2D qui en contiennent une.
 */
public class DecorKey implements Decor_key
{
    private Entity key;
    private int key_id;

    @Override
    public void setKeyId( int key_id ) { this.key_id = key_id; }

    @Override
    public void setKeyCoordinates( int x, int y ) { this.key = new Entity( x, y ); }

    @Override
    public void setKeyDimension( int width, int height ) { this.key.setSize( width, height ); }

    /**
     * @return le joueur et la clé, ou null à la place de la clé si elle a été ramassée
     */
    public RenderedEntity[] getEntities( Player player )
    {
        return new RenderedEntity[]{ player, key };
    }

    /**
     * Prévient le listener et retire la clé si le joueur la touche.
     */
    public void checkGrab( Player player, KeyFoundListener listener )
    {
        if ( key == null ) return; // clé déjà ramassée
        AABB aabb = new AABB( key.getX(), key.getY(), key.getWidth(), key.getHeight() );
        if ( ColliderChecker.hasKey( player, aabb ))
        {
            listener.onKeyFound( key_id );
            this.key = null;
        }
    }
}
