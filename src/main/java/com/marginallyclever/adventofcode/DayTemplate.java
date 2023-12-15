package com.marginallyclever.adventofcode;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class DayTemplate {
    private void findAnswer1() {
    }

    private void findAnswer2() {
    }

    private void processLine(String line) {
        if(line.trim().isEmpty()) return;
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
        DayTemplate me = new DayTemplate();
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