package com.antoine.model;

import com.antoine.physics.ColliderChecker;
import java.util.ArrayList;

public class Decor_1D extends Abstract_Decor
{
    protected ArrayList< Point > line_mask;

    protected Point entring_odd_Player_pos;
    protected Point entring_even_Player_pos;

    protected boolean player_come_odd_times = true;

    public Decor_1D( int _id )
    {
        super( _id);
        line_mask = new ArrayList<>(5);
    }

    @Override
    public void update() {
        if ( player.isMoving() ) {
            ColliderChecker.adaptVectorToLine( player, line_mask );
        }
    }

    public void setEntringPlayerPos( int _x, int _y )
    {
        entring_odd_Player_pos = new Point( _x, _y );
    }

    public void setComeBackPlayerPos( int _x, int _y )
    {
        entring_even_Player_pos = new Point( _x, _y );
    }

    public void addPointToMask( int x, int y )
    {
        line_mask.add( new Point( x, y ));
    }

    @Override
    public void setEntringPlayerCoordinates()
    {
        if ( this.player_come_odd_times )
        {
            this.player.setPosition( this.entring_odd_Player_pos );
            this.player.setDirection( this.getPlayerReentrantDir() );
        }
        else
        {
            this.player.setPosition( this.entring_even_Player_pos );
            this.player.setDirection( this.getPlayerReentrantDir() );
        }
        this.player_come_odd_times = ! this.player_come_odd_times;
    }
}
