package com.antoine.physics;

import com.antoine.contracts.Direction;
import com.antoine.model.DecorExit;
import com.antoine.model.Player;
import com.antoine.model.Point;
import com.antoine.model.Shape;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ColliderCheckerTest {

    /** joueur de 20x40 dont les pieds sont en ( x + 10, y + 40 ) */
    private static Player player( int x, int y, int speed ) {
        Player p = new Player( x, y, speed );
        p.setSize( 20, 40 );
        return p;
    }

    private static Shape rectangle( int x1, int y1, int x2, int y2 ) {
        Shape s = new Shape();
        s.addPoint( x1, y1 );
        s.addPoint( x2, y1 );
        s.addPoint( x2, y2 );
        s.addPoint( x1, y2 );
        return s;
    }

    @Test
    void seekCollideStopsPlayerJustBeforeObstacle() {
        Player p = player( 0, 0, 5 );
        p.Move( Direction.RIGHT );
        // le bord droit du joueur est en x = 20 : le mur commence en x = 23
        Shape wall = rectangle( 23, 0, 40, 100 );

        Shape hit = ColliderChecker.seekCollide( p, new ArrayList<>( Collections.singletonList( wall )));

        assertSame( wall, hit );
        assertEquals( 2, p.getDx() );
        assertEquals( 0, p.getDy() );
    }

    @Test
    void seekCollideKeepsVectorWhenPathIsFree() {
        Player p = player( 0, 0, 5 );
        p.Move( Direction.RIGHT );
        Shape far = rectangle( 200, 0, 240, 100 );

        assertNull( ColliderChecker.seekCollide( p, new ArrayList<>( Collections.singletonList( far ))));
        assertEquals( 5, p.getDx() );
    }

    @Test
    void isPlayerExitedReturnsTouchedExit() {
        Player p = player( 0, 0, 1 );
        DecorExit far  = new DecorExit( 1 );
        far.addPoint( 500, 500 ); far.addPoint( 510, 500 ); far.addPoint( 510, 510 ); far.addPoint( 500, 510 );
        DecorExit feet = new DecorExit( 2 );
        feet.addPoint( 0, 35 ); feet.addPoint( 30, 35 ); feet.addPoint( 30, 60 ); feet.addPoint( 0, 60 );

        DecorExit exit = ColliderChecker.isPlayerExited( p, new ArrayList<>( Arrays.asList( far, feet )));

        assertNotNull( exit );
        assertEquals( 2, exit.getNext_decor_id() );
    }

    @Test
    void isPlayerExitedReturnsNullAwayFromExits() {
        Player p = player( 0, 0, 1 );
        DecorExit far = new DecorExit( 1 );
        far.addPoint( 500, 500 ); far.addPoint( 510, 500 ); far.addPoint( 510, 510 ); far.addPoint( 500, 510 );

        assertNull( ColliderChecker.isPlayerExited( p, new ArrayList<>( Collections.singletonList( far ))));
    }

    @Test
    void adaptVectorToLineDoesNotOvershootTheLine() {
        // pieds en y = 40, ligne en y = 42 : la correction verticale doit être de 2, pas de speed
        Player p = player( 0, 0, 5 );
        p.Move( Direction.RIGHT );
        ArrayList< Point > line = new ArrayList<>( Arrays.asList( new Point( 0, 42 ), new Point( 300, 42 )));

        ColliderChecker.adaptVectorToLine( p, line );

        assertEquals( 5, p.getDx() );
        assertEquals( 2, p.getDy() );
    }

    @Test
    void adaptVectorToLineStopsAtTheEndOfTheLine() {
        Player p = player( 290, 0, 5 );
        p.Move( Direction.RIGHT );
        ArrayList< Point > line = new ArrayList<>( Arrays.asList( new Point( 0, 40 ), new Point( 300, 40 )));

        ColliderChecker.adaptVectorToLine( p, line );

        assertFalse( p.isMoving() );
    }

    @Test
    void hasKeyWhenPlayerCenterIsInsideKey() {
        Player p = player( 0, 0, 1 ); // centre en ( 10, 20 )
        assertTrue( ColliderChecker.hasKey( p, new AABB( 5, 10, 20, 20 )));
        assertFalse( ColliderChecker.hasKey( p, new AABB( 50, 10, 20, 20 )));
    }
}
