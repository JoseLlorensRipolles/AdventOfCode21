package me.josellorens.aoc2021.day01;

import me.josellorens.aoc2021.DaySolver;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day01Solver implements DaySolver {

    final List<Integer> numbers;

    public Day01Solver(List<String> input) {
        this.numbers = input.stream()
            .map(Integer::parseInt)
            .collect(toList());
    }

    public String part1() {
        var increases = 0;
        for (var i = 1; i < numbers.size(); i++) {
            if (numbers.get(i) > numbers.get(i - 1)) {
                increases++;
            }
        }
        return String.format("%d", increases);
    }

    public String part2() {
        var increases = 0;
        for (var i = 3; i < numbers.size(); i++) {
            if (windowStartingAt(numbers, i) > windowStartingAt(numbers, i - 1)) {
                increases++;
            }
        }
        return String.format("%d", increases);
    }

    private int windowStartingAt(List<Integer> numbers, int i) {
        return numbers.get(i) + numbers.get(i - 1) + numbers.get(i - 2);
    }

    public static void main(String[] args) {
        final var day1Solver = new Day01Solver(inputLinesForDay(1));
        final var day1part1result = timedExecution(day1Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day1part1result.milliseconds, day1part1result.result);
        final var day1part2result = timedExecution(day1Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day1part2result.milliseconds, day1part2result.result);
    }
}
