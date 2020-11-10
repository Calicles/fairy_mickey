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
}
