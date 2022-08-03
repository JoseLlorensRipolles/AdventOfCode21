package me.josellorens.aoc2021.day08;

import me.josellorens.aoc2021.DaySolver;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static me.josellorens.aoc2021.day08.Input08Line.Builder.input08Line;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day08Solver implements DaySolver {

    private final List<Input08Line> input;

    public Day08Solver(List<String> inputLines) {
        final var inputsSplitBySection = inputLines
            .stream()
            .map(it -> it.split(" \\| "))
            .collect(toList());

        final var uniqueEntries = inputsSplitBySection
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
        for (int i = 0; i < uniqueEntries.size(); i++) {
            input.add(input08Line()
                .uniquePatterns(uniqueEntries.get(i))
                .outputPatterns(outputs.get(i))
                .build());
        }
    }

    @Override
    public String part1() {
        final var segmentCount = input
            .parallelStream()
            .map(it -> it.outputPatterns)
            .map(it -> it.stream()
                .filter(pattern -> {
                    final var size = pattern.size();
                    return size == 2 || size == 3 || size == 4 || size == 7;
                })
                .count())
            .reduce(0L, Long::sum);
        return String.valueOf(segmentCount);
    }

    @Override
    public String part2() {
        final var valuesBySegmentPatterns = input
            .parallelStream()
            .map(it -> {
                final var patterns = it.uniquePatterns;

                final var one = getBySize(patterns, 2);
                final var four = getBySize(patterns, 4);
                final var seven = getBySize(patterns, 3);
                final var eight = getBySize(patterns, 7);
                final var three = getBySizeAndIntersectionSize(patterns, 5, one, 2);
                final var six = getBySizeAndIntersectionSize(patterns, 6, seven, 2);
                final var nine = getBySizeAndIntersectionSize(patterns, 6, four, 4);
                final var two = getBySizeAndIntersectionSize(patterns, 5, four, 2);
                final var five = getBySizeAndIntersectionSize(patterns, 5, six, 5);
                final var zero = getBySizeAndIntersectionSize(patterns, 6, five, 4);

                return Map.of(
                    zero, 0,
                    one, 1,
                    two, 2,
                    three, 3,
                    four, 4,
                    five, 5,
                    six, 6,
                    seven, 7,
                    eight, 8,
                    nine, 9);
            })
            .collect(toList());

        var totalValue = 0;
        for (int i = 0; i < input.size(); i++) {
            final var valuesBySegmentPattern = valuesBySegmentPatterns.get(i);
            final var outputValues = input.get(i).outputPatterns;

            var currentValue = 0;
            for (int j = 0; j < 4; j++) {
                currentValue += valuesBySegmentPattern.get(outputValues.get(j)) * (1000 / (Math.pow(10, j)));
            }
            totalValue += currentValue;

        }
        return String.valueOf(totalValue);
    }

    private static Set<Character> getBySizeAndIntersectionSize(List<Set<Character>> patterns,
                                                               int size,
                                                               Set<Character> intersecting,
                                                               int intersectionSize) {
        return patterns.stream()
            .filter(it -> it.size() == size)
            .filter(it -> !it.equals(intersecting))
            .filter(it -> {
                final var intersection = new HashSet<>(it);
                intersection.retainAll(intersecting);
                return intersection.size() == intersectionSize;
            })
            .findFirst()
            .orElseThrow();
    }

    private static Set<Character> getBySize(List<Set<Character>> patterns, int size) {
        return patterns.stream()
            .filter(it -> it.size() == size)
            .findFirst()
            .orElseThrow();
    }

    public static void main(String[] args) {
        final var day08Solver = new Day08Solver(inputLinesForDay(8));
        final var day08part1result = timedExecution(day08Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day08part1result.milliseconds, day08part1result.result);
        final var day08part2result = timedExecution(day08Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day08part2result.milliseconds, day08part2result.result);
    }
}
