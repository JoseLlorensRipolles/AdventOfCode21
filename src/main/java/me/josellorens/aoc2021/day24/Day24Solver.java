package me.josellorens.aoc2021.day24;

import me.josellorens.aoc2021.DaySolver;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.*;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day24Solver implements DaySolver {

    private final int[] variable1;
    private final int[] variable2;

    public Day24Solver(List<String> inputLines) {
        variable1 = readVariables1(inputLines);
        variable2 = readVariables2(inputLines);
    }

    @Override
    public String part1() {
        return findModelNumber(true);
    }

    @Override
    public String part2() {
        return findModelNumber(false);
    }

    private String findModelNumber(boolean maxNumber) {
        final var result = new int[14];
        for (int i = 0; i < 14; i++) {
            if (variable1[i] > 0) {
                var divisorIndex = findDivisorIndex(i, variable1);
                int[] pairResult;
                if (maxNumber) {
                    pairResult = calculatePairMaximising(variable1, variable2, i, divisorIndex);
                } else {
                    pairResult = calculatePairMinimising(variable1, variable2, i, divisorIndex);
                }
                result[i] = pairResult[0];
                result[divisorIndex] = pairResult[1];
            }
        }
        var modelNumber = 0L;
        for (int i = 0; i < 14; i++) {
            modelNumber = modelNumber * 10;
            modelNumber += result[i];
        }
        return String.valueOf(modelNumber);
    }

    private int[] calculatePairMaximising(int[] variable1, int[] variable2, int dividendIndex, int divisorIndex) {
        var maximumSlack = abs(variable1[divisorIndex]) + 9;
        var dividendValue = min(9, maximumSlack - variable2[dividendIndex]);
        var slack = dividendValue + variable2[dividendIndex];
        var divisorValue = slack + variable1[divisorIndex];
        return new int[]{dividendValue, divisorValue};
    }

    private int[] calculatePairMinimising(int[] variable1, int[] variable2, int dividendIndex, int divisorIndex) {
        var minimumSlack = abs(variable1[divisorIndex]) + 1;
        var dividendValue = max(1, minimumSlack - variable2[dividendIndex]);
        var slack = dividendValue + variable2[dividendIndex];
        var divisorValue = slack + variable1[divisorIndex];
        return new int[]{dividendValue, divisorValue};
    }

    private int findDivisorIndex(int referenceIndex, int[] variable1) {
        var divisorsToPass = 0;
        for (int i = referenceIndex + 1; i < 14; i++) {
            if (divisorsToPass == 0 && variable1[i] < 0) {
                return i;
            }

            if (variable1[i] < 0) {
                divisorsToPass--;
            } else {
                divisorsToPass++;
            }
        }
        throw new IllegalStateException();
    }

    private static int[] readVariables1(List<String> inputLines) {
        final var variable1 = new int[14];
        Pattern pattern = Pattern.compile("add x (-?\\d+)");
        for (int i = 0; i < 14; i++) {
            var lineIndex = (i * 18) + 5;
            var line = inputLines.get(lineIndex);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                variable1[i] = Integer.parseInt(matcher.group(1));
            } else {
                throw new IllegalStateException();
            }
        }
        return variable1;
    }

    private static int[] readVariables2(List<String> inputLines) {
        final var variable1 = new int[14];
        Pattern pattern = Pattern.compile("add y (-?\\d+)");
        for (int i = 0; i < 14; i++) {
            var lineIndex = (i * 18) + 15;
            var line = inputLines.get(lineIndex);
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                variable1[i] = Integer.parseInt(matcher.group(1));
            } else {
                throw new IllegalStateException();
            }
        }
        return variable1;
    }

    public static void main(String[] args) {
        final var day24Solver = new Day24Solver(inputLinesForDay(24));
        final var day24part1result = timedExecution(day24Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day24part1result.milliseconds, day24part1result.result);
        final var day24part2result = timedExecution(day24Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day24part2result.milliseconds, day24part2result.result);
    }
}
