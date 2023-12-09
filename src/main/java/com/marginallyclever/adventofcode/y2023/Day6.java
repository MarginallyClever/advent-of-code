package com.marginallyclever.adventofcode.y2023;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Day6 {
    int [] times;
    int [] distances;
    int [] ways;
    long t2;
    long d2;

    public static void main(String[] args) {
        Day6 me = new Day6();
        me.processFile("input.txt");
        long t0 = System.nanoTime();
        me.findAnswer1();
        long t1 = System.nanoTime();
        me.findAnswer2();
        long t2 = System.nanoTime();
        System.out.println("time 1 = "+(t1-t0)/1000000+"ms");
        System.out.println("time 2 = "+(t2-t1)/1000000+"ms");
    }

    private void findAnswer2() {
        long start=0, end=t2;

        for(long j=1;j<t2;++j) {
            if(getTravel(t2,j)>d2) {
                start = j;
                break;
            }
        }
        for(long j=t2-1;j>start;--j) {
            if(getTravel(t2,j)>d2) {
                end = j+1;
                break;
            }
        }

        System.out.println("answer 2="+(end-start));
    }

    private void findAnswer1() {
        if(times.length!=distances.length) {
            System.out.println("ERROR: times and distances are not the same length.");
            return;
        }
        ways = new int[times.length];

        for(int i=0;i<times.length;++i) {
            int t = times[i];
            int d = distances[i];
            for(int j=1;j<t;++j) {
                if(getTravel(t,j)>d) ways[i]++;
            }
            System.out.println("way "+i+" = "+ways[i]);
        }

        int sum = 1;
        for(int i=0;i<times.length;++i) {
            if(ways[i]>0) sum *= ways[i];
        }
        System.out.println("answer 1 = "+sum);
    }

    /**
     * @param gameTime max game time
     * @param timeAccelerating number of seconds building up speed
     * @return
     */
    private long getTravel(long gameTime, long timeAccelerating) {
        long travelTime = gameTime- timeAccelerating;
        return travelTime * timeAccelerating;
    }

    private void processFile(String filename) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(Objects.requireNonNull(this.getClass().getResourceAsStream(filename)))))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processLine(String line) {
        if(line.trim().isEmpty()) return;

        if(line.startsWith("Time: ")) {
            line = line.substring(6);
            times = splitString(line);
            String changed = line.replace(" ","");
            System.out.println(changed);
            t2 = Long.parseLong(changed);
            System.out.println("t2 = "+t2);
        }
        if(line.startsWith("Distance: ")) {
            line = line.substring(10);
            distances = splitString(line);
            String changed = line.replace(" ","");
            System.out.println(changed);
            d2 = Long.parseLong(changed);
            System.out.println("d2 = "+d2);
        }
    }

    private int [] splitString(String str) {
        String [] list = str.trim().split("\\s+");
        int [] result = new int[list.length];
        for(int i=0;i<list.length;++i) {
            result[i] = Integer.parseInt(list[i]);
        }
        return result;
    }
}