package com.marginallyclever.adventofcode.y2025;

import com.marginallyclever.adventofcode.AdventOfCode;

import java.util.ArrayList;
import java.util.List;

public class Day10 extends AdventOfCode {
    public static void main(String[] args) {
        new Day10().run();
    }

    long sum=0;
    int linesFinished=0;

    public static class Machine {
        List<Integer> buttons = new ArrayList<>();

        public Machine() {}

        public void addButton(int button) {
            buttons.add(button);
        }
    }

    @Override
    public void processLine(String line) {
        int a = line.indexOf("]");
        int b = line.indexOf("{");
        String section1 = line.substring(1, a);
        String section3 = line.substring(b + 1, line.length() - 1);
        String section2 = line.substring(a + 1, b - 1).trim();
        //System.out.println("Section 1: " + section1);
        //System.out.println("Section 2: " + section2);
        //System.out.println("Section 3: " + section3);

        //System.out.println("Target bitmask: "+Integer.toBinaryString(target));
        Machine machine = new Machine();

        // buttons are in format (a,b,c) (d,e,f) ...
        // where each letter is a light index affected by that button.  these can be converted to bitmasks.
        // split section2 on spaces to get buttons
        String[] buttons = section2.split(" ");
        for (String button : buttons) {
            // split on commas inside the parentheses
            String[] affectedLight = button.substring(1, button.length() - 1).split(",");
            // convert to bitmask
            int bits = 0;
            for (String light : affectedLight) {
                bits |= 1 << Integer.parseInt(light);
            }
            // add button to machine
            machine.addButton(bits);
        }

        //part1(machine,section1);
        part2(machine, section3);
        System.out.println("Finished line " + (++linesFinished));
    }

    private void part1(Machine machine,String section1) {
        // convert section1 to bitmask for target
        int target = 0;
        for (int i = 0; i < section1.length(); i++) {
            if (section1.charAt(i) == '#') {
                target |= 1 << i;
            }
        }

        long presses = findMinimumPressesForLights(machine,target);
        sum += presses;
    }

    /**
     * We have the machine with target and buttons.
     * We need to find the minimum number of button presses to reach the target from 0.
     * We can use a breadth-first search to find the shortest path to the target.
     * @param machine the machine to solve
     * @param target the target bitmask
     * @return the minimum number of button presses to reach the target
     */
    private int findMinimumPressesForLights(Machine machine,int target) {
        List<Integer> queue = new ArrayList<>();
        List<Integer> visited = new ArrayList<>();
        // start with initial state
        queue.add(0);
        // mark initial state as visited
        visited.add(0);
        int steps = 0;
        boolean found = false;
        while(!queue.isEmpty() && !found) {
            // process current level
            List<Integer> nextQueue = new ArrayList<>();
            for(int state : queue) {
                if(state == target) {
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
        // subtract 1 from steps because we incremented after finding the target
        return steps-1;
    }

    /**
     * section3 is a comma separated list of bit-flip counts.
     * the goal is to find the minimum number of presses to reach all counts at once.
     * each button press flips one or more bits at a time.
     * the search type is similar to part 1, but we need to track multiple targets.
     * @param machine the machine to solve
     * @param section3 the section3 string
     */
    private void part2(Machine machine,String section3) {
        // parse section3 into target State
        String[] counts = section3.split(",");
        State target = new State(counts.length);
        for(int i=0;i<counts.length;i++) {
            String countStr = counts[i];
            target.count.set(i,Integer.parseInt(countStr.trim()));
        }
        // find minimum presses for all target counts
        long presses = findMinimumPressesForVoltages(machine,target);
        sum += presses;
    }

    static class State {
        List<Integer> count;

        public State(int size) {
            count = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                count.add(0);
            }
        }

        public State(State from) {
            count = new ArrayList<>();
            count.addAll(from.count);
        }

        public boolean equals(State other) {
            // Because this is a small app we know States are always equal size.
            for(int i=0;i<count.size();i++) {
                // != is safe because these are Integers and cannot be null.
                if(count.get(i) != other.count.get(i)) {
                    return false;
                }
            }
            return true;
        }

        public int size() {
            return count.size();
        }

        public void press(int button) {
            for(int i=0;i<size();i++) {
                if((button & (1 << i)) != 0) {
                    count.set(i,count.get(i)+1);
                }
            }
        }

        public boolean exceeds(State target) {
            for(int i=0;i<count.size();i++) {
                if(count.get(i) > target.count.get(i)) {
                    return true;
                }
            }
            return false;
        }

        public void add(State other) {
            for(int i=0;i<count.size();i++) {
                count.set(i,count.get(i)+other.count.get(i));
            }
        }
    }

    /**
     * Find the minimum number of button presses to reach all target voltages.
     * the target voltages should not be exceeded, so we can ignore states that exceed any target.
     * A breadth-first search would exhaust the system's RAM as the target values are too high.
     *
     * @param machine the machine to solve
     * @param target the list of target voltages
     * @return the minimum number of button presses to reach all targets
     */
    private long findMinimumPressesForVoltages(Machine machine,State target) {

        return 0;
    }

    private boolean visitedContainsState(List<State> visited, State newState) {
        for(State state : visited) {
            if(state.equals(newState)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void postProcess() {
        System.out.println("Sum of minimum button presses: " + sum);
    }
}
