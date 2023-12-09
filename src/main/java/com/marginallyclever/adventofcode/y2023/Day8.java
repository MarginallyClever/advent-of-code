package com.marginallyclever.adventofcode.y2023;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Day8 {
    static class Node {
        public String name,left,right;

        public Node(String line) {
            line = line.replace("(","");
            line = line.replace(")","");
            line = line.replace(",","");
            line = line.replace("=","");
            line = line.replace("  "," ");
            String [] parts = line.split(" ");
            name = parts[0];
            left = parts[1];
            right = parts[2];
        }
    };

    private final Map<String,Node> graph = new HashMap<>();
    int lineCount = 0;
    String pattern;

    class Walker {
        public String pos;
        public int steps = 0;
        public Walker(String pos) {
            this.pos = pos;
        }

        public void step(char c) {
            try {
                pos = switch (c) {
                    case 'L' -> graph.get(pos).left;
                    case 'R' -> graph.get(pos).right;
                    default -> throw new RuntimeException("Unknown direction " + c);
                };
            } catch (NullPointerException e) {
                System.out.println("pos = " + pos);
                throw e;
            }
        }
    }

    // Function to find the GCD of two numbers
    public long gcd(long a, long b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    // Function to find the LCM of an array of numbers
    public long findLCM(int[] arr) {
        long lcm = arr[0];
        for (int i = 1; i < arr.length; i++) {
            long currentNumber = arr[i];
            lcm = (lcm * currentNumber) / gcd(lcm, currentNumber);
        }
        return lcm;
    }

    private void findAnswer2() {
        List<Walker> walkers = new ArrayList<Walker>();

        for(String key : graph.keySet()) {
            if(key.charAt(2)=='A') {
                System.out.println("Adding walker at "+key);
                Walker w = new Walker(key);
                walkers.add(w);

                while(!w.pos.endsWith("Z")) {
                    int i = w.steps++ % pattern.length();
                    w.step(pattern.charAt(i));
                }
            }
        }

        // lowest common multiple of all the steps
        int [] steps = new int[walkers.size()];
        for(int i=0;i<walkers.size();++i) {
            steps[i] = walkers.get(i).steps;
        }
        long lcm = findLCM(steps);


        System.out.println("Answer 2 = "+lcm +" steps");
    }

    private int walkersAtZ(List<Walker> walkers) {
        int count = 0;
        for(Walker w : walkers) {
            if(w.pos.endsWith("Z")) count++;
        }
        return count;
    }

    private void findAnswer1() {
        Walker w = new Walker("AAA");

        int steps = 0;
        while(!w.pos.equals("ZZZ")) {
            int i = steps++ % pattern.length();
            w.step(pattern.charAt(i));
        }
        System.out.println("Answer 1 = "+steps+" steps");
    }

    private void processLine(String line) {
        if(line.trim().isEmpty()) return;
        if(lineCount==0) {
            pattern = line;
            lineCount++;
            return;
        }
        Node n = new Node(line);
        graph.put(n.name,n);
        lineCount++;
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

    public static void main(String[] args) {
        Day8 me = new Day8();
        me.processFile("input.txt");
        long t0 = System.nanoTime();
        //me.findAnswer1();
        long t1 = System.nanoTime();
        me.findAnswer2();
        long t2 = System.nanoTime();
        System.out.println("time 1 = "+(t1-t0)/1000000+"ms");
        System.out.println("time 2 = "+(t2-t1)/1000000+"ms");
    }
}