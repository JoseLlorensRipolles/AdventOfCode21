package me.josellorens.aoc2021.day15;

import me.josellorens.aoc2021.DaySolver;
import me.josellorens.aoc2021.day05.Point;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.IntStream;

import static java.util.Comparator.comparingInt;
import static me.josellorens.aoc2021.day05.Point.point;
import static me.josellorens.aoc2021.day15.Node.Builder.node;
import static me.josellorens.aoc2021.utils.ExecutionUtil.timedExecution;
import static me.josellorens.aoc2021.utils.InputUtil.inputLinesForDay;

public class Day15Solver implements DaySolver {

    int[][] regularMap;

    public Day15Solver(List<String> inputLines) {
        final var maxY = inputLines.size();
        final var maxX = inputLines.get(0).length();

        regularMap = new int[maxX][maxY];

        for (int y = 0; y < maxY; y++) {
            final var line = inputLines.get(y);
            for (int x = 0; x < maxX; x++) {
                regularMap[x][y] = Character.getNumericValue(line.charAt(x));
            }
        }
    }


    @Override
    public String part1() {
        return pathCost(regularMap);
    }

    @Override
    public String part2() {
        final var bigMap = bigMap();
        return pathCost(bigMap);
    }

    private String pathCost(int[][] costMap) {
        final int maxX = costMap.length;
        final int maxY = costMap[0].length;

        final var queue = new PriorityQueue<Node>(maxX * maxY, comparingInt(a -> a.costToReach));
        final var visitedCoordinates = new HashSet<Point>();
        queue.add(node().coordinates(point(0, 0)).costToReach(0).build());

        while (!queue.isEmpty()) {
            final var node = queue.poll();
            final var coordinates = node.coordinates;
            visitedCoordinates.add(coordinates);

            for (Point adjacentCoordinates : coordinates.adjacentWithoutDiagonals(maxX - 1, maxY - 1)) {
                if (!visitedCoordinates.contains(adjacentCoordinates)) {
                    final var adjacentCost = costMap[adjacentCoordinates.x][adjacentCoordinates.y] + node.costToReach;
                    if (adjacentCoordinates.equals(point(maxX - 1, maxY - 1))) {
                        return String.valueOf(adjacentCost);
                    }
                    final var adjacentNode = node()
                        .coordinates(adjacentCoordinates)
                        .costToReach(adjacentCost)
                        .build();
                    queue.add(adjacentNode);
                    visitedCoordinates.add(adjacentCoordinates);
                }
            }
        }
        throw new IllegalStateException();
    }

    private int[][] bigMap() {
        final var maxX = regularMap.length;
        final var maxY = regularMap[0].length;
        final var bigMap = new int[maxX * 5][maxY * 5];
        for (int extraRow = 0; extraRow < 5; extraRow++) {
            for (int x = 0; x < maxX; x++) {
                for (int y = 0; y < maxY; y++) {
                    int reminder = (regularMap[x][y] + extraRow) % 10;
                    int quotient = (regularMap[x][y] + extraRow) / 10;
                    bigMap[(extraRow * maxX) + x][y] = reminder + quotient;
                }
            }
        }

        IntStream.range(1, 5)
            .parallel()
            .forEach(it -> {
                for (int extraColumn = 0; extraColumn < 5; extraColumn++) {
                    for (int x = 0; x < maxX; x++) {
                        for (int y = 0; y < maxY; y++) {
                            int reminder = (bigMap[(it * maxX) + x][y] + extraColumn) % 10;
                            int quotient = (bigMap[(it * maxX) + x][y] + extraColumn) / 10;
                            bigMap[(extraColumn * maxX) + x][(it * maxY) + y] = reminder + quotient;
                        }
                    }
                }
            });
        return bigMap;
    }

    public static void main(String[] args) {
        final var day15Solver = new Day15Solver(inputLinesForDay(15));
        final var day15part1result = timedExecution(day15Solver::part1);
        System.out.printf("[%.2f ms] Part1 solution: %s. %n", day15part1result.milliseconds, day15part1result.result);
        final var day15part2result = timedExecution(day15Solver::part2);
        System.out.printf("[%.2f ms] Part2 solution: %s. %n", day15part2result.milliseconds, day15part2result.result);
    }
}
