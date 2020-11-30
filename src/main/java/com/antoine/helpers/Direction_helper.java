package com.antoine.helpers;

import com.antoine.contracts.Direction;

public class Direction_helper {
    public static com.antoine.contracts.Direction intToDir(int dir )
    {
        switch ( dir )
        {
            case 0: return com.antoine.contracts.Direction.UP;
            case 1: return com.antoine.contracts.Direction.BOTTOM;
            case 2: return com.antoine.contracts.Direction.LEFT;
            case 3: return com.antoine.contracts.Direction.RIGHT;
        }
        return com.antoine.contracts.Direction.RIGHT;
    }

    public static int DirToInt( Direction direction )
    {
        switch ( direction )
        {
            case UP: return 0;
            case BOTTOM: return 1;
            case LEFT: return 2;
            case RIGHT: return 3;
        }
        return 0;
    }

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
