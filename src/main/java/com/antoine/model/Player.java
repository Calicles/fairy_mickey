package com.antoine.model;


import com.antoine.contracts.Direction;
import com.antoine.contracts.Mover;
import com.antoine.contracts.RenderedMotionEntity;

public class Player extends Entity implements Mover, RenderedMotionEntity {

    private final int speed;
    private int dx;
    private int dy;
    private Direction current_direction;

    private boolean hasFirstKey  = false;
    private boolean hasSecondKey = false;
    private boolean hasThirdKey  = false;

    public Player( int _x, int _y, int _speed )
    {
        super( _x, _y );
        this.speed = _speed;
    }

    public Direction getDirection() { return this.current_direction; }

    @Override
    public int getDx() {
        return dx;
    }

    @Override
    public int getDy() {
        return dy;
    }

    @Override
    public void setVector(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    public boolean isMoving() { return ( dx != 0 || dy != 0 ); }

    @Override
    public boolean hasKey(int key_id) {
        switch ( key_id ) {
            case 1: return hasFirstKey;
            case 2: return hasSecondKey;
            case 3: return hasThirdKey;

            default:return false;
        }
    }

    public Direction getCurrent_direction() { return this.current_direction; }

    public void setDirection( Direction _dir ) { this.current_direction = _dir; }
    public void setPosition( int x, int y ) { this.coordinates.setPosition( x, y ); }
    public void setPosition( Point p ) { this.coordinates.setPosition( p );}

    public void resetDelta() {
        this.dx = 0;
        this.dy = 0;
    }

    public void Move(Direction translate) {
        if ( this.current_direction != translate ) this.current_direction = translate;

        switch ( translate ) {
            case UP: dx = 0; dy = -speed;
                break;
            case BOTTOM: dx = 0; dy = speed;
                break;
            case LEFT: dx = -speed; dy = 0;
                break;
            case RIGHT: dx = speed; dy = 0;
        }
    }
}
