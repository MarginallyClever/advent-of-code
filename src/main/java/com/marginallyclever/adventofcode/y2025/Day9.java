package com.marginallyclever.adventofcode.y2025;

import com.marginallyclever.adventofcode.AdventOfCode;

import javax.vecmath.Point2d;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Day9 extends AdventOfCode {
    public static void main(String[] args) {
        new Day9().run();
    }

    List<Point2d> points = new ArrayList<>();
    int read=0;
    int topX, topY;

    public static class LineSegment {
        Point2d p1;
        Point2d p2;

        public LineSegment(Point2d p1, Point2d p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
    }

    @Override
    public void processLine(String line) {
        System.out.println(read+"...");
        read++;

        String[] toks = line.split(",");
        points.add( new Point2d(
                Double.parseDouble(toks[0]),
                Double.parseDouble(toks[1]) ) );
    }

    @Override
    public void postProcess() {
        getSizeOfMap();

        //part1();
        part2();
    }

    // find the size of the map
    private void getSizeOfMap() {
        topX = 0;
        topY = 0;
        for (Point2d point : points) {
            if (point.x > topX) topX = (int)point.x;
            if (point.y > topY) topY = (int)point.y;
        }
        System.out.println("Map size is "+topX+" x "+topY);
    }

    private void part1() {
        //drawMap();
        findBiggestRectangle();
    }

    // find the two points that make the biggest rectangle, regardless of whether other points are inside it.
    private void findBiggestRectangle() {
        System.out.println("hello");
        long biggest=0;
        Point2d bestP1 = null;
        Point2d bestP2 = null;
        for(int i=0; i<points.size(); i++) {
            Point2d p1 = points.get(i);
            System.out.println(i);
            for(int j=i+1; j<points.size(); j++) {
                Point2d p2 = points.get(j);
                long w = 1+(long)Math.abs(p2.x - p1.x);
                long h = 1+(long)Math.abs(p2.y - p1.y);
                long area = w*h;
                //System.out.println("Rectangle between "+p1+" and "+p2+" has area "+area);
                if (area > biggest) {
                    biggest = area;
                    bestP1 = p1;
                    bestP2 = p2;
                }

            }
        }
        System.out.println("Biggest rectangle is between "+bestP1+" and "+bestP2+" with area "+biggest);
    }

    private void drawMap() {
        // allocate the map
        char[][] map = new char[topY+1][topX+1];
        // fill it with dots
        for (int y=0; y<=topY; y++) {
            for (int x=0; x<=topX; x++) {
                map[y][x] = '.';
            }
        }
        // plot the points
        for (Point2d point : points) {
            map[(int)point.y][(int)point.x] = '#';
        }
        // display it
        for (int y=0; y<=topY; y++) {
            for (int x = 0; x <= topX; x++) {
                System.out.print(map[y][x]);
            }
            System.out.println();
        }
    }

    private void part2() {

    }

    private List<LineSegment> getHorizontalScanLines(int y) {
        List<LineSegment> scanlinePoints = new java.util.ArrayList<>();
        List<Point2d> list = new java.util.ArrayList<>();
        for(Point2d p : points) {
            if (p.y == y) list.add(p);
        }
        if(list.size()>=2) {
            list.sort( (a,b) -> Double.compare(a.x, b.x));
            scanlinePoints.add(new LineSegment(list.getFirst(),list.getLast()));
        }
        return scanlinePoints;
    }
}