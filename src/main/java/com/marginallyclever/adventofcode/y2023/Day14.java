package com.marginallyclever.adventofcode.y2023;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.marginallyclever.adventofcode.y2022.Day8.printGrid;

public class Day14 {
    List<Integer> weights = new ArrayList<>();
    char [][] grid;


    private void findAnswer1() {
        printGrid();
        while(roll(0,-1));
        scoreGrid();
        printGrid();
        System.out.println("sum = "+weights.getFirst());
    }

    private void scoreGrid() {
        int sum = 0;
        for(int y=0;y<grid.length;++y) {
            for(int x=0;x<grid[y].length;++x) {
                if(grid[y][x]=='O') {
                    sum += grid.length-y;
                }
            }
        }
        weights.add(sum);
    }

    private void printGrid() {
        for (char[] chars : grid) {
            for (char aChar : chars) {
                System.out.print(aChar);
            }
            System.out.println();
        }
        System.out.println();
    }

    private boolean roll(int dx, int dy) {
        boolean moved = false;
        for(int y=0;y<grid.length;++y) {
            for(int x=0;x<grid[y].length;++x) {
                if(grid[y][x]=='O') {
                    if(canMove(x+dx,y+dy)) {
                        grid[y][x]='.';
                        grid[y+dy][x+dx]='O';
                        moved = true;
                    }
                }
            }
        }
        return moved;
    }

    private boolean canMove(int x, int y) {
        if(x<0 || x>=grid[0].length) return false;
        if(y<0 || y>=grid.length) return false;
        return grid[y][x]=='.';
    }

    private void findAnswer2() {
        weights.clear();
        while (roll(-1, 0)) ;
        while (roll(0, 1)) ;
        while (roll(1, 0)) ;
        scoreGrid();
        for (int i = 1; i < 300; ++i) {
            while (roll(0, -1)) ;
            while (roll(-1, 0)) ;
            while (roll(0, 1)) ;
            while (roll(1, 0)) ;
            scoreGrid();
        }

        List<Integer> pattern = getPattern();
        System.out.println("weights length = "+weights.size());
        System.out.println("pattern length = "+pattern.size());

        int valToFetch = ((1000000000) % pattern.size()) - 1;
        if (valToFetch < 0) {
            valToFetch = pattern.size() - 1;
        }
        System.out.println("answer 2 = "+pattern.get(valToFetch));
    }

    private List<Integer> getPattern() {
        // find where the pattern of weights repeats
        int slow=0, fast=0;
        boolean found = false;
        int a = weights.getFirst();
        while( fast+5 < weights.size()) {
            slow++;
            fast+=2;
            a = weights.get(slow);
            int b = weights.get(fast);
            if(a == b) {
                found = true;
                break;
            }
        }
        if(!found) throw new RuntimeException("No repeat found");
        System.out.println("Found repeat at "+slow+" "+fast);

        int start = slow;
        int c;
        do {
            c = weights.get(++slow);
        } while( c != a );

        List<Integer> pattern = new ArrayList<>();
        for(int i=start;i<slow;++i) {
            pattern.add(weights.get(i));
        }
        return pattern;
    }

    int lineCount = 0;
    private void processLine(String line) {
        if(line.trim().isEmpty()) return;
        if(lineCount ==0) {
            grid = new char[line.length()][line.length()];
        }
        for(int i=0;i<line.length();++i) {
            grid[lineCount][i] = line.charAt(i);
        }
        lineCount++;
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
        Day14 me = new Day14();
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