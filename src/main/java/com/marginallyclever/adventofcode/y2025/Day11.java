package com.marginallyclever.adventofcode.y2025;

import com.marginallyclever.adventofcode.AdventOfCode;

import java.util.*;

public class Day11 extends AdventOfCode {
    public static void main(String[] args) {
        new Day11().run();
    }

    int sum=0;
    Map<String, List<String>> nodes = new HashMap<>();
    Map<String, List<String>> reverseNodes = new HashMap<>();

    public Day11() {}

    @Override
    public void processLine(String line) {
        String [] parts = line.split(":");
        String nodeName = parts[0].trim();
        String [] edges = parts[1].trim().split(" ");
        List<String> edgeList = new ArrayList<>();
        for(String e : edges) {
            edgeList.add(e.trim());
        }
        nodes.put(nodeName, edgeList);
    }

    @Override
    public void postProcess() {
        //part1();
        part2();
    }

    private void part1() {
        sum = dfs("you");
        System.out.println("Sum: "+sum);
    }

    private int dfs(String node) {
        if(node.equals("out")) return 1;
        int total = 0;
        var edges = nodes.get(node);
        if(edges != null) {
            for(String edge : edges) {
                total += dfs(edge);
            }
        }
        return total;
    }

    /**
     * the number of routes is the number of ways to travel out-dac-fft-you,
     *                    plus the number of ways to travel out-fft-dac-you.
     */
    private void part2() {
        buildReverseGraph();
        long outDac = countReverse("out","dac");
        long dacFft = countReverse("dac","fft");
        long fftScr = countReverse("fft","svr");
        long total1 = outDac * dacFft * fftScr;

        long outfft = countReverse("out","fft");
        long fftDac = countReverse("fft","dac");
        long dacScr = countReverse("dac","svr");
        long total2 = outfft * fftDac * dacScr;

        long finalTotal = total1+total2;
        System.out.println("Final total: "+finalTotal);
    }
// 1031573533
// 1473787136
    /**
     * Build the reverse graph.
     */
    private void buildReverseGraph() {
        for(var entry : nodes.entrySet()) {
            String fromNode = entry.getKey();
            for (String toNode : entry.getValue()) {
                reverseNodes.putIfAbsent(toNode, new ArrayList<>());
                reverseNodes.get(toNode).add(fromNode);
            }
        }
    }

    /**
     * Build path count from end to start.
     * @param end the destination node
     * @param start the starting node
     * @return the number of paths from start to end
     */
    private long countReverse(String end, String start) {
        Map<String, Long> memo = new HashMap<>();
        return countPathsRec(start, end, memo);
    }

    private long countPathsRec(String node, String end, Map<String, Long> memo) {
        if (node.equals(end)) return 1L;
        if (memo.containsKey(node)) return memo.get(node);
        long total = 0L;
        var edges = nodes.get(node);
        if (edges != null) {
            for (String nxt : edges) {
                total += countPathsRec(nxt, end, memo);
            }
        }
        memo.put(node, total);
        return total;
    }
}
