package me.josellorens.aoc2021.day14;

import me.josellorens.aoc2021.DaySolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day14Solver implements DaySolver {

    private final String initialTemplate;
    private final Map<String, String> insertionRules;

    public Day14Solver(List<String> input) {
        initialTemplate = input.get(0);
        insertionRules = range(2, input.size())
            .mapToObj(it -> input.get(it).split(" -> "))
            .collect(toMap(it -> it[0], it-> it[1]));
    }

    @Override
    public String part1() {
        return resultAtStep(10);
    }

    @Override
    public String part2() {
        return resultAtStep(40);
    }

    private String resultAtStep(int steps) {
        var pairCount = range(0, initialTemplate.length() - 1)
            .mapToObj(it -> initialTemplate.substring(it, it + 2))
            .collect(Collectors.groupingBy(identity(), counting()));

        for (int step = 0; step < steps; step++) {
            final var newPairCount = new HashMap<String, Long>();
            pairCount.forEach((pair, count) -> {
                final var insertedElement = insertionRules.get(pair);
                final var leftPair = pair.charAt(0) + insertedElement;
                final var rightPair = insertedElement + pair.charAt(1);

                newPairCount.put(leftPair, newPairCount.getOrDefault(leftPair, 0L) + count);
                newPairCount.put(rightPair, newPairCount.getOrDefault(rightPair, 0L) + count);
            });
            pairCount = newPairCount;
        }

        final var charCount = pairCount.entrySet()
            .stream()
            .collect(toMap(it -> it.getKey().charAt(1), Map.Entry::getValue, Math::addExact));
        final var firstLetter = initialTemplate.charAt(0);
        charCount.put(firstLetter, charCount.get(firstLetter) + 1);

        final var maxCharCount = charCount.values().stream().max(Long::compareTo).orElseThrow();
        final var minCharCount = charCount.values().stream().min(Long::compareTo).orElseThrow();
        return String.valueOf(maxCharCount - minCharCount);
    }

    public static void main(String[] args) {
        final var day14Solver = new Day14Solver(inputLinesForDay(14));
        final var day14part1result = timedExecution(day14Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day14part1result.milliseconds, day14part1result.result);
        final var day14part2result = timedExecution(day14Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day14part2result.milliseconds, day14part2result.result);
    }
}
