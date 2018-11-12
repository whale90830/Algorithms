#### Point.java

```java
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
```

#### BruteCollinearPoints.java

```java
import edu.princeton.cs.algs4.In;

import java.util.Arrays;

public class BruteCollinearPoints {

    private LineSegment[] segments;
    private int numberOfSegments;

    /**
     * finds all line segments containing 4 points
     * @param points
     */
    public BruteCollinearPoints(Point[] points) {
        if(points == null) throw new java.lang.IllegalArgumentException();

        numberOfSegments = 0;
        LineSegment[] temp_segments = new LineSegment[points.length/3];
        for (int i = 0; i < points.length; i++) {
            if(points[i] == null) throw new java.lang.IllegalArgumentException();

            for (int j = i+1; j < points.length; j++) {
                if(points[i].compareTo(points[j]) == 0) throw new java.lang.IllegalArgumentException();

                int count = 0;
                Point[] segment = new Point[4]; segment[0] = points[i];
                for (int k = j; k < points.length; k++) {
                    if(points[i].slopeTo(points[j]) == points[i].slopeTo(points[k])) {
                        segment[++count] = points[k];
                    }
                    if(count == 3) {
                        Arrays.sort(segment);
                        temp_segments[numberOfSegments++] = new LineSegment(segment[0],segment[3]);
                        break;
                    }
                }
            }
        }
        segments = new LineSegment[numberOfSegments];
        for (int i = 0; i < numberOfSegments; i++) {
            segments[i] = temp_segments[i];
        }
    }

    /**
     * the number of line segments
     * @return
     */
    public int numberOfSegments() {
        return numberOfSegments;
    }

    /**
     * the line segments
     * @return
     */
    public LineSegment[] segments() {
        return segments;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In("equidistant.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (int i = 0; i < collinear.numberOfSegments(); i++) {
            System.out.println(collinear.segments()[i].toString());
        }
    }
}
```

#### FastCollinearPoints.java

```java
import edu.princeton.cs.algs4.In;

import java.util.Arrays;

public class FastCollinearPoints {

    private LineSegment[] segments;
    private int numberOfSegments;

    /**
     * finds all line segments containing 4 or more points
     * @param points
     */
    public FastCollinearPoints(Point[] points) {
        if(points == null) throw new java.lang.IllegalArgumentException();

        Point[] slopeOrderPoints = points.clone();
        int countOfSegments = 0;
        LineSegment[] temp_segments = new LineSegment[points.length*points.length];

        for (int i = 0; i < points.length; i++) {

            if(points[i] == null) throw new java.lang.IllegalArgumentException();

            Arrays.sort(slopeOrderPoints, points[i].slopeOrder());

            for (int j = 0; j < slopeOrderPoints.length-1; ++j) {

                Point[] temp_segment = new Point[points.length];  // initiate a temp segment contains all the points at most
                temp_segment[0] = points[i];    // one point of the temp segment must be points[i]
                int count = 1;  // count records how many points the temp segment contains now

                if (points[i].slopeTo(slopeOrderPoints[j]) == points[i].slopeTo(slopeOrderPoints[j+1])) {
                    while (j < slopeOrderPoints.length - 1) {
                        temp_segment[count++] = slopeOrderPoints[j];
                        if (points[i].slopeTo(slopeOrderPoints[j]) == points[i].slopeTo(slopeOrderPoints[j+1])) {
                            ++j;
                        }
                        else {
                            break;
                        }
                    }
                    if (j == slopeOrderPoints.length - 1) {
                        temp_segment[count++] = slopeOrderPoints[j];
                    }

                    if (count >= 4) {
                        Point[] segment = new Point[count];
                        for (int k = 0; k < count; k++) {
                            segment[k] = temp_segment[k];
                        }
                        Arrays.sort(segment);
                        LineSegment line = new LineSegment(segment[0], segment[count - 1]);
                        boolean isIn = false;
                        for (int k = 0; k < countOfSegments; k++) {
                            if (temp_segments[k].toString().compareTo(line.toString()) == 0) {
                                isIn = true;
                                break;
                            }
                        }
                        if (!isIn) {
                            temp_segments[countOfSegments++] = line;
                        }
                    }
                }
            }
        }
        numberOfSegments = countOfSegments;
        segments = new LineSegment[countOfSegments];
        for (int i = 0; i < countOfSegments; i++) {
            segments[i] = temp_segments[i];
        }
    }

    /**
     * the number of line segments
     * @return
     */
    public int numberOfSegments() {
        return numberOfSegments;
    }

    /**
     * the line segments
     * @return
     */
    public LineSegment[] segments() {
        return segments;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In("input48.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (int i = 0; i < collinear.numberOfSegments(); i++) {
            System.out.println(collinear.segments()[i].toString());
        }
    }
}
```