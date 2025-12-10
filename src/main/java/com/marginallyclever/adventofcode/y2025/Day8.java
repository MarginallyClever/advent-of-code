package com.marginallyclever.adventofcode.y2025;

import com.marginallyclever.adventofcode.AdventOfCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day8 extends AdventOfCode {
    public static void main(String[] args) {
        new Day8();
    }

    int sum=0;

    // the list of points that may or may not be connected.
    public static class Point {
        int x,y,z;
        int network = 0;

        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double distance(Point other) {
            int dx = other.x - this.x;
            int dy = other.y - this.y;
            int dz = other.z - this.z;
            return Math.sqrt(dx*dx + dy*dy + dz*dz);
        }
    }

    List<Point> points;

    static public class PointPair {
        int i;
        List<Integer> js = new ArrayList<>();

        public PointPair(int i, int j) {
            this.i = i;
            this.js.add(j);
        }
        public void add(int j) {
            this.js.add(j);
        }
    }

    List<Long> pointPairs;

    int biggestNetworkID;

    @Override
    public void processLine(String line) {
        if(points==null) {
            points = new ArrayList<>();
            pointPairs = new ArrayList<>();
            biggestNetworkID=1;
        }
        String[] tokens = line.split(",");
        Point point = new Point(
                Integer.parseInt(tokens[0]),
                Integer.parseInt(tokens[1]),
                Integer.parseInt(tokens[2])
        );
        points.add(point);
    }

    @Override
    public void postProcess() {
        buildNetworks(1000);

        // find the size of each network
        System.out.println("Counting");
        int [] networkCounts = new int[biggestNetworkID];
        for(Point p : points) {
            if(p.network>0) {
                networkCounts[p.network]++;
            }
        }
        // sort network counts descending
        System.out.println("Sorting");
        List<Integer> countsList = new ArrayList<>();
        for(int count : networkCounts) {
            countsList.add(count);
        }
        countsList.sort(Collections.reverseOrder());

        String add="";
        for(int count : countsList) {
            if(count==0) break;
            System.out.print(add+count);
            add=", ";
        }
        System.out.println();
        sum = countsList.get(0) * countsList.get(1) * countsList.get(2);
        System.out.println("result="+sum);
    }

    private void buildNetworks(int maxIterations) {
        for(int count=0;count<maxIterations;++count) {
            int[] bestPair = getNearestPair();
            int bestI = bestPair[0];
            int bestJ = bestPair[1];
            Point pointI = points.get(bestI);
            Point pointJ = points.get(bestJ);
            //System.out.println("found closest pair: "+pointI.x+","+pointI.y+","+pointI.z+" (net "+pointI.network+") and "+pointJ.x+","+pointJ.y+","+pointJ.z+" (net "+pointJ.network+") dist="+pointI.distance(pointJ));
            // record the connection between this pair of points

            addPointPair(bestI,bestJ);

            // determine network IDs
            if(pointI.network>0 && pointJ.network>0) {
                // same network?
                if(pointI.network != pointJ.network) {
                    // merge two different networks
                    int smallestID = Math.min(pointI.network, pointJ.network);
                    int biggestID = Math.max(pointI.network, pointJ.network);
                    // two networks are merging.  change all points with biggestID to smallestID
                    System.out.println("Merging network " + smallestID + " into " + biggestID);
                    for (Point p : points) {
                        if (p.network == smallestID) {
                            p.network = biggestID;
                        }
                    }
                }
            } else if(pointI.network>0) {
                System.out.println("Adding point J to network " + pointI.network);
                pointJ.network = pointI.network;
            } else if(pointJ.network>0) {
                System.out.println("Adding point I to network " + pointJ.network);
                pointI.network = pointJ.network;
            } else {
                System.out.println("Creating network " + biggestNetworkID);
                pointI.network = biggestNetworkID;
                pointJ.network = biggestNetworkID;
                biggestNetworkID++;
            }
        }
    }

    // Find the nearest two points by linear distance that are not already connected.
    int [] getNearestPair() {
        double bestDistance = Double.MAX_VALUE;
        int bestI=0;
        int bestJ=0;

        for(int i=0;i<points.size();++i) {
            Point pointA = points.get(i);
            for(int j=i+1;j<points.size();++j) {
                if(connectionExists(i,j)) continue;
                Point pointB = points.get(j);
                // already in the same network?
//                if(pointA.network!=0 && pointA.network==pointB.network) continue;

                double distance = pointA.distance(pointB);
                if (distance < bestDistance) {
                    bestDistance = (int) distance;
                    //System.out.println("New best distance: " + bestDistance+"\t"+i+"\t"+j);
                    bestI = i;
                    bestJ = j;
                }
            }
        }
        return new int[]{bestI,bestJ};
    }

    private Long getPointPairKey(int a, int b) {
        int smallest = Math.min(a,b);
        int biggest = Math.max(a,b);
        return (((long)smallest)<<32) | ((long)biggest);
    }

    private void addPointPair(int a, int b) {
        Long pairKey = getPointPairKey(a,b);
        if(pointPairs.contains(pairKey)) {
            throw new RuntimeException("Point pair already exists: "+a+","+b);
        }
        pointPairs.add(pairKey);
    }

    private boolean connectionExists(int a, int b) {
        Long pairKey = getPointPairKey(a,b);
        return pointPairs.contains(pairKey);
    }
}

// 27195 too low