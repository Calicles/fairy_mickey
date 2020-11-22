package com.antoine.model;

import com.antoine.contracts.Direction;
import com.antoine.contracts.View;
import com.antoine.helpers.Direction_helper;

import java.util.ArrayList;
import java.util.HashMap;

public class Decor_2D extends Abstract_Decor
{
    private ArrayList< Shape > obstacle_mask;
    private HashMap< Direction, Point > player_reentring_pos;

    public Decor_2D( int _id )
    {
        super( _id );
        obstacle_mask = new ArrayList<>(5);
        player_reentring_pos = new HashMap<>(3);
    }

    @Override
    public void update() {

    }

    public void createShape()
    {
        obstacle_mask.add( new Shape() );
    }

    public void addPointToCurrentShape( int _x, int _y )
    {
        obstacle_mask
                .get( obstacle_mask.size() - 1 )
                .addPoint( new Point( _x, _y));
    }

    public void addPlayerEntrance( String dir_name, int _x, int _y )
    {
        Direction direction = Direction_helper.strToDir( dir_name );
        int x, y;
        if ( direction == Direction.UP ) {
            x = _x;
            y = _y - player.getHeight();
        } else if ( direction == Direction.BOTTOM) {
            x = _x;
            y = _y - player.getHeight();
        } else if ( direction == Direction.RIGHT ) {
            x = _x;
            y = _y;
        } else if ( direction == Direction.LEFT ) {
            x = _x - player.getWidth();
            y = _y;
        } else {
            throw new IllegalArgumentException("dir_name error" );
        }
        // add the association
        this.player_reentring_pos.put( direction, new Point( x, y ));
    }

    @Override
    void setEntringPlayerCoordinates() {
        this.player.setDirection( this.getPlayerReentrantDir() );
        this.player.setPosition( player_reentring_pos.get( this.last_player_dir ));
    }
}
