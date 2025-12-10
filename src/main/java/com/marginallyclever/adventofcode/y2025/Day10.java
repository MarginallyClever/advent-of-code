package com.marginallyclever.adventofcode.y2025;

import com.marginallyclever.adventofcode.AdventOfCode;

import java.util.ArrayList;
import java.util.List;

public class Day10 extends AdventOfCode {
    public static void main(String[] args) {
        new Day10();
    }

    long sum=0;

    public static class Machine {
        int lights;  // current state of machine
        int target;
        List<Integer> buttons = new ArrayList<>();

        public Machine(int target) {
            this.lights = 0;
            this.target = target;
        }
        public void addButton(int button) {
            buttons.add(button);
        }
    }

    @Override
    public void processLine(String line) {
        int a = line.indexOf("]");
        int b = line.indexOf("{");
        String part1 = line.substring(1,a);
        String part3 = line.substring(b+1,line.length()-1);
        String part2 = line.substring(a+1,b-1).trim();
        //System.out.println("Part 1: "+part1);
        //System.out.println("Part 2: "+part2);
        //System.out.println("Part 3: "+part3);

        // convert part1 to bitmask for target
        int target = 0;
        for(int i=0;i<part1.length();i++) {
            if(part1.charAt(i)=='#') {
                target |= 1 << i;
            }
        }
        //System.out.println("Target bitmask: "+Integer.toBinaryString(target));
        Machine machine = new Machine(target);

        // buttons are in format (a,b,c) (d,e,f) ...
        // where each letter is a light index affected by that button.  these can be converted to bitmasks.
        // split part2 on spaces to get buttons
        String[] buttons = part2.split(" ");
        for (String button : buttons) {
            // split on commas inside the parentheses
            String [] affectedLight = button.substring(1,button.length()-1).split(",");
            // convert to bitmask
            int bits = 0;
            for(String light : affectedLight) {
                bits |= 1 << Integer.parseInt(light);
            }
            // add button to machine
            machine.addButton(bits);
        }

        long presses = findMinimumPresses(machine);
        sum += presses;
    }

    private int findMinimumPresses(Machine machine) {
        // Now we have the machine with target and buttons.  We need to find the minimum number of button presses to reach the target from 0.
        // We can use a breadth-first search to find the shortest path to the target.
        List<Integer> queue = new ArrayList<>();
        List<Integer> visited = new ArrayList<>();
        // start with initial state
        queue.add(machine.lights);
        // mark initial state as visited
        visited.add(machine.lights);
        int steps = 0;
        boolean found = false;
        while(!queue.isEmpty() && !found) {
            // process current level
            List<Integer> nextQueue = new ArrayList<>();
            for(int state : queue) {
                if(state == machine.target) {
                    found = true;
                    break;
                }
                // try pressing each button
                for(int button : machine.buttons) {
                    int newState = state ^ button; // toggle lights
                    if(!visited.contains(newState)) {
                        // mark new state as visited
                        visited.add(newState);
                        // add new state to next level queue
                        nextQueue.add(newState);
                    }
                }
            }
            queue = nextQueue;
            steps++;
        }

        steps--;
        // Output result
        if(found) {
            System.out.println("Minimum button presses to reach target: " + steps);
        } else {
            System.out.println("Target not reachable");
            steps = 0;
        }
        return steps;
    }

    @Override
    public void postProcess() {
        System.out.println("Sum of minimum button presses: " + sum);
    }
}
