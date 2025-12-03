package com.marginallyclever.adventofcode.y2025;

import com.marginallyclever.adventofcode.AdventOfCode;
import org.jetbrains.annotations.NotNull;

public class Day3 extends AdventOfCode {
    public static void main(String[] args) {
        new Day3();
    }

    long sum=0;

    @Override
    public void processLine(String line) {
        //part1(line);
        part2(line);
    }

    private void part2(@NotNull String line) {
        System.out.println("before: "+line);

        while(line.length()>12) {
            int length = line.length();
            for (int i = 0; i < length-1; ++i) {
                if(line.charAt(i) < line.charAt(i+1)) {
                    line = line.substring(0,i) + line.substring(i+1);
                    break;
                }
            }
            if(length == line.length()) {
                // the last char is the smallest
                line = line.substring(0,length-1);
            }
        }
        sum += Long.parseLong(line);
        System.out.println("Remaining: "+line+". Current sum: "+sum);
    }

    private void part1(@NotNull String line) {
        int biggest=0;
        for(int i=0; i<line.length()-1; i++) {
            int first = (line.charAt(i)-'0')*10;

            for(int j=i+1; j<line.length(); j++) {
                int second = line.charAt(j) - '0';

                var value = first + second;
                if(value > biggest) {
                    biggest = value;
                }
            }
        }
        sum += biggest;
        System.out.println("Biggest="+biggest+". Current sum: "+sum);
    }
}
