package com.marginallyclever.adventofcode.y2023;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Day11 {
    public static class Star {
        public int x,y;
        public Star(int x, int y) {
            this.x=x;
            this.y=y;
        }

        @Override
        public String toString() {
            return "("+x+","+y+")";
        }
    }
    private char [][] sky;
    List<Star> stars = new ArrayList<>();
    List<Star> starsOriginal = new ArrayList<>();

    private void findAnswer2() {
        stars.clear();
        for(Star p : starsOriginal) {
            stars.add(new Star(p.x,p.y));
        }
        expandSky(999999);
        sumDistances();
    }

    private void findAnswer1() {
        stars.clear();
        for (Star p : starsOriginal) {
            stars.add(new Star(p.x, p.y));
        }

        System.out.println("before");
        printAllStars();
        expandSky(1);
        System.out.println("after");
        printAllStars();

        sumDistances();
    }

    private void sumDistances() {
        int c = stars.size();
        int pairs = c*(c-1)/2;
        long sum = 0;
        System.out.println("pairs="+pairs);
        for(int i=0;i<c;++i) {
            Star a = stars.get(i);
            for(int j=i+1;j<c;++j) {
                Star b = stars.get(j);
                // get distance between a and b.
                long dx = Math.abs(a.x-b.x);
                long dy = Math.abs(a.y-b.y);
                long d = dx+dy;
                System.out.println(""+(i+1)+a+" - "+(j+1)+b+"="+d);
                sum+=d;
            }
        }
        System.out.println("sum="+sum);
    }

    private void printAllStars() {
        int i=0;
        for(Star p : stars) {
            System.out.println(i+"="+p);
            i++;
        }
    }

    private void expandSky(int amount) {
        // if any row in the sky is all '.' then add 1 to all stars below it.
        List<Integer> empties = new ArrayList<>();

        for(int y=0;y<sky[0].length;++y) {
            boolean notEmpty = false;
            for (int x = 0; x < sky.length; ++x) {
                if (sky[y][x] != '.') {
                    notEmpty = true;
                    break;
                }
            }
            if(!notEmpty) {
                empties.add(y);
            }
        }

        System.out.println("adding rows");
        for(Star p : stars) {
            int count=0;
            for(int y : empties) {
                if(p.y>y) count++;
            }
            p.y += amount * count;
        }
        empties.clear();

        // if any column in the sky is all '.' then add 1 to all stars to the right of it.
        for(int x=0;x<sky.length;++x) {
            boolean notEmpty = false;
            for (int y = 0; y < sky[0].length; ++y) {
                if (sky[y][x] != '.') {
                    notEmpty = true;
                    break;  // not empty
                }
            }
            if (notEmpty) continue;
            empties.add(x);
        }

        System.out.println("adding columns");
        for(Star p : stars) {
            int count=0;
            for(int x : empties) {
                if(p.x>x) count++;
            }
            p.x+=amount * count;
        }
    }

    int linesRun = 0;
    private void processLine(String line) {
        if(line.trim().isEmpty()) return;
        if(linesRun==0) {
            sky = new char[line.length()][];
            for(int i=0;i<line.length();++i) {
                sky[i] = new char[line.length()];
            }
        }
        for(int i=0;i<line.length();++i) {
            char c = line.charAt(i);
            sky[linesRun][i] = c;
            if(c!='.') starsOriginal.add(new Star(i, linesRun));
        }
        linesRun++;
    }

    private void processFile() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(Objects.requireNonNull(this.getClass().getResourceAsStream("input.txt")))))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Day11 me = new Day11();
        me.processFile();
        long t0 = System.nanoTime();
        me.findAnswer1();
        long t1 = System.nanoTime();
        me.findAnswer2();
        long t2 = System.nanoTime();
        System.out.println("time 1 = "+(t1-t0)/1000000+"ms");
        System.out.println("time 2 = "+(t2-t1)/1000000+"ms");
    }
}