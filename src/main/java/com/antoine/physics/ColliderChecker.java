package com.antoine.physics;

import com.antoine.contracts.Direction;
import com.antoine.contracts.Mover;
import com.antoine.model.DecorExit;
import com.antoine.model.Point;
import com.antoine.model.Shape;

import java.util.ArrayList;
import java.util.function.Function;

public class ColliderChecker {

    public static Line collide;

    public static CohenSutherland cs = new CohenSutherland();

    private static final int INSIDE = 0;
    private static final int LEFT   = 1;
    private static final int RIGHT  = 2;
    private static final int BOTTOM = 4;
    private static final int TOP    = 8;


    public static Shape seekCollide(Mover mover, ArrayList< Shape > shapes )
    {
        Shape shape = null;
        // create an AABB that represent the frame in the clipping alog

        AABB player = new AABB(
                mover.getX(),
                mover.getY() + mover.getHeight() - 10, // sub 2 for create a frame of height 2
                mover.getWidth(),
                10 );

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

    public static Shape isPlayerExited(Mover mover, ArrayList<DecorExit > exites )
    {
        ArrayList< Shape > tmp = new ArrayList<>( exites.size() );
        for ( DecorExit de : exites ) {
            tmp.add( (Shape) de);
        }
        return seekCollide( mover, tmp );
    }

    private static Point findNextStep( Mover mover, ArrayList< Point > line )
    {
        Point closerPoint = line.get( 0 );
        Direction dir = mover.getDirection();
        Point center = new Point( ( mover.getWidth() / 2 ), ( mover.getHeight() / 2 ));
        Function<Point, Integer> predicate;
        if ( dir == Direction.BOTTOM ) predicate = p -> p.getY() - center.getY();
        else if ( dir == Direction.UP ) predicate = p -> center.getY() - p.getY();
        else if ( dir == Direction.LEFT ) predicate = p -> center.getX() - p.getX();
        else predicate = p -> p.getX() - center.getX();
        int prevDelta = predicate.apply( closerPoint );

        for (int i = 1; i < line.size(); i++) {
            int delta = predicate.apply( line.get( i ));
            if ( delta > 0 && delta < prevDelta ) {
                closerPoint = line.get( i );
            }
            prevDelta = delta;
        }
        return closerPoint;
    }
    public static void adaptVectorToLine( Mover mover, ArrayList<Point> line ) {
        Point p = findNextStep( mover, line );
        Direction dir = mover.getDirection();
        int speed = mover.getSpeed();
        int delta;
        if ( dir == Direction.BOTTOM || dir == Direction.UP ) {
            delta = mover.getX() - p.getX();
            if ( delta > 0 ) { // Point is on left
                delta = delta < speed ? -delta : -speed;
            } else {    // Point on right
                delta = delta > -speed ? delta : speed;
            }
            mover.setVector( delta, mover.getDy() );
        } else {
            delta = mover.getY() - p.getY();
            if ( delta > 0 ) {  // point is upper
                delta = delta < speed ? -delta : -speed;
            } else {    // point is on down
                delta = delta > -speed ? delta : speed;
            }
            mover.setVector( mover.getDx(), delta );
        }
    }
}
