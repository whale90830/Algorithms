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