package com.antoine.model;

import com.antoine.contracts.Direction;
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

    // TODO REMOVE
    @Override
    public ArrayList< Point >[] getLines()
    {
        ArrayList< Point >[] line = new ArrayList[ 1 ];
        line[0] = new ArrayList<>( this.line_mask );
        return line;
    }

    @Override
    public void update() {
        // TODO remove
        this.player.translate();
        if ( player.isMoving() ) {
            //ColliderChecker.adaptVectorToLine( player, line_mask );
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
        }
        else
        {
            this.player.setPosition( this.entring_even_Player_pos );
        }
        this.player.setDirection( this.getPlayerReentrantDir() );
        this.player_come_odd_times = ! this.player_come_odd_times;
    }

    @Override
    public void setPlayerDirection(Direction direction) {
        player.Move( direction );
    }
}
