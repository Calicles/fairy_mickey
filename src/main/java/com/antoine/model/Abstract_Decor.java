package com.antoine.model;

import com.antoine.contracts.*;
import com.antoine.helpers.Direction_helper;

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
    /** coordonnées d'entrée telles que décrites dans le json, avant adaptation à la taille du joueur */
    protected HashMap< Direction, Point > entrance_anchors;
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
        entrance_anchors        = new HashMap<>(3);
    }

    public void setEntringPlayerCoordinates() {
        if ( ! player_reentring_pos.containsKey( this.last_player_dir )) {
            if ( player_reentring_pos.isEmpty() ) {
                throw new IllegalStateException( "decor " + id + " has no player entrance" );
            }
            // aucune entrée pour cette direction : on prend la première disponible
            this.last_player_dir = player_reentring_pos.keySet().iterator().next();
        }
        this.ending   = false;
        this.starting = true;
        player.setDirection( this.getPlayerReentrantDir() );
        player.setPosition( player_reentring_pos.get( this.last_player_dir ));
    }

    @Override
    public void setEntringPlayerCoordinates( int previous_decor_id ) {
        Direction direction = findEntranceTowards( previous_decor_id );
        if ( direction != null ) {
            this.last_player_dir = direction;
        }
        this.setEntringPlayerCoordinates();
    }

    /**
     * Cherche l'entrée la plus proche de la sortie qui mène au décor précédent.
     * @return la direction de cette entrée, ou null si aucune sortie ne mène au décor précédent
     */
    private Direction findEntranceTowards( int previous_decor_id ) {
        Direction best = null;
        double best_distance = Double.MAX_VALUE;
        for ( DecorExit exit : exit_boxes ) {
            if ( exit.getNext_decor_id() != previous_decor_id || exit.getNbrOfPoint() == 0 ) continue;
            for ( java.util.Map.Entry< Direction, Point > entry : entrance_anchors.entrySet() ) {
                double distance = distanceToBoundingBox( entry.getValue(), exit );
                if ( distance < best_distance ) {
                    best_distance = distance;
                    best = entry.getKey();
                }
            }
        }
        return best;
    }

    private static double distanceToBoundingBox( Point p, Shape shape ) {
        int min_x = Integer.MAX_VALUE, min_y = Integer.MAX_VALUE;
        int max_x = Integer.MIN_VALUE, max_y = Integer.MIN_VALUE;
        for ( Point s : shape.getPoints() ) {
            min_x = Math.min( min_x, s.getX() );
            min_y = Math.min( min_y, s.getY() );
            max_x = Math.max( max_x, s.getX() );
            max_y = Math.max( max_y, s.getY() );
        }
        int dx = Math.max( Math.max( min_x - p.getX(), 0 ), p.getX() - max_x );
        int dy = Math.max( Math.max( min_y - p.getY(), 0 ), p.getY() - max_y );
        return Math.hypot( dx, dy );
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
        this.entrance_anchors.put( direction, new Point( _x, _y ));
    }

    @Override
    public boolean isEnding() {
        return this.ending;
    }

    @Override
    public boolean isStarting() { return this.starting; }

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

    @Override
    public int getId() { return this.id; }

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
