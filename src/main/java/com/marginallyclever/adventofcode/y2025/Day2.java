package com.marginallyclever.adventofcode.y2025;

import com.marginallyclever.adventofcode.AdventOfCode;
import org.jetbrains.annotations.NotNull;

public class Day2 extends AdventOfCode {
    public static void main(String[] args) {
        new Day2();
    }

    long sum=0;

    @Override
    public void processLine(String line) {
        String [] parts = line.split(",");
        for(String part : parts) {
            // we can assume it contains a hyphen
            String[] rangeParts = part.split("-");
            // count the number of digits in rangeParts[0];
            long start = Long.parseLong(rangeParts[0]);
            long end = Long.parseLong(rangeParts[1]);

            for (long i = start; i <= end; i++) {
                testNumber(i,part);
            }
        }
        System.out.println("Current sum: "+sum);
    }

    private void testNumber(long i, String part) {
        String asString = Long.toString(i);
        int digitCount = asString.length();
        //part1(i,asString,digitCount,part);
        part2(i,asString,digitCount,part);
    }

    private void part1(long i, @NotNull String asString, int digitCount, String part) {
        int half = digitCount/2;
        String firstHalf = asString.substring(0, half);
        String secondHalf = asString.substring(half);
        if(firstHalf.equals(secondHalf)) {
            System.out.println("found "+i+" for part: "+part);
            sum += i;
        }
    }

    private void part2(long i, @NotNull String asString, int digitCount, String part) {
        for(int len=1;len<=digitCount/2;len++) {
            String firstPart = asString.substring(0, len);
            int start;
            for (start = len; start < digitCount; start+=len) {
                String middlePart = asString.substring(start, Math.min(start + len, digitCount));
                if (!firstPart.equals(middlePart)) {
                    // no match
                    break;
                }
            }
            if(start>=digitCount) {
                // if we got here then the middle parts all matched the first part.
                System.out.println("found "+i+" for part: "+part+" with length "+len);
                sum += i;
                return;
            }
        }
    }
}
