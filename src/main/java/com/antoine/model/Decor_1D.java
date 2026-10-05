package com.antoine.model;

import com.antoine.contracts.Direction;
import com.antoine.physics.ColliderChecker;
import java.util.ArrayList;

public class Decor_1D extends Abstract_Decor
{
    protected ArrayList< Point > line_mask;

    protected boolean is_vertical;

    public Decor_1D( int _id )
    {
        super( _id);
        line_mask = new ArrayList<>(5);
    }

    @Override
    public void update() {
        if ( ! starting && ! ending ) {
            DecorExit exit = ColliderChecker.isPlayerExited( this.player, this.exit_boxes );
            if ( exit != null ) {
                this.next_decor_id = exit.getNext_decor_id();
                this.ending = true;
                super.last_player_dir = player.getDirection();
            } else {
                if ( this.player.isMoving() )
                {
                    ColliderChecker.adaptVectorToLine( player, line_mask );
                    checkOtherCollides();
                    this.player.translate();
                }
            }
        }
    }

    protected void checkOtherCollides()
    {}

    public void setIs_vertical( boolean is_vertical )
    {
        this.is_vertical = is_vertical;
    }

    public void addPointToMask( int x, int y )
    {
        line_mask.add( new Point( x, y ));
    }

    @Override
    public void setPlayerDirection(Direction direction) {
        if ( isValidDirection( direction ))
        {
            player.Move( direction );
        }
    }

    private boolean isValidDirection(Direction direction) {
        return this.is_vertical ? ( direction == Direction.UP || direction == Direction.BOTTOM ) : ( direction == Direction.LEFT || direction == Direction.RIGHT );
    }
}
