package com.marginallyclever.adventofcode.y2023;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Day9 {
    class Sequence {
        public List<Integer> numbers = new ArrayList<>();

        public Sequence() {}

        public Sequence(String list) {
            String[] parts = list.split(" ");
            for(String part : parts) {
                numbers.add(Integer.parseInt(part));
            }
        }

        public boolean areDifferencesAllZero() {
            int prev = numbers.get(0);
            for(int i=1;i<numbers.size();++i) {
                int now = numbers.get(i);
                if(now-prev!=0) return false;
                prev = now;
            }
            return true;
        }

        public Sequence getSeqenceOfDifferences() {
            Sequence result = new Sequence();
            int prev = numbers.get(0);
            for(int i=1;i<numbers.size();++i) {
                int now = numbers.get(i);
                result.numbers.add(now-prev);
                prev = now;
            }
            return result;
        }
    }

    List<Sequence> sequences = new ArrayList<>();

    private void findAnswer2() {
    }

    private void findAnswer1() {
        int sum=0;
        for(Sequence s : sequences) {
            sum += getSequenceNext(s);
        }
        System.out.println("Answer 1 = "+sum);
    }

    private int getSequenceNext(Sequence s) {
        int d=0;
        if(!s.areDifferencesAllZero()) {
            Sequence diff = s.getSeqenceOfDifferences();
            d = getSequenceNext(diff);
        }

        return s.numbers.get(s.numbers.size()-1) + d;
    }

    private int getSequencePrev(Sequence s) {
        int d=0;
        if(!s.areDifferencesAllZero()) {
            Sequence diff = s.getSeqenceOfDifferences();
            d = getSequencePrev(diff);
        }
        return s.numbers.get(s.numbers.size()-1) + d;
    }

    private void processLine(String line) {
        if(line.trim().isEmpty()) return;
        sequences.add(new Sequence(line));
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
        Day9 me = new Day9();
        me.processFile("input.txt");
        long t0 = System.nanoTime();
        me.findAnswer1();
        long t1 = System.nanoTime();
        me.findAnswer2();
        long t2 = System.nanoTime();
        System.out.println("time 1 = "+(t1-t0)/1000000+"ms");
        System.out.println("time 2 = "+(t2-t1)/1000000+"ms");
    }
}