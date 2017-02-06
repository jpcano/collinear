import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints {
    private Point[] points;
    private int n;

    public FastCollinearPoints(Point[] points) {    // finds all line segments containing 4 points
        n = points.length;
        this.points = new Point[n];

        for (int i = 0; i < n; i++) {
            if (points[i] == null) 
                throw new NullPointerException("A Point is null");
            this.points[i] = points[i];
        }
        Arrays.sort(this.points, new ByPoint());
        // check duplicates
        for (int i = 1; i < n; i++) {
            if (this.points[i].compareTo(this.points[i - 1]) == 0)
              throw new java.lang.IllegalArgumentException("Duplicated Point");
        }
    }
    
    private static class ByPoint implements Comparator<Point> {
        public int compare(Point v, Point w){return v.compareTo(w);}
    }

    public int numberOfSegments(){        // the number of line segments
        LineSegment[] segments;
        segments = segments();
        return segments.length;
    }
    
    public LineSegment[] segments() {                // the line segments
        Stack<LineSegment> stack = new Stack<LineSegment>();
        Point origin;
        LineSegment[] segments;
        Point[] aux;
        Point[] colinear;

        aux = new Point[n];
        for (int i = 0; i < n - 3; i++) {
            for (int iter = i; iter < n; iter++)
                aux[iter] = points[iter];
            origin = aux[i];
            Arrays.sort(aux, i + 1, n, origin.slopeOrder());
            for (int j = i + 1; j < n - 2; j++ ) {
                if (checkSlopes(origin, aux[j], aux[j + 2])) {
                    stack.push(new LineSegment(origin, aux[j + 2]));
                    j++;
                }
            }

        }
        segments = new LineSegment[stack.size()];
        for (int i = 0; i < segments.length; i++)
            segments[i] = stack.pop();
        return segments;
    }
    
    private boolean checkSlopes(Point origin, Point a, Point b) {
        return origin.slopeTo(a) == origin.slopeTo(b);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}