package com.antoine.physics;

import com.antoine.contracts.Direction;
import com.antoine.contracts.Mover;
import com.antoine.model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

public class ColliderChecker {

    public static Line collide;
    public static Point nexttep;
    public static Point playerMiddle;
    public static CohenSutherland cs = new CohenSutherland();

    private static AABB playerToAABB( Mover mover ) {
        return new AABB(
                mover.getX(),
                mover.getY() + mover.getHeight() - 10, // sub 2 for create a frame of height 2
                mover.getWidth(),
                10 );
    }


    public static Shape seekCollide(Mover mover, ArrayList< Shape > shapes )
    {
        Shape shape = null;
        // create an AABB that represent the frame in the clipping alog

        AABB player = playerToAABB( mover );

        cs.setClip( player );

        int xSign = (mover.getDx() != 0 ) ? (int) Math.signum( mover.getDx() ) : 0;
        int ySign = (mover.getDy() != 0 ) ? (int) Math.signum( mover.getDy() ) : 0;

        for ( int i = 1; i <= mover.getSpeed(); ++i ) {
            int dx = 1 * xSign, dy = 1 * ySign;
            player.translate( dx, dy );
            cs.setClip( player );
            for ( Shape s : shapes ) {
                if ( checkCollide( s, player)) {
                    shape = s;
                    break;
                }
            }
            if ( shape != null ) {
                dx = (i - 1) * xSign;
                dy = (i - 1) * ySign;
                mover.setVector( (i - 1) * xSign, (i - 1) * ySign );
                break;
            }
        }

        return shape;
    }

    private static boolean checkCollide( Shape shape, AABB player ) {
        for (int i = 0; i < shape.getNbrOfPoint(); i++) {
            Point start, end;
            start = shape.getPoint( i );
            if ( i == shape.getNbrOfPoint() - 1 ) {
                end = shape.getPoint( 0 );
            } else {
                end = shape.getPoint( i + 1 );
            }
            if ( cs.clip( start, end )) {
                collide = new Line();
                collide.p1 = start;
                collide.p2 = end;
                return true;
            }
        }
        return false;
    }

    public static DecorExit isPlayerExited(Mover mover, ArrayList<DecorExit > exites )
    {
        AABB aabb = playerToAABB( mover );
        cs.setClip( aabb );
        DecorExit collide = exites
            .stream()
            .filter( e -> checkCollide( e, aabb ))
            .findFirst()
            .orElse( null );
        return collide;
    }

    private static Point findNextStep( Point player_middle, ArrayList< Point > line, Direction dir )
    {
        Stream< Point > sorted_n_filtered;
        if ( dir == Direction.UP )
        {
            sorted_n_filtered = line
                    .stream()
                    .sorted( (p1, p2) -> Integer.compare( p2.getY(), p1.getY() ))
                    .filter( p -> p.getY() < player_middle.getY() );

        }
        else if ( dir == Direction.BOTTOM  ) {
            sorted_n_filtered = line
                    .stream()
                    .sorted( Comparator.comparingInt( Point::getY ))
                    .filter( p -> player_middle.getY() < p.getY());
        }
        else if ( dir == Direction.RIGHT )
        {
            sorted_n_filtered = line
                    .stream()
                    .sorted( Comparator.comparingInt( Point::getX ))
                    .filter( p -> player_middle.getX() < p.getX() );
        }
        else {
            sorted_n_filtered = line
                    .stream()
                    .sorted( (p1, p2) -> Integer.compare( p2.getX(), p1.getX() ) )
                    .filter( p -> p.getX() < player_middle.getX() );
        }

        return sorted_n_filtered.findFirst().orElse( null );
    }
    public static void adaptVectorToLine( Mover mover, ArrayList<Point> line ) {
        Direction dir   = mover.getDirection();
        Point middle    = new Point(mover.getX() + mover.getWidth() / 2 , mover.getY() + mover.getHeight() );
        Point nextPoint = findNextStep( middle, line, dir );
        nexttep         = nextPoint;

        if ( nextPoint == null ) {
            mover.setVector( 0, 0 );
        }
        else {
            playerMiddle = middle;
            if ( dir == Direction.UP || dir == Direction.BOTTOM ) {
                if ( middle.getX() < nextPoint.getX() ) {
                    mover.setVector( mover.getSpeed(), mover.getDy());
                } else if ( middle.getX() > nextPoint.getX() ) {
                    mover.setVector( - mover.getSpeed(), mover.getDy());
                }
            } else
            {
                if ( middle.getY() < nextPoint.getY() ) {
                    mover.setVector(mover.getDx(), mover.getSpeed() );
                } else if ( middle.getY() > nextPoint.getY() ) {
                    mover.setVector(mover.getDx(), - mover.getSpeed() );
                }
            }
        }
    }

    public static boolean hasKey(Mover player, AABB key) {
        int dx1 = ( player.getWidth() / 2 + player.getX() ) - key.getMaxX();
        int dy1 = ( player.getHeight() / 2 + player.getY() ) - key.getMaxY();
        int dx2 = key.getMinX() - (player.getX() + player.getWidth() / 2);
        int dy2 = key.getMinY() - (player.getY() + player.getHeight() / 2);

        if ( dx1 > 0 || dy1 > 0 ) return false;
        if ( dx2 > 0 || dy2 > 0 ) return false;

        return true;
    }
}
