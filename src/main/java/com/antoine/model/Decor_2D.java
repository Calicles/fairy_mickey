package com.antoine.model;

import com.antoine.contracts.Direction;
import com.antoine.contracts.View;
import com.antoine.helpers.Direction_helper;
import com.antoine.physics.ColliderChecker;

import java.util.ArrayList;
import java.util.HashMap;

public class Decor_2D extends Abstract_Decor
{
    private ArrayList< Shape > obstacle_mask;
    private Shape collision;

    public Decor_2D( int _id )
    {
        super( _id );
        obstacle_mask = new ArrayList<>(5);
    }

    @Override
    public Shape getCollision() { return this.collision; }

    @Override
    public void update() {
        if ( ! starting && ! ending ) {
            DecorExit exit = ColliderChecker.isPlayerExited( this.player, this.exit_boxes );
            if ( exit != null ) {
                this.next_decor_id = exit.getNext_decor_id();
                this.ending = true;
                super.last_player_dir = player.getDirection();
            } else {
                if ( this.player.isMoving() )
                {
                    this.collision = ColliderChecker.seekCollide( this.player, this.obstacle_mask );
                    this.player.translate();
                }
            }
        }
    }
    //TODO REMOVE
    @Override
    public ArrayList< Point >[] getLines()
    {
        ArrayList< Point >[] boxes = new ArrayList[ this.obstacle_mask.size() ];

        for ( int i = 0; i < boxes.length; i++ )
        {
            boxes[i] = this.obstacle_mask.get(i).getPoints();
        }
        return boxes;
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
