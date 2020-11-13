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
}
