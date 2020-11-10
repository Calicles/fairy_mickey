package com.antoine.physics;

import com.antoine.contracts.Mover;
import com.antoine.model.DecorExit;
import com.antoine.model.Point;
import com.antoine.model.Shape;

import java.util.ArrayList;

public class ColliderChecker {
    public static Shape seekCollide(Mover mover, ArrayList< Shape > shapes )
    {
        return null;
    }

    public static Shape isPlayerExited(Mover mover, ArrayList<DecorExit > exites )
    {
        ArrayList< Shape > tmp = new ArrayList<>( exites.size() );
        for ( DecorExit de : exites ) {
            tmp.add( (Shape) de);
        }
        return seekCollide( mover, tmp );
    }

    public static void adaptVectorToLine( Mover mover, ArrayList<Point> line ) {

    }
}
