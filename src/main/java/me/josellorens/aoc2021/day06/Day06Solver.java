package me.josellorens.aoc2021.day06;

import me.josellorens.aoc2021.DaySolver;

import java.util.List;

import static java.util.Arrays.stream;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day06Solver implements DaySolver {

    private final long[] lanternFishes = new long[9];

    public Day06Solver(List<String> inputLines) {
        stream(inputLines.get(0).split(","))
            .map(Integer::parseInt)
            .forEach(number -> lanternFishes[number] += 1);
    }

    @Override
    public String part1() {
        for (int day = 0; day < 80; day++) {
            completeDay();
        }
        return String.valueOf(numberOfFishes());
    }

    @Override
    public String part2() {
        for (int day = 80; day < 256; day++) {
            completeDay();
        }
        return String.valueOf(numberOfFishes());
    }

    private void completeDay() {
        final var fishesEndingCycle = lanternFishes[0];
        shiftLeft(lanternFishes);
        lanternFishes[6] += fishesEndingCycle;
        lanternFishes[8] = fishesEndingCycle;
    }

    private void shiftLeft(long[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            array[i] = array[i + 1];
        }
    }

    private long numberOfFishes() {
        return stream(lanternFishes).sum();
    }

    public static void main(String[] args) {
        final var day06Solver = new Day06Solver(inputLinesForDay(6));
        final var day06part1result = timedExecution(day06Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day06part1result.milliseconds, day06part1result.result);
        final var day06part2result = timedExecution(day06Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day06part2result.milliseconds, day06part2result.result);
    }
}
