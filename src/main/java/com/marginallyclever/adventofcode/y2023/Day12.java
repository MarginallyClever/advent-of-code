package com.marginallyclever.adventofcode.y2023;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day12 {
    class Rule {
        String pattern;
        int [] result;

        public Rule(String line) {
            String [] parts = line.split(" ");
            pattern = parts[0];
            String [] subParts = parts[1].split(",");
            result = new int[subParts.length];
            for(int i=0;i<subParts.length;++i) {
                result[i] = Integer.parseInt(subParts[i]);
            }
        }

        // pattern has wild card '?' characters.  replace each with either '.' or '#'.
        // if the pattern matches, add 1 to sum.
        public void testRecursive(int index, String match) {
            if(!testPartialMatch(match)) return;

            char c = pattern.charAt(index);
            if(c=='?') {
                testRecursive(index+1,match+"#");
                testRecursive(index+1,match+".");
            } else {
                testRecursive(index+1,match+c);
            }
        }

        private boolean testPartialMatch(String match) {
            String mid = match + pattern.substring(match.length());
            //System.out.println("  Comparing "+mid+" to "+ Arrays.toString(result));
            boolean contains = mid.contains("?");

            String [] chunks = Arrays.stream(mid.split("[\\.\\?]"))
                    .filter(str -> !str.isEmpty())
                    .toArray(String[]::new);
            int c = Math.min(chunks.length,result.length);
            for(int i=0;i<c;++i) {
                if(chunks[i].length()!=result[i]) {
                    //System.out.println("    Failed "+chunks[i]);
                    return contains;
                }
            }

            if(chunks.length==result.length && !contains) {
                //System.out.println("    Matched "+mid+" aka "+Arrays.toString(chunks)+" to "+Arrays.toString(result));
                sum++;
            }
            return contains;
        }
    }

    private final List<Rule> rules = new ArrayList<>();
    private int sum = 0;

    private void findAnswer1() {
        for(Rule rule : rules) {
            System.out.println("Testing "+rule.pattern);
            rule.testRecursive(0,"");
        }
        System.out.println("sum = "+sum);
    }

    private void findAnswer2() {
        sum=0;
        System.out.println("Unfolding");
        for(Rule rule : rules) {
            String p = rule.pattern;
            rule.pattern = p+"?"+p+"?"+p+"?"+p+"?"+p;
            int [] r = rule.result;
            rule.result = new int[r.length*5];
            for(int i=0;i<5;++i) {
                System.arraycopy(r,0,rule.result,i*r.length,r.length);
            }
        }
        findAnswer1();
    }

    private void processLine(String line) {
        if(line.trim().isEmpty()) return;

        rules.add(new Rule(line));
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
        Day12 me = new Day12();
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