import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {
   
    Point[] aux;
    int n;
    LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {    // finds all line segments containing 4 points
        n = points.length;
        aux = new Point[n];

        for (int i = 0; i < n; i++) {
            if (points[i] == null) 
                throw new NullPointerException("A Point is null");
            aux[i] = points[i];
        }
        Arrays.sort(aux, new ByPoint());
        // check duplicates
        for (int i = 1; i < n; i++) {
            if (aux[i] == aux[i - 1])
              throw new java.lang.IllegalArgumentException("Duplicated Point");
        }
    }
    
    private static class ByPoint implements Comparator<Point> {
        public int compare(Point v, Point w){return v.compareTo(w);}
    }

    public int numberOfSegments(){        // the number of line segments
        return segments.length;
    }
    
    public LineSegment[] segments() {                // the line segments
        Stack<LineSegment> stack = new Stack<LineSegment>();

        for (int i = 0; i < n - 3; i++) {
            for (int j = i + 1; j < n - 2; j++) {
                double slopeij = aux[i].slopeTo(aux[j]);
                for (int k = j + 1; k < n - 1; k++) {
                    double slopejk = aux[j].slopeTo(aux[k]);
                    for (int l = k + 1; l < n; l++) {
                        double slopekl = aux[k].slopeTo(aux[l]);
                        if (slopeij == slopejk &&  slopejk == slopekl)
                            stack.push(new LineSegment(aux[i], aux[l]));
                    }
                }
            }
        }
        segments = new LineSegment[stack.size()];
        for (int i = 0; i < segments.length; i++)
            segments[i] = stack.pop();
        return segments;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}