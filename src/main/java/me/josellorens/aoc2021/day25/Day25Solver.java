package me.josellorens.aoc2021.day25;

import me.josellorens.aoc2021.DaySolver;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.deepEquals;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day25Solver implements DaySolver {

    private final char[][] originalMap;

    public Day25Solver(List<String> inputLines) {
        originalMap = parseInput(inputLines);
    }

    private char[][] parseInput(List<String> inputLines) {
        final var height = inputLines.size();
        final var width = inputLines.get(0).length();
        final var map = new char[width][height];

        for (int y = 0; y < height; y++) {
            final var line = inputLines.get(height - y - 1);
            for (int x = 0; x < width; x++) {
                map[x][y] = line.charAt(x);
            }
        }
        return map;
    }

    @Override
    public String part1() {
        var map = cloneMap(originalMap);
        var changes = true;
        var steps = 0;
        while (changes) {
            var newMap = step(map);
            if (deepEquals(newMap, map)) {
                changes = false;
            }
            map = newMap;
            steps++;
        }
        return String.valueOf(steps);
    }

    @Override
    public String part2() {
        return "Automatically unlocked if the other 49 stars are present";
    }

    private char[][] cloneMap(char[][] originalMap) {
        return Arrays.stream(originalMap).map(char[]::clone).toArray(char[][]::new);
    }

    private char[][] step(char[][] map) {
        var newMap = cloneMap(map);
        newMap = moveEast(newMap);
        newMap = moveSouth(newMap);
        return newMap;
    }

    private char[][] moveEast(char[][] map) {
        final var newMap = cloneMap(map);
        for (int y = 0; y < map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                var potentialX = (x + 1) % map.length;
                if (map[x][y] == '>' && map[potentialX][y] == '.') {
                    newMap[potentialX][y] = '>';
                    newMap[x][y] = '.';
                }
            }
        }
        return newMap;
    }

    private char[][] moveSouth(char[][] map) {
        final var newMap = cloneMap(map);
        for (int y = 0; y < map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                var potentialY = (y - 1) < 0 ? map[0].length - 1 : y - 1;
                if (map[x][y] == 'v' && map[x][potentialY] == '.') {
                    newMap[x][potentialY] = 'v';
                    newMap[x][y] = '.';
                }
            }
        }
        return newMap;
    }

    public static void main(String[] args) {
        final var day25Solver = new Day25Solver(inputLinesForDay(25));
        final var day25part1result = timedExecution(day25Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day25part1result.milliseconds, day25part1result.result);
        final var day25part2result = timedExecution(day25Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day25part2result.milliseconds, day25part2result.result);
    }
}
