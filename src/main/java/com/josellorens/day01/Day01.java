package com.josellorens.day01;

import java.util.List;

import static com.josellorens.InputUtil.inputLinesAsIntegersForDay;

public class Day01 {
    public static void main(String[] args) {
        final var numbers = inputLinesAsIntegersForDay(1);

        final var  startTime = System.nanoTime();
        final var part1Solution = part1(numbers);
        final var  endTime = System.nanoTime();
        final var duration = (endTime - startTime) / 1_000_000F;
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", duration, part1Solution);

        final var  startTime2 = System.nanoTime();
        final var part2Solution = part2(numbers);
        final var  endTime2 = System.nanoTime();
        final var duration2 = (endTime2 - startTime2) / 1_000_000F;
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", duration2, part2Solution);
    }

    private static Integer part1(List<Integer> numbers) {
        var increases = 0;
        for (var i = 1; i < numbers.size(); i++) {
            if (numbers.get(i) > numbers.get(i - 1)) {
                increases++;
            }
        }
        return increases;
    }

    private static Integer part2(List<Integer> numbers) {
        var increases = 0;
        for (var i = 3; i < numbers.size(); i++) {
            if (windowStartingAt(numbers, i) > windowStartingAt(numbers, i - 1)) {
                increases++;
            }
        }
        return increases;
    }

    private static int windowStartingAt(List<Integer> numbers, int i) {
        return numbers.get(i) + numbers.get(i - 1) + numbers.get(i - 2);
    }
}
