package com.josellorens.day01;

import java.util.List;

import static com.josellorens.InputUtil.inputLinesAsIntegersForDay;

public class Day01 {
    public static void main(String[] args) {
        final var numbers = inputLinesAsIntegersForDay(1);

        System.out.printf("Part1 solution: %s%n", part1(numbers));
        System.out.printf("Part2 solution: %s%n", part2(numbers));
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
        for (var i = 3; i < numbers.size() - 2; i++) {
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
