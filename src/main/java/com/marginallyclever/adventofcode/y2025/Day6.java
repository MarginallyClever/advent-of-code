package com.marginallyclever.adventofcode.y2025;

import com.marginallyclever.adventofcode.AdventOfCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Day 6, part 2
 */
public class Day6 extends AdventOfCode {
    public static void main(String[] args) {
        new Day6().run();
    }

    private List<String> allLines = new ArrayList<>();

    // Helper class to hold values for a series
    public static class Values {
        public List<Long> numbers = new ArrayList<>();

        public Values(long first) {
            add(first);
        }

        public void add(long n) {
            numbers.add(n);
        }

        public long addAll() {
            long total = 0;
            for(Long n : numbers) {
                total += n;
            }
            return total;
        }

        public long multiplyAll() {
            long total = 1;
            for(Long n : numbers) {
                total *= n;
            }
            return total;
        }
    }

    private List<Values> series = new ArrayList<>();

    @Override
    public void processLine(String line) {
        allLines.add(line);
    }

    @Override
    public void postProcess() {
        //part1();
        part2();
    }

    private void part1() {
        long sum=0;

        for(String line : allLines) {
            // split part by whitespace
            String[] parts = line.trim().split("\\s+");
            // first line?
            if (series.isEmpty()) {
                // initialize series with first values
                for (String part : parts) {
                    series.add(new Values(Long.parseLong(part)));
                }
            } else {
                for (int i = 0; i < parts.length; i++) {
                    String asLong = parts[i].trim();
                    // check for + or *
                    if (asLong.startsWith("+")) {
                        sum += series.get(i).addAll();
                    } else if (asLong.startsWith("*")) {
                        sum += series.get(i).multiplyAll();
                    } else {
                        // just a number, add to series
                        series.get(i).add(Long.parseLong(asLong));
                    }
                }
            }
        }
        System.out.println("Sum: " + sum);
    }

    private void part2() {
        boolean isAdd=true;
        long total=0;
        long subTotal=0;
        long value;

        List<String> newLines = flipAllRowsAndCols();
        for(String line : newLines) {
            line = line.trim();
            System.out.println(line);
            // if the line is blank, new operation.
            if(line.isEmpty()) {
                total+=subTotal;
                subTotal=0;
                continue;
            }
            // if line ends with + or *, start a new operation.
            if(line.endsWith("+")) {
                isAdd=true;
                line = line.substring(0, line.length()-1);
                subTotal = Long.parseLong(line.trim());
                continue;
            }
            if(line.endsWith("*")) {
                isAdd=false;
                line = line.substring(0, line.length()-1);
                subTotal = Long.parseLong(line.trim());
                continue;
            }
            // otherwise, just a number to add or multiply
            value = Long.parseLong(line);
            if(isAdd) subTotal += value;
            else subTotal *= value;
        }
        // if there's any remaining subtotal, add it in.
        total+=subTotal;

        System.out.println("Sum: " + total);
    }

    // flip every row and column in allLines.
    private List<String> flipAllRowsAndCols() {
        int rowCount = allLines.size();
        // colCount is the length of the longest line
        int colCount = 0;
        for(String line : allLines) {
            colCount = Math.max(colCount,line.length());
        }

        // swap the rows and columns
        List<String> newLines = new ArrayList<>();
        for(int c=0; c<colCount; c++) {
            StringBuilder newRow = new StringBuilder();
            for(int r=0; r<rowCount; r++) {
                String line = allLines.get(r);
                if(line.length()>c) {
                    newRow.append(line.charAt(c));
                }
            }
            newLines.add(c, newRow.toString());
        }
        return newLines;
    }
}
