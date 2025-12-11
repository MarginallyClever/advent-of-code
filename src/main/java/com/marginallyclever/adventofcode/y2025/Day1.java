package com.marginallyclever.adventofcode.y2025;

import com.marginallyclever.adventofcode.AdventOfCode;

public class Day1 extends AdventOfCode {
    public static void main(String[] args) {
        new Day1().run();
    }

    int dialPosition = 50;
    int sum=0;

    @Override
    public void processLine(String line) {
        int direction = line.charAt(0)=='L'?-1:1;
        int steps = Integer.parseInt(line.substring(1));
        //part1(direction,steps);
        part2(direction,steps);
        System.out.println("Total times dial hit 0: "+sum);
    }

    private void part1(int direction,int steps) {
        dialPosition += direction * steps;
        checkForZero();
        System.out.println("Dial position: "+dialPosition);
    }

    private void part2(int direction, int steps) {
        while(steps>0) {
            steps--;
            dialPosition += direction;
            checkForZero();
        }
        System.out.println("Dial position: "+dialPosition);
    }

    private void checkForZero() {
        dialPosition = (dialPosition + 100) % 100;
        if(dialPosition == 0) {
            sum++;
        }
    }
}
