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