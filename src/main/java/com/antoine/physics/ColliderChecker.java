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

    private static boolean isCollideDroiteSegment( Point A, Point B,  Point O, Point P )
    {
        Vector AO = new Vector(), AP = new Vector(), AB = new Vector();

        AB.x = B.getX() - A.getX();
        AB.y = B.getY() - A.getY();

        AP.x = P.getX() - A.getX();
        AP.y = P.getY() - A.getY();

        AO.x = O.getX() - A.getX();
        AO.y = O.getY() - A.getY();

        if (( AB.x * AP.y - AB.y * AP.x ) * ( AB.x * AO.y - AB.y * AO.x ) < 0) return true;
        return false;
    }

    private static boolean isCollideSegSeg( Point d1, Point d2, Point s1, Point s2 )
    {
        if ( ! isCollideDroiteSegment( d1, d2, s1, s2 ) ) return false;
        if ( ! isCollideDroiteSegment( s1, s2, d1, d2 ) ) return false;

        collide = new Line();
        collide.p1 = d1;
        collide.p2 = d2;
        return true;
    }

    private static boolean isCollide( Mover mover, Shape solid )
    {
        Point mover1 = new Point( mover.getX(), mover.getY() );
        Point mover2 = new Point( mover.getX() + mover.getWidth(), mover.getY() );

        int nbr_point = solid.getNbrOfPoint();

        for ( int i = 0; i < nbr_point; i++ )
        {
            int index;
            if ( i == nbr_point - 1 )
            {
                index = 0;
            } else
            {
                index = i + 1;
            }
            Point p1 = solid.getPoint( i );
            Point p2 = solid.getPoint( index );
            if ( isCollideSegSeg( p1, p2, mover1, mover2 )) return true;
        }
        return false;
    }

    public static Shape seekCollide(Mover mover, ArrayList< Shape > shapes )
    {
        Shape shape = null;
        for ( Shape s : shapes )
        {
            if ( isCollide( mover, s )) shape =  s;
        }
        if ( shape == null ) collide = null;
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
