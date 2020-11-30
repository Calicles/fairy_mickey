package com.antoine.model;

import com.antoine.contracts.*;
import com.antoine.events.DecorChangeEvent;
import com.antoine.helpers.Direction_helper;

import java.awt.*;
import java.util.ArrayList;

public abstract class Abstract_Decor implements Decor {
    protected int id;
    protected int next_decor_id;
    protected static Player player;
    protected Direction last_player_dir;
    protected double player_distance_ratio;
    protected double player_distance_transform_speed;

    protected ArrayList< DecorExit > exit_boxes;

    protected boolean starting = true;
    protected boolean ending   = false;

    static protected DecorListener dListener;

    public abstract void update();
    public abstract void setEntringPlayerCoordinates();

    protected Abstract_Decor( int _id )
    {
        this.id = _id;
        this.exit_boxes = new ArrayList<>(2);
    }
    protected Abstract_Decor( int _id, Player _p )
    {
        this( _id );
        this.player = _p;
    }

    @Override
    public Shape getCollision() { return null;}

    public static void setPlayer( Player _player )
    {
        Abstract_Decor.player = _player;
    }

    public void setPlayerLastDirection( Direction direction )
    {
        this.last_player_dir = direction;
    }

    //TODO REMOVE
    @Override
    public ArrayList< Point >[] getExit()
    {
        ArrayList< Point >[] exits = new ArrayList[ this.exit_boxes.size() ];
        for ( int i = 0; i < exit_boxes.size(); i++ )
        {
            exits[ i ] = exit_boxes.get( i ).getPoints();
        }
        return exits;
    }
    //TODO REMOVE
    @Override
    public ArrayList< Point >[] getLines()
    {
        return null;
    }


    @Override
    public RenderedEntity[] getEntities(){
        RenderedEntity[] tab = new RenderedEntity[1];
        tab[0] = player;
        return tab;
    }

    public double getDistanceRatio() { return this.player_distance_ratio; }

    public void addListener( DecorListener listener ) {
        dListener = listener;
    }

    protected void fireDecorEnded()
    {
        DecorChangeEvent event = new DecorChangeEvent( this.next_decor_id );
        dListener.onDecorEnded( event );
    }

    public void addExit(int next_decor_id ) {
        this.exit_boxes.add( new DecorExit( next_decor_id ) );
    }

    public void addPointToCurrentExit( int x, int y )
    {
        this.exit_boxes
                .get( this.exit_boxes.size() - 1 )
                .addPoint( new Point( x, y ));
    }

    public void setPlayer_distance_ratio(int ratio ) {
        this.player_distance_ratio = ratio;
    }

    public void setPlayer_distance_transform_speed(double player_distance_transform_speed) {
        this.player_distance_transform_speed = player_distance_transform_speed;
    }


    Direction getPlayerReentrantDir()
    {
        return Direction_helper.getInversDir( this.last_player_dir );
    }
}
