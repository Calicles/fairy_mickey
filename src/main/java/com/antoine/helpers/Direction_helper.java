package com.antoine.helpers;

import com.antoine.contracts.Direction;

public class Direction_helper {
    public static Direction getInversDir( Direction dir )
    {
        switch ( dir )
        {
            case UP: return Direction.BOTTOM;
            case BOTTOM: return Direction.UP;
            case LEFT: return Direction.RIGHT;
            case RIGHT: return Direction.LEFT;
        }
        return Direction.BOTTOM;
    }

    public static Direction strToDir( String dir_str )
    {
        if ( dir_str.equals( "UP" )) {
            return Direction.UP;
        } else if ( dir_str.equals( "DOWN" )) {
            return Direction.BOTTOM;
        }else if ( dir_str.equals( "RIGHT" )) {
            return Direction.RIGHT;
        }else if ( dir_str.equals( "LEFT" )) {
            return Direction.LEFT;
        } else {
            throw new IllegalArgumentException( "bad direction parameter" );
        }
    }
}
