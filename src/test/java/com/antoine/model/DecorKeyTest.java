package com.antoine.model;

import com.antoine.contracts.RenderedEntity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DecorKeyTest {

    @Test
    void keyIsGrabbedOnceWhenPlayerTouchesIt() {
        DecorKey key = new DecorKey();
        key.setKeyId( 2 );
        key.setKeyCoordinates( 0, 0 );
        key.setKeyDimension( 50, 50 );
        Player player = new Player( 0, 0, 1 );
        player.setSize( 20, 40 );
        List< Integer > found = new ArrayList<>();

        key.checkGrab( player, found::add );
        key.checkGrab( player, found::add );

        assertEquals( 1, found.size() );
        assertEquals( 2, found.get( 0 ));
        RenderedEntity[] entities = key.getEntities( player );
        assertSame( player, entities[0] );
        assertNull( entities[1] );
    }

    @Test
    void keyStaysWhenPlayerIsFar() {
        DecorKey key = new DecorKey();
        key.setKeyId( 1 );
        key.setKeyCoordinates( 500, 500 );
        key.setKeyDimension( 50, 50 );
        Player player = new Player( 0, 0, 1 );
        player.setSize( 20, 40 );

        key.checkGrab( player, id -> fail( "key should not be found" ));

        assertNotNull( key.getEntities( player )[1] );
    }
}
