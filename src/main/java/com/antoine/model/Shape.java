package com.antoine.model;

import java.util.ArrayList;

import java.util.ArrayList;

public class Shape {
    private ArrayList< Point > points;

    public Shape()
    {
        this.points = new ArrayList<>(5);
    }

    public boolean addPoint( Point p )
    {
        return points.add( p );
    }

    public ArrayList< Point > getPoints() { return this.points; }

    public int getNbrOfPoint() { return points.size(); }

    public Point getPoint( int index )
    {
        return points.get( index );
    }

    public String toString()
    {
        StringBuffer buff = new StringBuffer();
        for ( Point p : points )
        {
            buff.append( p.toString() );
            buff.append( '\n' );
        }
        return buff.toString();
    }
}
