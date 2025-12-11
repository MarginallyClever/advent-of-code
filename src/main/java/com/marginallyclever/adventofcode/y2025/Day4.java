package com.marginallyclever.adventofcode.y2025;

import com.marginallyclever.adventofcode.AdventOfCode;
import org.jetbrains.annotations.NotNull;

import javax.vecmath.Vector2d;
import java.util.ArrayList;
import java.util.List;

public class Day4 extends AdventOfCode {
    public static void main(String[] args) {
        new Day4().run();
    }

    public static final char OCCUPIED = '@';
    public static final char EMPTY = '.';
    private long sum=0;
    private int width, height;
    private List<Character> grid = new ArrayList<>();
    private List<Vector2d> toRemove = new ArrayList<>();


    @Override
    public void processLine(String line) {
        width = line.length();
        for( Character c : line.toCharArray() ) {
            grid.add(c);
        }
    }

    @Override
    public void postProcess() {
        height = grid.size()/width;
        //part1();
        part2();
    }

    private void part2() {
        while(true) {
            part1();

            if(toRemove.isEmpty()) break;

            System.out.println("Removing "+toRemove.size());
            for( Vector2d v : toRemove ) {
                grid.set((int)(v.y*width+v.x), EMPTY);
            }
            toRemove.clear();
        }
    }

    private void part1() {
        // for each space in the grid,
        for(int y=0;y<height;y++) {
            for(int x=0;x<width;x++) {
                // is this an occupied space?
                if(get(x,y)==OCCUPIED) {
                    // count neighbors
                    scan(x,y);
                }
            }
        }
        System.out.println("Part 1: "+sum);
    }

    /**
     * Scan neighbors of given cell.  We want any occupied cell with less than 4 occupied neighbors.
     */
    private void scan(int x,int y) {
        int count=0;
        for(int a=-1;a<=1;a++) {
            for(int b=-1;b<=1;b++) {
                if( a==0 && b==0 ) continue;
                int nx = x+a;
                int ny = y+b;
                if( nx>=0 && nx<width && ny>=0 && ny<height && get(nx,ny)==OCCUPIED ) {
                    count++;
                }
            }
        }
        if(count<4) {
            sum++;
            toRemove.add(new Vector2d(x,y));
        }
    }

    private int get(int x,int y) {
        return grid.get(y*width+x);
    }
}
