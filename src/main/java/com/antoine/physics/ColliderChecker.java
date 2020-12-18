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

    private static int INSIDE = 0;
    private static int LEFT   = 1;
    private static int RIGHT  = 2;
    private static int BOTTOM = 4;
    private static int TOP    = 8;

    private int computeOutCode( double x, double y, AABB frame )
    {
        int code = INSIDE;

        if ( x < frame.getMinX() ) {
            code |= LEFT;
        } else if ( x > frame.getMaxX() ) {
            code |= RIGHT;
        } else if ( y < frame.getMinY() ) {
            code |= TOP;
        } else if ( y > frame.getMaxY() ) {
            code |= BOTTOM;
        }

        return code;
    }


    private boolean cohenSutherlandClip( Point start, Point end, AABB frame )
    {
        int outCode0 = computeOutCode( start.getX(), start.getY(), frame );
        int outCode1 = computeOutCode( end.getX(), end.getY(), frame );

        boolean isCollide = false;

        while ( true ) {
            if (( outCode0 | outCode1 ) == 0 ) {
                isCollide = true;
                break;
            } else if (( outCode0 & outCode1 ) != 0 ) {
                break;
            } else {
                double x, y;
                int outCode = ( outCode1 > outCode0 ) ? outCode1 : outCode0;

                if (( outCode & TOP ) != 0 ) {

                }else if (( outCode & BOTTOM) != 0 ) {

                } else if (( outCode & LEFT ) != 0 ) {

                } else if (( outCode & RIGHT ) != 0 ) {

                }

                // Clip another intersect point for next pass
                if ( outCode == outCode0 ) {

                } else {

                }
            }
        }

        return false;
    }

    public static Shape seekCollide(Mover mover, ArrayList< Shape > shapes )
    {
        Shape shape = null;
        // create an AABB that represent the frame in the clipping alog

        AABB player = new AABB(
                mover.getX(),
                mover.getY() + mover.getHeight() - 2, // sub 2 for create a frame of height 2
                mover.getWidth(),
                2 );
        return shape;
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
