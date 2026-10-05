package com.antoine.game;

import com.antoine.contracts.Decor;
import com.antoine.contracts.Direction;
import com.antoine.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecorFactoryTest {

    private static Player player;

    @BeforeAll
    static void setUp() {
        player = new Player( 0, 0, 1 );
        player.setSize( 20, 40 );
        DecorFactory.setPlayer( player );
    }

    @Test
    void everyDecorOfTheJsonIsBuiltWithTheRightType() {
        assertTrue( DecorFactory.getDecor( 1 ) instanceof Decor_1D );
        assertTrue( DecorFactory.getDecor( 2 ) instanceof Decor_2D );
        assertTrue( DecorFactory.getDecor( 3 ) instanceof Decor_2D );
        assertTrue( DecorFactory.getDecor( 4 ) instanceof Decor_1D );
        assertTrue( DecorFactory.getDecor( 5 ) instanceof Decor_with_key );
        assertTrue( DecorFactory.getDecor( 6 ) instanceof Decor_1D );
        assertTrue( DecorFactory.getDecor( 7 ) instanceof Decor_with_key_2d );
    }

    @Test
    void decorsAreCached() {
        assertSame( DecorFactory.getDecor( 2 ), DecorFactory.getDecor( 2 ));
    }

    @Test
    void decorsWithKeyExposeTheKey() {
        assertNotNull( DecorFactory.getDecor( 5 ).getEntities()[1] );
        assertNotNull( DecorFactory.getDecor( 7 ).getEntities()[1] );
    }

    /**
     * Le joueur doit arriver à l'entrée située près de la sortie qui ramène au décor précédent,
     * et regarder vers l'intérieur du décor.
     */
    @ParameterizedTest( name = "{0} -> {1} : entrée {2}" )
    @CsvSource( {
            "1, 3, RIGHT",
            "3, 1, LEFT",
            "3, 2, BOTTOM",
            "2, 3, UP",
            "5, 2, RIGHT",
            "4, 2, LEFT",
            "2, 5, LEFT",
            "4, 6, BOTTOM",
            "6, 4, UP",
            "7, 6, UP",
            "6, 7, UP",
    } )
    void playerEntersNextToTheExitLeadingBack( int from, int to, Direction entrance ) {
        Decor decor = DecorFactory.getDecor( to );

        decor.setEntringPlayerCoordinates( from );

        assertEquals( opposite( entrance ), player.getDirection() );
    }

    private static Direction opposite( Direction d ) {
        switch ( d ) {
            case UP:     return Direction.BOTTOM;
            case BOTTOM: return Direction.UP;
            case LEFT:   return Direction.RIGHT;
            default:     return Direction.LEFT;
        }
    }
}
