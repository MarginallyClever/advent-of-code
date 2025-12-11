package com.marginallyclever.adventofcode.y2025;

import com.marginallyclever.adventofcode.AdventOfCode;

import java.util.ArrayList;
import java.util.List;

public class Day7 extends AdventOfCode {
    public static void main(String[] args) {
        new Day7().run();
    }

    int sum=0;

    // track the number of splits over time
    static public class Beam {
        int pos;
        long multiplier = 1;

        public Beam(int position) {
            this.pos = position;
        }
    }
    private List<Beam> beams = new ArrayList<>();

    @Override
    public void processLine(String line) {
        List<Beam> newBeams = new ArrayList<>();
        if(beams.isEmpty()) {
            // must be first line.  scan line for 'S' position.
            for(int i=0; i<line.length(); i++) {
                char c = line.charAt(i);
                if(c=='S') {
                    newBeams.add(new Beam(i));
                }
            }
        } else {
            // every line after the first.
            // look for a '^' at the same position as a beam.
            for (Beam b : beams) {
                if (b.pos >= 0 && b.pos < line.length()) {
                    if(line.charAt(b.pos) == '^') {
                        // found a splitter.  count the splits.
                        sum++;

                        // check if two beams are about to merge and combine their multipliers
                        mergeBeams(newBeams, b.pos - 1, b.multiplier);
                        mergeBeams(newBeams, b.pos + 1, b.multiplier);
                        continue;
                    }
                }
                mergeBeams(newBeams, b.pos, b.multiplier);
            }

        }
        beams = newBeams;
    }

    private void mergeBeams(List<Beam> newBeams, int i, long multiplier) {
        Beam existing = getBeam(newBeams,i);
        if(existing != null) {
            existing.multiplier += multiplier;
        } else {
            Beam b = new Beam(i);
            b.multiplier = multiplier;
            newBeams.add(b);
        }
    }

    // return any branch at position i
    private Beam getBeam(List<Beam> newBeams, int i) {
        for(Beam b : newBeams) {
            if(b.pos==i) return b;
        }
        return null;
    }

    @Override
    public void postProcess() {
        // print final branch positions
        System.out.println("splits:"+sum);
        // the sum of all multipliers
        long total=0;
        for(Beam b : beams) {
            total+=b.multiplier;
        }
        System.out.println("total branches:"+total);
    }
}

