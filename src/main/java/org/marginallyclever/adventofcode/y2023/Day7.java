package org.marginallyclever.adventofcode.y2023;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Day7 {
    public static String cardValues1 = "23456789TJQKA";
    public static String cardValues2 = "J23456789TQKA";

    class Hand {
        public String originalHand;
        public int [] cards = new int[5];
        public int bid;

        public Hand(String line) {
            String [] parts = line.split(" ");
            originalHand = parts[0];
            for(int i=0;i<5;++i) {
                cards[i] = parts[0].charAt(i);
            }
            bid = Integer.parseInt(parts[1]);
        }

        public int getType() {
            int[] counts = new int[13];
            for (int i = 0; i < 5; ++i) {
                counts[cardValues1.indexOf(cards[i])]++;
            }
            return getTypeWithOffset(counts, 0);
        }

        public int getTypeWild() {
            int [] counts = new int[13];
            for(int i=0;i<5;++i) {
                counts[cardValues2.indexOf(cards[i])]++;
            }
            if(counts[0]>4) {
                return 6; // Five Of A Kind
            }

            int type = getTypeWithOffset(counts, 1);
            for(int i=0;i<counts[0];++i) {
                type = upgradeType(type);
            }

            return type;
        }

        private int getTypeWithOffset(int[] counts, int startOffset) {
            int pairs = 0;
            int triples = 0;
            for(int i=startOffset;i<13;++i) {
                // FiveOfAKind
                if(counts[i]==5) return 6;
                // FourOfAKind
                if(counts[i]==4) return 5;
                // count for later
                if(counts[i]==3) triples++;
                if(counts[i]==2) pairs++;
            }

            // Full House
            if(triples==1 && pairs==1) return 4;
            // Three Of A Kind
            if(triples==1) return 3;
            // Two Pair
            if(pairs==2) return 2;
            // Pair
            if(pairs==1) return 1;
            // high card
            return 0;
        }

        private int upgradeType(int type) {
            return switch (type) {
                case 0 -> 1;  // high becomes pair
                case 1 -> 3;  // pair becomes triple
                case 2 -> 4;  // two pair becomes full house
                case 3 -> 5;  // triple becomes four of a kind
                case 4 -> 5;  // full house becomes four of a kind
                case 5 -> 6;  // four of a kind becomes five of a kind
                default -> type;
            };
        }
    }

    class HandComparator1 implements Comparator<Hand> {
        @Override
        public int compare(Hand o1, Hand o2) {
            int t1 = o1.getType();
            int t2 = o2.getType();
            if(t1!=t2) return t2-t1;
            for(int i=0;i<5;++i) {
                if(o1.cards[i]!=o2.cards[i]) return cardValues1.indexOf(o2.cards[i]) - cardValues1.indexOf(o1.cards[i]);
            }
            return 0;
        }
    }

    class HandComparator2 implements Comparator<Hand> {
        @Override
        public int compare(Hand o1, Hand o2) {
            int t1 = o1.getTypeWild();
            int t2 = o2.getTypeWild();
            if(t1!=t2) return t2-t1;
            for(int i=0;i<5;++i) {
                if(o1.cards[i]!=o2.cards[i]) return cardValues2.indexOf(o2.cards[i]) - cardValues2.indexOf(o1.cards[i]);
            }
            return 0;
        }
    }

    private List<Hand> hands = new ArrayList<>();

    private void findAnswer2() {
        hands.sort(new HandComparator2());
        hands = hands.reversed();
        System.out.println("sorted hands 2:");
        for (Hand h : hands) {
            System.out.println(h.originalHand + " " + h.bid + " " + h.getTypeWild());
        }
        getFinalScore();
    }

    private void findAnswer1() {
        hands.sort(new HandComparator1());
        hands = hands.reversed();
        System.out.println("sorted hands 1:");
        for (Hand h : hands) {
            System.out.println(h.originalHand + " " + h.bid + " " + h.getType());
        }
        getFinalScore();
    }

    private void getFinalScore() {
        int sum = 0;
        int i = 1;
        for (Hand h : hands) {
            sum += h.bid * i++;
        }
        System.out.println("sum = " + sum);
    }

    private void processLine(String line) {
        if(line.trim().isEmpty()) return;
        hands.add(new Hand(line));
    }

    private void processFile(String filename) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(Objects.requireNonNull(Day7.class.getResourceAsStream(filename)))))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Day7 me = new Day7();
        me.processFile("input.txt");
        long t0 = System.nanoTime();
        me.findAnswer1();
        long t1 = System.nanoTime();
        me.findAnswer2();
        long t2 = System.nanoTime();
        System.out.println("time 1 = "+(t1-t0)/1000000+"ms");
        System.out.println("time 2 = "+(t2-t1)/1000000+"ms");
    }
}