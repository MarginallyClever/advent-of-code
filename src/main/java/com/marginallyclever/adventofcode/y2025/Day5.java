package com.marginallyclever.adventofcode.y2025;

import com.marginallyclever.adventofcode.AdventOfCode;

import javax.vecmath.Vector2d;
import java.util.ArrayList;
import java.util.List;

public class Day5 extends AdventOfCode {
    public static void main(String[] args) {
        new Day5();
    }

    private long sum=0;
    boolean phaseTwo=false;
    private List<Vector2d> ranges;

    @Override
    public void processLine(String line) {
        if(ranges==null) ranges = new ArrayList<>();

        if(line.trim().isEmpty()) {
            phaseTwo = true;
            return;
        }
        if(phaseTwo) {
            var value = Long.parseLong(line);
            boolean inRange = false;
            for(Vector2d range : ranges) {
                if(value >= range.x && value <= range.y) {
                    inRange = true;
                    break;
                }
            }
            if(inRange) {
                sum++;
                return;
            }
        } else {
            var parts = line.split("-");
            ranges.add(new Vector2d(Long.parseLong(parts[0]), Long.parseLong(parts[1])));
        }
    }

    @Override
    public void postProcess() {
        // merge overlapping ranges.  Sort them and then look for overlaps.
        List<Vector2d> mergedRanges = new ArrayList<>();
        Vector2d lastRange = null;
        ranges.sort((a, b) -> Double.compare(a.x, b.x));
        for(Vector2d range : ranges) {
            if (mergedRanges.isEmpty()) {
                mergedRanges.add(range);
                lastRange = range;
            } else {
                assert lastRange != null;
                if (range.x <= lastRange.y + 1) {
                    // lastRange is the instance in mergedRanges.  We can modify it directly.
                    lastRange.y = Math.max(lastRange.y, range.y);
                } else {
                    mergedRanges.add(range);
                    lastRange = range;
                }
            }
        }
        // sum up the sizes of the merged ranges
        long sum2 = 0;
        for( Vector2d r : mergedRanges ) {
            sum2 += ((long)r.y - (long)r.x + 1);
        }

        System.out.println("Sum: " + sum);
        System.out.println("Sum2: " + sum2);
    }
}
