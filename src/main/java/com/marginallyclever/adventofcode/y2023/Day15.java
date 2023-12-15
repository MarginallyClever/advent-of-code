package com.marginallyclever.adventofcode.y2023;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Day15 {
    private final List<String> allParts = new ArrayList<>();

    private void findAnswer1() {
        int sum=0;
        for(String s : allParts) {
            int hash = getHash(s);
            sum += hash;
            System.out.println(s + " = " + hash);
        }
        System.out.println("sum = "+sum);
    }

    private int getHash(String s) {
        int hash = 0;
        for(char c : s.toCharArray()) {
            hash += c;
            hash = (hash * 17) & 0xFF;
        }
        return hash;
    }

    static class LensBox {
        public List<String> slots = new ArrayList<>();
    }
    private final LensBox[] hashmap = new LensBox[256];

    private void findAnswer2() {
        for(int i=0;i<256;++i) {
            hashmap[i] = new LensBox();
        }

        for(String s : allParts) {
            String [] parts = s.split("[=-]");
            int hash = getHash(parts[0]);
            if(s.contains("-")) {
                removeLens(hash,parts[0]);
            } else {
                addLens(hash,parts[0],parts[1]);
            }
        }

        int sum=0;
        for(int i=0;i<256;++i) {
            if(!hashmap[i].slots.isEmpty()) {
                int score = 0;
                System.out.print("Box "+i+":");
                int j=1;
                for(String s : hashmap[i].slots) {
                    System.out.print(" ["+s+"]");
                    score = (i+1) * j * Integer.parseInt(s.substring(s.indexOf(' ')+1));
                    j++;
                    System.out.print("("+score+")");
                    sum += score;
                }
                System.out.println();
            }
        }
        System.out.println("sum = "+sum);
    }

    private void addLens(int hash, String label, String value) {
        LensBox b = hashmap[hash];
        // find match to replace
        for(int i=0;i<b.slots.size();++i) {
            if(b.slots.get(i).startsWith(label)) {
                b.slots.set(i,label+" "+value);
                return;
            }
        }
        // none found
        b.slots.add(label+" "+value);
    }

    private void removeLens(int hash, String label) {
        LensBox b = hashmap[hash];
        for(int i=0;i<b.slots.size();++i) {
            if(b.slots.get(i).startsWith(label)) {
                b.slots.remove(i);
                return;
            }
        }
    }

    private void processLine(String line) {
        if(line.trim().isEmpty()) return;

        String [] parts = line.split(",");
        allParts.addAll(List.of(parts));
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
        Day15 me = new Day15();
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