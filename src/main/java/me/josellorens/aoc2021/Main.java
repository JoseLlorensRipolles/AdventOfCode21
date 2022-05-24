package me.josellorens.aoc2021;

import me.josellorens.aoc2021.day01.Day01Solver;
import me.josellorens.aoc2021.day02.Day02Solver;

import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Main {
    public static void main(String[] args) {
        final var day01Solver = new Day01Solver(inputLinesForDay(1));
        final var part1result = timedExecution(day01Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", part1result.milliseconds, part1result.result);
        final var part2result = timedExecution(day01Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", part2result.milliseconds, part2result.result);


        final var day02Solver = new Day02Solver(inputLinesForDay(2));
        final var day02part1result = timedExecution(day02Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day02part1result.milliseconds, day02part1result.result);
        final var day02part2result = timedExecution(day02Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day02part2result.milliseconds, day02part2result.result);
    }
}
