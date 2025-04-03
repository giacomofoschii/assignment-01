package pcd.ass01.multithreading.jpftesting;

/**
 *
 * 2-dimensional point
 * objects are completely state-less
 *
 */
public class P2d {
    private final double x;
    private final double y;

    public P2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public P2d sum(V2d v) {
        return new P2d(x + v.x(), y + v.y());
    }

    public V2d sub(P2d v) {
        return new V2d(x - v.x(), y - v.y());
    }

    public double distance(P2d p) {
        double dx = p.x() - x;
        double dy = p.y() - y;
        return Math.sqrt(dx*dx + dy*dy);
    }

    public String toString() {
        return "P2d(" + x + "," + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        P2d p2d = (P2d) obj;
        return Double.compare(p2d.x, x) == 0 && Double.compare(p2d.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * Double.hashCode(x) + Double.hashCode(y);
    }
}