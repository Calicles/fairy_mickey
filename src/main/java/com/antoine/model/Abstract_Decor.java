package com.antoine.model;

import com.antoine.contracts.*;
import com.antoine.events.DecorChangeEvent;
import com.antoine.helpers.Direction_helper;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Abstract_Decor implements Decor {
    protected int           id;
    protected int           next_decor_id;
    protected static Player player;
    protected static KeyFoundListener kListener;
    protected Direction     last_player_dir;
    protected double        player_distance_ratio;
    protected double        player_distance_transform_speed;

    protected HashMap< Direction, Point > player_reentring_pos;
    protected ArrayList< DecorExit >    exit_boxes;

    protected boolean starting = true;
    protected boolean ending   = false;

    public abstract void update();

    public abstract void setPlayerDirection( Direction direction );

    protected Abstract_Decor( int _id )
    {
        this.id = _id;
        this.exit_boxes         = new ArrayList<>(2);
        player_reentring_pos    = new HashMap<>(3);
    }
    protected Abstract_Decor( int _id, Player _p )
    {
        this( _id );
        this.player = _p;
    }

    public void setEntringPlayerCoordinates() {
        this.ending   = false;
        this.starting = true;
        player.setDirection( this.getPlayerReentrantDir() );
        player.setPosition( player_reentring_pos.get( this.last_player_dir ));
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
    public boolean isEnding() {
        return this.ending;
    }

    @Override
    public boolean isStarting() { return this.starting; }

    @Override
    public Shape getCollision() { return null;}

    public static void setPlayer( Player _player )
    {
        Abstract_Decor.player = _player;
    }

    public static void setKeyFoundListener( KeyFoundListener listener )
    {
        kListener = listener;
    }

    public void setPlayerLastDirection( Direction direction )
    {
        this.last_player_dir = direction;
    }

    public void setStarted() { this.starting = false; }

    @Override
    public int getNextDecorId() { return this.next_decor_id; }

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
