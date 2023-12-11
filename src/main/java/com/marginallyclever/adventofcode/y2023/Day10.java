package com.marginallyclever.adventofcode.y2023;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Day10 {
    static class Pipe {
        char [] exits;

        public Pipe(char[] exits) {
            this.exits = exits;
        }

        public char getOtherExit(char exit) {
            for(char c : exits) {
                if(c!=exit) return c;
            }
            return 0;
        }

        public boolean hasExit(char s) {
            for(char c : exits) {
                if(c==s) return true;
            }
            return false;
        }
    }

    static class Tile {
        public char c;
        public int x,y;
        public int count=0;
        public char mark=' ';

        public Tile(char c, int x, int y) {
            this.c = c;
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "("+x+","+y+")\t"+c+"\t"+count;
        }
    }

    static class Point {
        int x,y;
        public Point(int x,int y) {
            this.x=x;
            this.y=y;
        }
    }

    private final List<Point> polygon = new ArrayList<>();

    private final Map<Character,Pipe> pipes = new HashMap<>();
    private Tile [][] tiles;
    private int startX, startY;
    private final char [] exits = {'N','E','S','W'};
    int maxSteps=0;

    private void findAnswer2() {
        List<Tile> toTest = new ArrayList<>();
        // find all tiles with count==0 except start
        for(int y=0;y<this.tiles.length;++y) {
            for(int x=0;x<this.tiles.length;++x) {
                if(this.tiles[y][x].count==0 && !(x==startX && y==startY)) {
                    toTest.add(this.tiles[y][x]);
                }
            }
        }

        int insideCount=0;
        int outsideCount=0;
        for(Tile t : toTest) {
            if(isInside(t.x,t.y)) {
                t.mark='I';
                insideCount++;
            } else {
                outsideCount++;
                t.mark='O';
            }
        }
        printTiles();
        System.out.println("insideCount = "+insideCount);
        System.out.println("outsideCount = "+outsideCount);
    }

    // test if point x,y is inside the closed loop of points in polygon.
    private boolean isInside(int x, int y) {
        // draw a line in one direction and count the number of times it crosses the polygon.
        // if the number of crossings is odd, the point is inside the polygon.

        boolean c = false;
        int i, j;
        for (i = 0, j = polygon.size()-1; i < polygon.size(); j = i++) {
            Point a = polygon.get(i);
            Point b = polygon.get(j);
            if( ((a.y > y) != (b.y > y)) &&
                    (x < (b.x - a.x) * (y - a.y) / (b.y - a.y) + a.x)){
                c = !c;
            }
        }
        return c;
    }

    private void printTiles() {
        for(int y=0;y<tiles.length;++y) {
            for(int x=0;x<tiles.length;++x) {
                if(tiles[y][x]==null) {
                    System.out.print(' ');
                    continue;
                }
                System.out.print(tiles[y][x].mark);
            }
            System.out.println();
        }
    }

    private char getMark(char direction, char exit) {
        switch(direction) {
            case 'N':
                if(exit=='E') return 'A';
                if(exit=='W') return 'B';
                break;
            case 'E':
                if(exit=='N') return 'B';
                if(exit=='S') return 'A';
                break;
            case 'S':
                if(exit=='E') return 'B';
                if(exit=='W') return 'A';
                break;
            case 'W':
                if(exit=='N') return 'A';
                if(exit=='S') return 'B';
                break;
        }
        return ' ';
    }

    private void findAnswer1() {
        Tile start = findStart();
        assert(start!=null);

        polygon.add(new Point(start.x,start.y));

        // if the pipe north connects to this pipe (has exit 'S') then go north
        Tile t;
        for(char exit : exits) {
            t = getPipeAt(start.x,start.y,exit);
            if(t==null || t.c=='.' || t.count!=0) continue;
            char reverse = getReverseDirection(exit);
            if(pipes.get(t.c).hasExit(reverse)) {
                t.count=1;
                polygon.add(new Point(t.x,t.y));
                System.out.print("Stepping "+exit+" ");
                followPipe(start,t,reverse);
            }
        }
    }

    // find tile with c=='S'
    private Tile findStart() {
        for(startY=0;startY<tiles.length;++startY) {
            for(startX=0;startX<tiles.length;++startX) {
                if(tiles[startY][startX].c=='S') {
                    System.out.println("start at "+tiles[startY][startX]);
                    return tiles[startY][startX];
                }
            }
        }
        return null;
    }

    private void followPipe(Tile start,Tile t,char entryFrom) {
        Tile next;
        do {
            if(t==null || t.c=='.') {
                break;
            }
            System.out.println(t);
            char exit = pipes.get(t.c).getOtherExit(entryFrom);
            System.out.print("Stepping "+exit+" ");
            next = getPipeAt(t.x, t.y, exit);
            if(next==null) {
                System.out.println("no pipe");
                break;
            }
            if(next==start) {
                maxSteps = t.count+1;
                System.out.println("found start. "+(maxSteps/2)+" half size");
                break;
            }
            next.count = t.count + 1;
            entryFrom = getReverseDirection(exit);
            t=next;
            polygon.add(new Point(t.x,t.y));
        } while(true);
    }

    private Tile getPipeAt(int x, int y, char exit) {
        return switch (exit) {
            case 'N' ->  findTile(x,y-1);
            case 'E' ->  findTile(x+1,y);
            case 'S' ->  findTile(x,y+1);
            case 'W' ->  findTile(x-1,y);
            default ->  null;
        };
    }

    private char getReverseDirection(char exit) {
        return switch (exit) {
            case 'N' -> 'S';
            case 'E' -> 'W';
            case 'S' -> 'N';
            case 'W' -> 'E';
            default -> 0;
        };
    }

    private Tile findTile(int x,int y) {
        if(x<0 || y<0 || x>=tiles.length || y>=tiles.length) return null;
        return tiles[y][x];
    }

    boolean firstLine=true;
    int yy=0;
    private void processLine(String line) {
        if(line.trim().isEmpty()) return;
        if(firstLine) {
            firstLine = false;
            int size = line.length();
            tiles = new Tile[size][];
            for(int i=0;i<size;++i) {
                tiles[i] = new Tile[size];
            }
        }
        int x=0;
        for(char c : line.toCharArray()) {
            tiles[yy][x]=new Tile(c,x,yy);
            x++;
        }
        yy++;
    }

    private void processFile() {
        pipes.put('|',new Pipe(new char[]{'N','S'}));
        pipes.put('-',new Pipe(new char[]{'E','W'}));
        pipes.put('7',new Pipe(new char[]{'W','S'}));
        pipes.put('L',new Pipe(new char[]{'N','E'}));
        pipes.put('F',new Pipe(new char[]{'S','E'}));
        pipes.put('J',new Pipe(new char[]{'W','N'}));

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
        Day10 me = new Day10();
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