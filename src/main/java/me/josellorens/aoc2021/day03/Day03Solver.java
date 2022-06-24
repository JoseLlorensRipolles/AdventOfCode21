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
        var gammaBuilder = new StringBuilder();
        for (var i = 0; i < numbers.get(0).length(); i++) {
            var average = 0.0;
            for (int j = 0; j < numbers.size(); j++) {
                String number = numbers.get(j);
                var digit = parseInt(number.substring(i, i + 1));
                average = addToOnlineAverage(average, digit, j + 1);
            }
            int newDigit = (int) Math.round(average);
            gammaBuilder.append(newDigit);
        }
        var gamma = gammaBuilder.toString();
        var epsilon = invert(gamma);

        var result = parseInt(gamma, 2) * parseInt(epsilon, 2);
        return result + "";
    }

    private String invert(String gamma) {
        var epsilonBuilder = new StringBuilder();
        for (var digit : gamma.toCharArray()) {
            if (digit == '0') {
                epsilonBuilder.append('1');
            } else {
                epsilonBuilder.append('0');
            }
        }
        return epsilonBuilder.toString();
    }

    private double addToOnlineAverage(double currentAverage, int newValue, int numberOfValues) {
        return currentAverage + ((newValue - currentAverage) / numberOfValues);

    }

    public String part2() {
        var oxygenValue = parseInt(findRating(false), 2);
        var co2Value = parseInt(findRating(true), 2);
        var result = oxygenValue * co2Value;
        return result + "";
    }

    private String findRating(boolean invert) {
        var candidates = new ArrayList<>(this.numbers);
        for (var i = 0; i < candidates.get(0).length(); i++) {
            var average = 0.0;
            for (int j = 0; j < candidates.size(); j++) {
                var number = candidates.get(j);
                var digit = parseInt(number.substring(i, i + 1));
                average = addToOnlineAverage(average, digit, j + 1);
            }
            var aux = average == 0.5 ? 1 : Math.round(average);
            if (invert) {
                aux = aux == 1 ? 0 : 1;
            }
            var referenceBit = aux + "";

            var newCandidates = new ArrayList<String>();
            for (var candidate : candidates) {
                if (candidate.substring(i, i + 1).equals(referenceBit)) {
                    newCandidates.add(candidate);
                }
            }
            candidates = newCandidates;

            if (candidates.size() == 1) {
                return candidates.get(0);
            }
        }
        throw new IllegalStateException();
    }

    public static void main(String[] args) {
        final var day03Solver = new Day03Solver(inputLinesForDay(3));
        final var day03part1result = timedExecution(day03Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day03part1result.milliseconds, day03part1result.result);
        final var day03part2result = timedExecution(day03Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day03part2result.milliseconds, day03part2result.result);
    }
}
