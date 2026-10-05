package com.antoine.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import com.antoine.contracts.Direction;

import java.util.LinkedHashMap;

/**
 * Croix directionnelle tactile, multi-touch : la direction retenue est celle du dernier doigt posé.
 */
final class DPad {

    private float cx, cy, radius;
    /** doigt -> direction, dans l'ordre où les directions ont été choisies */
    private final LinkedHashMap< Integer, Direction > touches = new LinkedHashMap<>();

    private final Paint base   = new Paint( Paint.ANTI_ALIAS_FLAG );
    private final Paint arrow  = new Paint( Paint.ANTI_ALIAS_FLAG );
    private final Paint active = new Paint( Paint.ANTI_ALIAS_FLAG );

    DPad()
    {
        base.setColor( 0x40FFFFFF );
        arrow.setColor( 0xA0FFFFFF );
        active.setColor( 0xE0FFD54F );
    }

    /** place la croix dans la marge gauche si elle est assez large, sinon en bas à gauche */
    void layout( int screenWidth, int screenHeight, float leftMargin )
    {
        radius = Math.min( screenHeight * 0.2f, screenWidth * 0.12f );
        cx = leftMargin > radius * 2.4f ? leftMargin / 2f : radius * 1.3f;
        cy = screenHeight - radius * 1.4f;
    }

    /** @return la direction courante, ou null si aucun doigt n'est sur la croix */
    synchronized Direction current()
    {
        Direction last = null;
        for ( Direction d : touches.values() ) last = d;
        return last;
    }

    synchronized void onTouch( MotionEvent e )
    {
        int action = e.getActionMasked();
        switch ( action ) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                int i = e.getActionIndex();
                update( e.getPointerId( i ), e.getX( i ), e.getY( i ), true );
                break;
            }
            case MotionEvent.ACTION_MOVE:
                for ( int i = 0; i < e.getPointerCount(); i++ ) {
                    update( e.getPointerId( i ), e.getX( i ), e.getY( i ), false );
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                touches.remove( e.getPointerId( e.getActionIndex() ));
                break;
            case MotionEvent.ACTION_CANCEL:
                touches.clear();
                break;
            default:
                break;
        }
    }

    private void update( int pointer, float x, float y, boolean isNew )
    {
        Direction d = directionAt( x, y );
        Direction previous = touches.get( pointer );
        if ( d == null ) {
            // un doigt qui glisse hors de la croix la relâche ; un nouveau doigt ailleurs est ignoré
            if ( previous != null ) touches.remove( pointer );
            return;
        }
        if ( d != previous || isNew ) {
            touches.remove( pointer );
            touches.put( pointer, d );
        }
    }

    private Direction directionAt( float x, float y )
    {
        float dx = x - cx, dy = y - cy;
        float dist = (float) Math.hypot( dx, dy );
        if ( dist > radius * 1.8f || dist < radius * 0.15f ) return null;
        if ( Math.abs( dx ) > Math.abs( dy )) return dx > 0 ? Direction.RIGHT : Direction.LEFT;
        return dy > 0 ? Direction.BOTTOM : Direction.UP;
    }

    void draw( Canvas canvas )
    {
        Direction pressed = current();
        canvas.drawCircle( cx, cy, radius, base );
        drawArrow( canvas, Direction.UP,     0, -1, pressed );
        drawArrow( canvas, Direction.BOTTOM, 0,  1, pressed );
        drawArrow( canvas, Direction.LEFT,  -1,  0, pressed );
        drawArrow( canvas, Direction.RIGHT,  1,  0, pressed );
    }

    private void drawArrow( Canvas canvas, Direction d, int ux, int uy, Direction pressed )
    {
        float tip = radius * 0.85f, back = radius * 0.4f, half = radius * 0.25f;
        Path p = new Path();
        // pointe dans la direction (ux, uy), base perpendiculaire
        p.moveTo( cx + ux * tip, cy + uy * tip );
        p.lineTo( cx + ux * back - uy * half, cy + uy * back + ux * half );
        p.lineTo( cx + ux * back + uy * half, cy + uy * back - ux * half );
        p.close();
        canvas.drawPath( p, d == pressed ? active : arrow );
    }
}
