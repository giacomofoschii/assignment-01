/*
 *   V2d.java
 *
 * Copyright 2000-2001-2002  aliCE team at deis.unibo.it
 *
 * This software is the proprietary information of deis.unibo.it
 * Use is subject to license terms.
 *
 */
package pcd.ass01;

/**
 *
 * 2-dimensional vector
 * objects are completely state-less
 *
 */
public class V2d {
    private final double x;
    private final double y;

    public V2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public V2d sum(V2d v) {
        return new V2d(x + v.x(), y + v.y());
    }

    public double abs() {
        return Math.sqrt(x * x + y * y);
    }

    public V2d getNormalized() {
        double module = Math.sqrt(x * x + y * y);
        return new V2d(x / module, y / module);
    }

    public V2d mul(double fact) {
        return new V2d(x * fact, y * fact);
    }

    @Override
    public String toString() {
        return "V2d(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        V2d v2d = (V2d) o;
        return Double.compare(v2d.x, x) == 0 && Double.compare(v2d.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) * 31 + Double.hashCode(y);
    }
}