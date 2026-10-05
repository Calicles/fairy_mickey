package com.antoine.model;

import com.antoine.contracts.Direction;
import com.antoine.physics.ColliderChecker;

import java.util.ArrayList;

public class Decor_2D extends Abstract_Decor
{
    private ArrayList< Shape > obstacle_mask;

    public Decor_2D( int _id )
    {
        super( _id );
        obstacle_mask = new ArrayList<>(5);
    }

    @Override
    protected void constrainPlayerMove() {
        ColliderChecker.seekCollide( player, this.obstacle_mask );
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

    @Override
    public void setPlayerDirection(Direction direction) {
        player.Move( direction );
    }
}
