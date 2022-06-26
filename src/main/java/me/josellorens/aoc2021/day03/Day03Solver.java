package me.josellorens.aoc2021.day03;

import me.josellorens.aoc2021.DaySolver;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day03Solver implements DaySolver {


    final List<String> numbers;

    public Day03Solver(List<String> input) {
        this.numbers = input;
    }

    public String part1() {
        var gamma = 0;
        var numCols = numbers.get(0).length();
        for (var i = 0; i < numCols; i++) {
            gamma = gamma << 1;
            var average = getAverageForColumn(this.numbers, i);
            var newDigit = Math.round(average);
            gamma += newDigit;
        }
        var epsilon = invertBits(gamma, numCols);
        var result = gamma * epsilon;
        return String.valueOf(result);
    }

    public String part2() {
        var oxygenRating = parseInt(findRating(false), 2);
        var co2Rating = parseInt(findRating(true), 2);
        var result = oxygenRating * co2Rating;
        return String.valueOf(result);
    }

    private double getAverageForColumn(List<String> numbers, int column) {
        var average = 0.0;
        for (int j = 0; j < numbers.size(); j++) {
            String number = numbers.get(j);
            var digit = parseInt(number.substring(column, column + 1));
            average += (digit - average) / (j + 1);
        }
        return average;
    }

    private int invertBits(int gamma, int numColumns) {
        final var mask = (int) Math.pow(2, numColumns) - 1;
        return (~gamma) & mask;
    }

    private String findRating(boolean leastCommon) {
        var candidates = new ArrayList<>(this.numbers);
        for (var i = 0; i < candidates.get(0).length(); i++) {
            var average = getAverageForColumn(candidates, i);
            var bit = (int) Math.round(average);
            bit = leastCommon ? bit ^ 1 : bit;
            var bitString = String.valueOf(bit);

            var newCandidates = new ArrayList<String>();
            for (var candidate : candidates) {
                if (candidate.substring(i, i + 1).equals(bitString)) {
                    newCandidates.add(candidate);
                }
            }
            candidates = newCandidates;

            if (candidates.size() == 1) {
                return candidates.get(0);
            }
        }
        throw new IllegalStateException("A single candidate rating was not found");
    }

    public static void main(String[] args) {
        final var day03Solver = new Day03Solver(inputLinesForDay(3));
        final var day03part1result = timedExecution(day03Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day03part1result.milliseconds, day03part1result.result);
        final var day03part2result = timedExecution(day03Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day03part2result.milliseconds, day03part2result.result);
    }
}
