package com.antoine.physics;

import com.antoine.model.Point;


public class CohenSutherland {

    private double xMin;
    private double yMin;
    private double xMax;
    private double yMax;

    private static final int INSIDE = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int BOTTOM = 4;
    private static final int TOP = 8;

    private final static float MINIMUM_DELTA = 0.01f;

    /**
     * Creates a Cohen Sutherland clipper with clip window (0, 0, 0, 0).
     */
    public CohenSutherland() {
    }

    /**
     * Creates a Cohen Sutherland clipper with the given clip window.
     *
     * @param clipWindow the clip window to use.
     */
    public CohenSutherland( AABB clipWindow) {
        setClip(clipWindow);
    }

    /**
     * Sets the clip rectangle.
     *
     * @param clipWindow the clip window.
     */
    //public void setClip(Rectangle2D clipWindow) {
    public void setClip( AABB clipWindow) {
        xMin = clipWindow.getMinX();
        xMax = clipWindow.getMaxX();
        yMin = clipWindow.getMinY();
        yMax = clipWindow.getMaxY();
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //

    /**
     * Clips a given line against the clip window.
     * The modification (if needed) is done in place.
     *
    // * @param line the line to clip.
     * @return true if line is clipped, false if line is
     * totally outside the clip window.
     */
    //public boolean clip(Line2D.Float line) {
     public boolean clip( Point start, Point end ) {
        PointR point1 = new PointR( start.getX(), start.getY() );
        PointR point2 = new PointR( end.getX(), end.getY() );
        PointR outsidePoint = new PointR(0d, 0d);

        boolean lineIsVertical = (point1.x == point2.x);
        double lineSlope = lineIsVertical ? 0d : (point2.y - point1.y) / (point2.x - point1.x);

        while (point1.region != INSIDE || point2.region != INSIDE) {
            if ((point1.region & point2.region) != 0) return false;

            outsidePoint.region = (point1.region == INSIDE) ? point2.region : point1.region;

            if ((outsidePoint.region & LEFT) != 0) {
                outsidePoint.x = xMin;
                outsidePoint.y = delta(outsidePoint.x, point1.x) * lineSlope + point1.y;
            } else if ((outsidePoint.region & RIGHT) != 0) {
                outsidePoint.x = xMax;
                outsidePoint.y = delta(outsidePoint.x, point1.x) * lineSlope + point1.y;
            } else if ((outsidePoint.region & BOTTOM) != 0) {
                outsidePoint.y = yMin;
                outsidePoint.x = lineIsVertical
                        ? point1.x
                        : delta(outsidePoint.y, point1.y) / lineSlope + point1.x;
            } else if ((outsidePoint.region & TOP) != 0) {
                outsidePoint.y = yMax;
                outsidePoint.x = lineIsVertical
                        ? point1.x
                        : delta(outsidePoint.y, point1.y) / lineSlope + point1.x;
            }

            if (outsidePoint.isInTheSameRegionAs(point1)) {
                point1.setPositionAndRegion(outsidePoint.x, outsidePoint.y);
            } else {
                point2.setPositionAndRegion(outsidePoint.x, outsidePoint.y);
            }
        }
        //line.setLine(point1.x, point1.y, point2.x, point2.y);

        return true;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    private static double delta(double value1, double value2) {
        return (Math.abs(value1 - value2) < MINIMUM_DELTA) ? 0 : (value1 - value2);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - //
    class PointR {
        double x, y;
        int region;

        PointR(double x, double y) {
            setPositionAndRegion(x, y);
        }

        void setPositionAndRegion(double x, double y) {
            this.x = x; this.y = y;
            region = (x < xMin) ? LEFT : (x > xMax) ? RIGHT : INSIDE;
            if (y < yMin)
                region |= BOTTOM;
            else if (y > yMax)
                region |= TOP;
        }

        boolean isInTheSameRegionAs(PointR otherPoint) {
            return this.region == otherPoint.region;
        }
    }
}
