package me.josellorens.aoc2021.day08;

import me.josellorens.aoc2021.DaySolver;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static me.josellorens.aoc2021.day08.Input08Line.Builder.input08Line;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day08Solver implements DaySolver {

    private List<Input08Line> input;

    public Day08Solver(List<String> inputLines) {
        final var inputsSplitBySection = inputLines
            .stream()
            .map(it -> it.split(" \\| "))
            .collect(toList());

        final var entries = inputsSplitBySection
            .stream()
            .map(it -> it[0])
            .map(it -> it.split(" "))
            .map(Arrays::asList)
            .collect(toList());

        final var outputs = inputsSplitBySection
            .stream()
            .map(it -> it[1])
            .map(it -> it.split(" "))
            .map(Arrays::asList)
            .collect(toList());

        input = new ArrayList<>();
        for (int i = 0; i < entries.size(); i++) {
            input.add(input08Line()
                .uniquePatterns(entries.get(i))
                .outputPatterns(outputs.get(i))
                .build());
        }
    }

    @Override
    public String part1() {
        final var segmentCount = input.stream()
            .map(it -> it.outputPatterns)
            .flatMap(it -> it.stream()
                .filter(pattern -> {
                    final var segmentsSize = pattern.size();
                    return segmentsSize == 2 || segmentsSize == 3 || segmentsSize == 4 || segmentsSize == 7;
                }))
            .collect(toList());
        var aux = segmentCount.size();
        return String.valueOf(aux);
    }

    @Override
    public String part2() {
        final var valuesBySegmentPatterns = new ArrayList<Map<Set<Character>, Integer>>();
        for (Input08Line input08Line : input) {
            final var map = new HashMap<Set<Character>, Integer>();
            final var patterns = input08Line.uniquePatterns;

            final var one = patterns.stream()
                .filter(it -> it.size() == 2)
                .findFirst()
                .orElseThrow();

            final var four = patterns.stream()
                .filter(it -> it.size() == 4)
                .findFirst()
                .orElseThrow();

            final var seven = patterns.stream()
                .filter(it -> it.size() == 3)
                .findFirst()
                .orElseThrow();

            final var eight = patterns.stream()
                .filter(it -> it.size() == 7)
                .findFirst()
                .orElseThrow();

            final var three = patterns.stream()
                .filter(it -> it.size() == 5)
                .filter(it -> !it.equals(one))
                .filter(it -> {
                    final var intersection = new HashSet<>(it);
                    intersection.retainAll(one);
                    return intersection.size() == 2;
                })
                .findFirst()
                .orElseThrow();

            final var six = patterns.stream()
                .filter(it -> it.size() == 6)
                .filter(it -> !it.equals(seven))
                .filter(it -> {
                    final var intersection = new HashSet<>(it);
                    intersection.retainAll(seven);
                    return intersection.size() == 2;
                })
                .findFirst()
                .orElseThrow();

            final var nine = patterns.stream()
                .filter(it -> it.size() == 6)
                .filter(it -> !it.equals(four))
                .filter(it -> {
                    final var intersection = new HashSet<>(it);
                    intersection.retainAll(four);
                    return intersection.size() == 4;
                })
                .findFirst()
                .orElseThrow();

            final var two = patterns.stream()
                .filter(it -> it.size() == 5)
                .filter(it -> !it.equals(six))
                .filter(it -> !it.equals(three))
                .filter(it -> {
                    final var intersection = new HashSet<>(it);
                    intersection.retainAll(six);
                    return intersection.size() == 4;
                })
                .findFirst()
                .orElseThrow();

            final var five = patterns.stream()
                .filter(it -> it.size() == 5)
                .filter(it -> !it.equals(six))
                .filter(it -> {
                    final var intersection = new HashSet<>(it);
                    intersection.retainAll(six);
                    return intersection.size() == 5;
                })
                .findFirst()
                .orElseThrow();

            final var zero = patterns.stream()
                .filter(it -> it.size() == 6)
                .filter(it -> !it.equals(six) && !it.equals(nine))
                .findFirst()
                .orElseThrow();

            map.put(zero, 0);
            map.put(one, 1);
            map.put(two, 2);
            map.put(three, 3);
            map.put(four, 4);
            map.put(five, 5);
            map.put(six, 6);
            map.put(seven, 7);
            map.put(eight, 8);
            map.put(nine, 9);
            valuesBySegmentPatterns.add(map);
        }

        var totalValue = 0;
        for (int i = 0; i < input.size(); i++) {
            final var valuesBySegmentPattern = valuesBySegmentPatterns.get(i);
            final var outputValues = input.get(i).outputPatterns;
            totalValue += valuesBySegmentPattern.get(outputValues.get(0)) * 1000
                + valuesBySegmentPattern.get(outputValues.get(1)) * 100
                + valuesBySegmentPattern.get(outputValues.get(2)) * 10
                + valuesBySegmentPattern.get(outputValues.get(3));

        }
        return String.valueOf(totalValue);
    }

    public static void main(String[] args) {
        final var day08Solver = new Day08Solver(inputLinesForDay(8));
        final var day08part1result = timedExecution(day08Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day08part1result.milliseconds, day08part1result.result);
        final var day08part2result = timedExecution(day08Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day08part2result.milliseconds, day08part2result.result);
    }
}
