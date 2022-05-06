package com.josellorens.day01;

import com.josellorens.Day;

import java.util.List;

import static com.josellorens.ExecutionUtil.timedExecution;
import static com.josellorens.InputUtil.inputLinesAsIntegersForDay;

public class Day01 implements Day {
    public static void main(String[] args) {
        final var numbers = inputLinesAsIntegersForDay(1);

        final var part1result = timedExecution(Day01::part1, numbers);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", part1result.milliseconds, part1result.result);

        final var part2result = timedExecution(Day01::part2, numbers);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", part2result.milliseconds, part2result.result);
    }

    private static String part1(List<Integer> numbers) {
        var increases = 0;
        for (var i = 1; i < numbers.size(); i++) {
            if (numbers.get(i) > numbers.get(i - 1)) {
                increases++;
            }
        }
        return String.format("%d", increases);
    }

    private static String part2(List<Integer> numbers) {
        var increases = 0;
        for (var i = 3; i < numbers.size(); i++) {
            if (windowStartingAt(numbers, i) > windowStartingAt(numbers, i - 1)) {
                increases++;
            }
        }
        return String.format("%d", increases);
    }

    private static int windowStartingAt(List<Integer> numbers, int i) {
        return numbers.get(i) + numbers.get(i - 1) + numbers.get(i - 2);
    }
}
