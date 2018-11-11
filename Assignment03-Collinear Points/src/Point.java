import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the x-coordinate of the point
     * @param  y the y-coordinate of the point
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * the slope between this point and that point
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        if(this.x == that.x && this.y == that.y) return Double.NEGATIVE_INFINITY;
        else if(this.x == that.x) return Double.POSITIVE_INFINITY;
        else if(this.y == that.y) return +0.0;
        else return (double)(that.y - this.y)/(that.x - this.x);
    }

    /**
     * compare two points by y-coordinates, breaking ties by x-coordinates
     *
     * @param  that the other point
     * @return the value 0 if this point is equal to the argument point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument point;
     *         and a positive integer if this point is greater than the argument point
     */
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        if (this.y < that.y ) return -1;
        if (this.y > that.y ) return +1;
        if (this.x < that.x) return -1;
        if (this.x > that.x) return +1;
        return 0;
    }

    /**
     * compare two points by slopes they make with this point
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        return new slopeComparator();
    }

    private class slopeComparator implements Comparator<Point>
    {
        public int compare(Point q1, Point q2)
        {
            if (Point.this.slopeTo(q1) < Point.this.slopeTo(q2)) return -1;
            else if (Point.this.slopeTo(q1) > Point.this.slopeTo(q2)) return 1;
            else return 0;
        }
    }

    /**
     * string representation
     *
     * @return a string representation of this point
     */
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        Point p1 = new Point(1,2);
        Point p2 = new Point(6,5);

        System.out.println(p1.slopeTo(p2));
    }
}